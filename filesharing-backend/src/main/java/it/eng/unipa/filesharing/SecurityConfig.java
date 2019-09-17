package it.eng.unipa.filesharing;

import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

//@KeycloakConfiguration
@Configuration
@EnableWebSecurity
@SuppressWarnings("rawtypes")
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     */
	
	public SecurityConfig() {
		LOGGER.info("------------start security--------------");
	}
	
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

//    @Autowired
//    @Bean
//    @Conditional(value = FalseCondition.class)
//    protected HttpSessionManager httpSessionManager() {
//        return new HttpSessionManager();
//    }
    
    
//    public static class FalseCondition implements Condition{ @Override public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {System.out.println("ok");	return false;} 	}
    
    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    	/*
    		You must provide a session authentication strategy bean which should be of type RegisterSessionAuthenticationStrategy for public or confidential applications 
    		and NullAuthenticatedSessionStrategy for bearer-only applications. 
    	 */
    	//return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        return new NullAuthenticatedSessionStrategy();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
    	LOGGER.info("----------------------------------");
    	
    	LOGGER.info("----------------------------------");
    	LOGGER.info("----------------------------------");
    	LOGGER.info("----------------------------------");
        super.configure(http);
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**")
                .permitAll()
                .and()
                .authorizeRequests()
              .anyRequest().authenticated();
            //   .anyRequest().permitAll();
    }
}
