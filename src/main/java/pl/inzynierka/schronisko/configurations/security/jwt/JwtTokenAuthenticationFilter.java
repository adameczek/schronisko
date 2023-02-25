package pl.inzynierka.schronisko.configurations.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	public static final String HEADER_PREFIX = "Bearer ";
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
		final String token = this.resolveToken((HttpServletRequest) servletRequest);
		JwtTokenAuthenticationFilter.log.info("Extracting token from HttpServletRequest: {}", token);

		if (null != token && this.jwtTokenProvider.validateToken(token)) {
			final Authentication auth = this.jwtTokenProvider.getAuthentication(token);

			if (null != auth && !(auth instanceof AnonymousAuthenticationToken)) {
				final SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(auth);
				SecurityContextHolder.setContext(context);
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String resolveToken(final HttpServletRequest request) {
		final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenAuthenticationFilter.HEADER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
