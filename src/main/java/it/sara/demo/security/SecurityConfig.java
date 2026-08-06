package it.sara.demo.security;

import io.jsonwebtoken.*;
import it.sara.demo.exception.GenericException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.requiredPolicy}")
    private String requiredPolicy;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    FilterChain chain) throws IOException, ServletException {
                        String header = request.getHeader("Authorization");
                        if (header == null || !header.startsWith("Bearer ")) {
                            chain.doFilter(request, response);
                            return;
                        }
                        String token = header.substring(7);
                        try {
                            Jws<Claims> jws = Jwts.parserBuilder()
                                    .setSigningKey(secret.getBytes())
                                    .requireIssuer(issuer)
                                    .build()
                                    .parseClaimsJws(token);

                            Claims claims = jws.getBody();
                            if (!requiredPolicy.equals(claims.get("policy"))) {
                                throw new GenericException(401, "Policy not satisfied");
                            }

                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null, List.of());
                            SecurityContextHolder.getContext().setAuthentication(auth);

                        } catch (ExpiredJwtException e) {
                            try {
                                throw new GenericException(401, "Token expired");
                            } catch (GenericException ex) {
                                throw new RuntimeException(ex);
                            }
                        } catch (JwtException | GenericException e) {
                            try {
                                throw new GenericException(401, "Invalid token");
                            } catch (GenericException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        chain.doFilter(request, response);
                    }
                }, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
