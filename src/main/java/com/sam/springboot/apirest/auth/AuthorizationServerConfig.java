package com.sam.springboot.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private InfoAdicionalToken infoAdicionalToken;


    //PARA PODER INGRESAR LAS CREDENCIALES Y SE PERMITA A TODOS LOS USUARIOS LA POSIBILIDAD DE LOGEARSE Y OBTENER UN TOKEN
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") //este es el endpoint oauth token (/oauth/token) sirve para logearse y obtener un token
                .checkTokenAccess("isAuthenticated()"); //se valida el token  que solo ingresen autenticados a los end points protegidos
    }

    //REGISTRAMOS LA APLICACIÓN QUE USARÁ MI BACKEND
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("angularapp").
                secret(passwordEncoder.encode("12345")).
                scopes("read","write").
                authorizedGrantTypes("password","refresh_token").
                accessTokenValiditySeconds(3600). //LO QUE DURARÁ EL TOKEN
                refreshTokenValiditySeconds(3600);
    }

    //SE ENCARGARÁ DE LA AUTENTICACIÓN DEL USUARIO | GENERA EL TOKEN Y SE LO ENTREGA AL USUARIO
    //TAMBIÉN VALIDARÁ EL TOKEN
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accesTokenConverter()));

        endpoints.authenticationManager(authenticationManager) //USARÁ EL authenticationManager PARA VERIFICAR LOS DATOS DEL USUARIO
                .accessTokenConverter(accesTokenConverter()) //DECODIFICA Y CODIFICA LOS DATOS DEL TOKEN | CREA EL JWT | VALIDA EL JWT
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Bean
    public JwtAccessTokenConverter accesTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(JwtConfig.LLAVE_SECRETA);
        return jwtAccessTokenConverter;
    }
}
