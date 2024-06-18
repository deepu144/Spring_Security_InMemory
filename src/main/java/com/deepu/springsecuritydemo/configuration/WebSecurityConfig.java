package com.deepu.springsecuritydemo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    DataSource dataSource;
//    .....................................................................................................
//    FOR CHANGING THE DEFAULT SPRING SECURITY THAT MEANS THE LOGIN PAGE AND LOGOUT PAGE
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        http.headers(headers-> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
//    ......................................................................................................
//    PASSWORD ENCODER USING BCryptPasswordEncoder() CLASS
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    .....................................................................................................
    @Bean
    UserDetailsService userDetailsService() {
        UserDetails user1 = User.withUsername("user1")
                .password("{noop}pass")
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password("{noop}pass")
                .roles("ADMIN")
                .build();
//        BELOW InMemoryUserDetailsManager() IS USED TO SECURE OUR WEBSITE THROUGH IN-MEMORY THAT MEANS NO DATABASES ARE USED TO SAVE THE USER OR ADMIN OR ANY OTHER ROLE
//        NOW WE CAN USE JDBCUserDetailManager() , IN THAT USER DETAILS STORE IN H2 DATABASE , NOTHING MUCH CHANGE AND CODE IS MODIFY , JUST RETURN A OBJECT OF JDBCUserDetailService CLASS.
//        FOR H2 DATABASE , WE WANT SCHEMA ALSO THE SCHEMA IS GIVEN BELOW OR ADDED IN /src/resources.


       /* create table users(username varchar_ignorecase(50) not null primary key,password varchar_ignorecase(500) not null,enabled boolean not null);
        create table authorities (username varchar_ignorecase(50) not null,authority varchar_ignorecase(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
        create unique index ix_auth_username on authorities (username,authority);*/


        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(admin);
        return userDetailsManager;
//      return new InMemoryUserDetailsManager(user1,admin);
    }
}
