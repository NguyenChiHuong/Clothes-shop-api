package com.example.shopweb.config;

import com.example.shopweb.constant.Constant;
import com.example.shopweb.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    //Cấp quyền cho các user

    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests ->{
                    //Cấp quyền
                   requests.requestMatchers(
                            String.format("%s/user/register",apiPrefix),
                            String.format("%s/user/login",apiPrefix)
                           ).permitAll()

                           //Role
                           .requestMatchers(HttpMethod.GET,String.format("%s/role**",apiPrefix)).permitAll()

                           //Phân quyền Order
                           .requestMatchers(HttpMethod.POST,String.format("%s/order/**",apiPrefix)).hasRole(Constant.USER)
                           .requestMatchers(HttpMethod.GET,String.format("%s/order/**",apiPrefix)).hasAnyRole(Constant.USER,Constant.ADMIN)
                           .requestMatchers(HttpMethod.PUT,String.format("%s/order/**",apiPrefix)).hasRole(Constant.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/order/**",apiPrefix)).hasRole(Constant.ADMIN)

                           //Phân quyền Category
                           .requestMatchers(HttpMethod.POST,String.format("%s/category/**",apiPrefix)).hasRole(Constant.ADMIN)
                           .requestMatchers(HttpMethod.GET,String.format("%s/category**",apiPrefix)).permitAll()
                           .requestMatchers(HttpMethod.PUT,String.format("%s/category/**",apiPrefix)).hasRole(Constant.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/category/**",apiPrefix)).hasRole(Constant.ADMIN)

                           //Phân quyền Order detail
                           .requestMatchers(HttpMethod.POST,String.format("%s/order_detail/**",apiPrefix)).hasRole(Constant.USER)
                           .requestMatchers(HttpMethod.GET,String.format("%s/order_detail/**",apiPrefix)).hasAnyRole(Constant.USER,Constant.ADMIN)
                           .requestMatchers(HttpMethod.PUT,String.format("%s/order_detail/**",apiPrefix)).hasAnyRole(Constant.ADMIN,Constant.USER)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/order_detail/**",apiPrefix)).hasAnyRole(Constant.USER,Constant.ADMIN)

                           //Phân quyền Product
                           .requestMatchers(HttpMethod.GET,String.format("%s/product/images/*",apiPrefix)).permitAll()
                           .requestMatchers(HttpMethod.POST,String.format("%s/product/**",apiPrefix)).hasRole(Constant.ADMIN)
                           .requestMatchers(HttpMethod.GET,String.format("%s/product**",apiPrefix)).permitAll()
                           .requestMatchers(HttpMethod.PUT,String.format("%s/product/**",apiPrefix)).hasRole(Constant.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/product/**",apiPrefix)).hasRole(Constant.ADMIN)
                           .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);

        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization","content-type"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}
