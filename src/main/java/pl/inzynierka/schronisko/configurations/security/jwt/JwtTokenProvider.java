package pl.inzynierka.schronisko.configurations.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import pl.inzynierka.schronisko.configurations.security.UserPrincipal;
import pl.inzynierka.schronisko.shelters.models.Shelter;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import static java.util.stream.Collectors.joining;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "roles";
    
    private final JwtProperties jwtProperties;
    
    private SecretKey secretKey;
    
    @PostConstruct
    public void init() {
        var secret = Base64.getEncoder().encodeToString(this.jwtProperties.getSecretKey().getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Claims claims = Jwts.claims().setSubject(username);
        if (!authorities.isEmpty()) {
            claims.put(AUTHORITIES_KEY,
                       authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        }
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        claims.setId(String.valueOf(user.getUser().getId()));
        claims.put("username", user.getUser().getUsername());
        claims.put("email", user.getUser().getEmail());
        
        if (user.getUser().getShelter() != null) {
            claims.put("shelter", user.getUser().getShelter().getName());
            claims.put("shelter-id", user.getUser().getShelter().getId());
        }
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.jwtProperties.getValidityInMs());
        
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .signWith(this.secretKey,
                             SignatureAlgorithm.HS256)
                   .compact();
        
    }
    
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).getBody();
        
        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);
        
        Collection<? extends GrantedAuthority> authorities = null == authoritiesClaim ?
                                                             AuthorityUtils.NO_AUTHORITIES :
                                                             AuthorityUtils.commaSeparatedStringToAuthorityList(
                                                                     authoritiesClaim.toString());
        
        String shelterName = claims.get("shelter", String.class);
        Long shelterId = claims.get("shelter-id", Long.class);
        
        Shelter shelter = shelterName == null || shelterId == null ? null : Shelter.builder().id(shelterId).name(shelterName).build();
        
        User user = User.builder()
                        .username(String.valueOf(claims.get("username")))
                        .email(claims.getSubject())
                        .id(Long.parseLong(claims.getId()))
                        .password("")
                        .shelter(shelter)
                        .roles(authorities.stream()
                                          .map(grantedAuthority -> Role.valueOf(grantedAuthority.getAuthority()))
                                          .toList())
                        .build();
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }
    
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);
            // parseClaimsJws will check expiration date. No need do here.
            log.info("expiration date: {}", claims.getBody().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
