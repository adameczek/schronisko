package pl.inzynierka.schronisko.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.inzynierka.schronisko.configurations.security.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Component
public class AuthenticationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<AuthResponse> login(final AuthRequest request) {
        final Authentication authenticate =
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword()));

        final String token = this.jwtTokenProvider.createToken(authenticate);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).body(new AuthResponse(token));
    }
}
