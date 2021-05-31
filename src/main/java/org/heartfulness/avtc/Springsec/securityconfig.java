package org.heartfulness.avtc.Springsec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import javax.sql.DataSource;

/*
@Configuration
@EnableWebSecurity
@Order(value = 200 )
public class securityconfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Qualifier("MyUserDetailsSrvices")
    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository=new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        // authentication manager (see below)
        //make it database oriented or whatever
        auth.userDetailsService(userDetailsService);

      /* auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN");

    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // http builder configurations for authorize requests and form login (see below)
      /*  http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("USER","ADMIN")
                .antMatchers("/").permitAll()
                .and().formLogin();

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/*.js").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/registration/**").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/login*").permitAll()
                .antMatchers("/confirm-account").permitAll()
                .antMatchers("/change-password").permitAll()
                .antMatchers("/custom_login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/perform_login").permitAll()
                .antMatchers("/forgot").permitAll()
                .antMatchers("/changepwd").permitAll()
                .antMatchers("/phoneverify").permitAll()
                .antMatchers("/success").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/phonereg").permitAll()
                .antMatchers("/dashboard/**").authenticated()
                .antMatchers("/").authenticated()
                .and()
                .formLogin()
                .loginPage("/custom_login")
                .defaultSuccessUrl("/dashboard",true)
                .loginProcessingUrl("/perform_login")
                .failureUrl("/custom_login?error=true")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/custom_login")
                .and().oauth2Login()
                .loginPage("/custom_login")
                .defaultSuccessUrl("/dashboard",true)
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()

                //.successHandler(oAuth2LoginSuccessHandler)
                .and()
                .logout().permitAll()

                .permitAll();
                }
 */
