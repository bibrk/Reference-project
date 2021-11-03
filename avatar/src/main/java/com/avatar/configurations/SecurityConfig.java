package com.avatar.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
//@EnableAutoConfiguration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	JwtFilter jwtFilter;
	
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	
	}
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(encoder());
	    return authProvider;
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
        .csrf().disable()
        
          .authorizeRequests()
          .antMatchers("/swagger-ui/**").permitAll()
          .antMatchers("/v3/api-docs",
                  "/configuration/ui",
                  "/swagger-resources/**",
                  "/configuration/security",
                  "/swagger-ui.html",
                  "/webjars/**").permitAll()
          .antMatchers("/logout").permitAll()
          .antMatchers("/login").permitAll()
          .antMatchers("/builder/**").permitAll()
          .antMatchers("/perform_login").permitAll()
          .antMatchers("/avatar").permitAll()
          .antMatchers("/avatar/registration").permitAll()
          .antMatchers("/avatar/registrationConfirm").permitAll()
          .antMatchers("/avatar/user/**").hasAnyRole("ADMIN","USER")
          .antMatchers("/avatar/main/**").hasAnyRole("ADMIN","USER")  
          .antMatchers("/avatar/admin/**").permitAll()//.hasRole("ADMIN")
          .antMatchers("/h2-console", "/h2-console/", "/h2-console/*", "/h2-console/**").permitAll() // H2 console!!!!!!
          .antMatchers("/file/**").permitAll() // TODO just for tests!!!!
          .anyRequest().authenticated()
          .and()
          //.addFilter(new JWTAuthenticationFilter(authenticationManager()))
          //.addFilter(new JWTAuthorizationFilter(authenticationManager()))
          // this disables session creation on Spring Security
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          //.and().formLogin()
          //.loginPage("/login")
          //.loginProcessingUrl("/login")
          //.successHandler(authenticationSuccessHandler())
          //.defaultSuccessUrl("/", true)
          //.failureUrl("/login.html")
          //.failureHandler(authenticationFailureHandler())
          .and()
          .logout()
          .logoutUrl("/logout")
          //.deleteCookies("JSESSIONID")
          .deleteCookies()
          .logoutSuccessHandler(logoutSuccessHandler());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
          
    }
    
    
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AllAuthenticationSuccesHandler();
    }
     
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new AllLogoutSuccessHandler();
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AllAuthenticationFailureHandler();
    }

    @Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
    

}
