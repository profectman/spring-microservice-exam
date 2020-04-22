package com.jeeho.auth.config;

import com.jeeho.auth.security.CustomTokenConverter;
import com.jeeho.common.security.core.ClientDetailsServiceImpl;
import com.jeeho.common.security.exception.CustomOauthException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;


@Configuration
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    private AuthenticationConfiguration authenticationConfiguration;

    /**
     * redis连接工厂
     */
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 数据源
     */
    private final DataSource dataSource;

    /**
     * key配置信息
     */
    private final KeyProperties keyProperties;

    @Autowired
    public CustomAuthorizationServerConfigurer(RedisConnectionFactory redisConnectionFactory,
                                               DataSource dataSource,
                                               KeyProperties keyProperties) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.dataSource = dataSource;
        this.keyProperties = keyProperties;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .passwordEncoder(new BCryptPasswordEncoder())
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置客户端信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST)
                .authenticationManager(this.authenticationConfiguration.getAuthenticationManager())
                //将token存储到redis中
                .tokenStore(tokenStore())
                //token增强
                .tokenEnhancer(jwtAccessTokenConverter())
                //异常转换
                .exceptionTranslator(webResponseExceptionTranslator());
    }

    @Bean
    public ClientDetailsService clientDetailsService(){
        return new ClientDetailsServiceImpl(dataSource);
    }

    /**
     * oauth中token存储
     * @return
     */
    @Bean
    public TokenStore tokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * oauth中token转换增强
     * @return
     */
    @Bean
    protected JwtAccessTokenConverter jwtAccessTokenConverter(){
        return new CustomTokenConverter(keyPair(), Collections.singletonMap("kid", "bael-key-id"));
    }

    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getPassword().toCharArray());
        return keyStoreKeyFactory.getKeyPair(keyProperties.getKeyStore().getAlias());
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair().getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("bael-key-id");
        return new JWKSet(builder.build());
    }

    @Bean
    @Lazy
    public WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                if (e instanceof OAuth2Exception) {
                    OAuth2Exception exception = (OAuth2Exception) e;
                    // 转换返回结果
                    return ResponseEntity.status(exception.getHttpErrorCode()).body(new CustomOauthException(e.getMessage()));
                } else {
                    throw e;
                }
            }
        };
    }

    @Autowired
    public void setAuthenticationConfiguration(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }
}
