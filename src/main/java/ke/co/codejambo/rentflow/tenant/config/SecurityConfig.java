package ke.co.codejambo.rentflow.tenant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health/**", "/actuator/info").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter()))
            )
            .build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> keycloakJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<GrantedAuthority> authorities = new HashSet<>();

            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> scopeAuthorities = defaultConverter.convert(jwt);
            if (scopeAuthorities != null) authorities.addAll(scopeAuthorities);

            String scopeClaim = jwt.getClaimAsString("scope");
            if (scopeClaim != null && !scopeClaim.isBlank()) {
                for (String s : scopeClaim.split("\\s+")) {
                    if (!s.isBlank()) authorities.add(new SimpleGrantedAuthority("SCOPE_" + s));
                }
            }

            Object realmAccess = jwt.getClaim("realm_access");
            if (realmAccess instanceof Map<?, ?> realmAccessMap) {
                Object roles = realmAccessMap.get("roles");
                if (roles instanceof List<?> roleList) {
                    for (Object r : roleList) {
                        if (r != null) authorities.add(new SimpleGrantedAuthority("ROLE_" + r));
                    }
                }
            }

            Object resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess instanceof Map<?, ?> resourceMap) {
                for (Object clientEntry : resourceMap.values()) {
                    if (clientEntry instanceof Map<?, ?> clientMap) {
                        Object roles = clientMap.get("roles");
                        if (roles instanceof List<?> roleList) {
                            for (Object r : roleList) {
                                if (r != null) authorities.add(new SimpleGrantedAuthority("ROLE_" + r));
                            }
                        }
                    }
                }
            }

            return new ArrayList<>(authorities);
        });
        return converter;
    }
}
