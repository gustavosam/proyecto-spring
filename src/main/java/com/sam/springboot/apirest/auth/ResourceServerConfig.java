package com.sam.springboot.apirest.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers(HttpMethod.GET,"/api/clientes").permitAll().
                antMatchers("/api/uploads/img/**").permitAll().
                antMatchers("/img/**").permitAll().
                /*antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasRole("ADMIN").
                antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN").
                */
                anyRequest().authenticated()
                .and()
                .cors().configurationSource(corsConfigurationSource()); //ES BASICAMENTE PARA QUE UN DOMINIO EXTERNO PUEDA ACCEDER A LOS RECURSOS PROTEGIDOS QUE TENGO
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "*")); //EL * LE INDICAMOS DE MANERA GENERAL QUE EL DOMINIO PUEDE SER CUALQUIERA
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("http://localhost:4200", "*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type","Authorization"));

        //SE CONFIGURA EL CORS PARA TODAS NUESTRAS RUTAS DEL BACKEND
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    //INDICARÁ QUE TIENE UNA PRIORIDAD ALTA
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }
}
