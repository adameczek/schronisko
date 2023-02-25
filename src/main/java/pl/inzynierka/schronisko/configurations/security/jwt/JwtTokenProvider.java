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
		final var secret = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
		secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String createToken(final Authentication authentication) {
		final String username = authentication.getName();
		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		final Claims claims = Jwts.claims().setSubject(username);
		if (!authorities.isEmpty()) {
			claims.put(JwtTokenProvider.AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
		}

		final Date now = new Date();
		final Date validity = new Date(now.getTime() + jwtProperties.getValidityInMs());

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(validity)
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();

	}

	public Authentication getAuthentication(final String token) {
		final Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

		final Object authoritiesClaim = claims.get(JwtTokenProvider.AUTHORITIES_KEY);

		final Collection<? extends GrantedAuthority> authorities = null == authoritiesClaim ? AuthorityUtils.NO_AUTHORITIES :
				AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

		final User user = User.builder().username(claims.getSubject())
				.password("")
				.roles(authorities.stream().map(grantedAuthority -> Role.valueOf(grantedAuthority.getAuthority())).toList())
				.build();
		return new UsernamePasswordAuthenticationToken(user, token, authorities);
	}

	public boolean validateToken(final String token) {
		try {
			final Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			// parseClaimsJws will check expiration date. No need do here.
			JwtTokenProvider.log.info("expiration date: {}", claims.getBody().getExpiration());
			return true;
		} catch (final JwtException | IllegalArgumentException e) {
			JwtTokenProvider.log.info("Invalid JWT token: {}", e.getMessage());
			JwtTokenProvider.log.trace("Invalid JWT token trace.", e);
		}
		return false;
	}
}
