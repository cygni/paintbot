package se.cygni.paintbot.socketserver.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Configuration
@RequiredArgsConstructor
public class InMemoryUserDatabase {

    @Primary
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(BCryptPasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("squiddy")
                        .password(passwordEncoder.encode("newPASSLOL88"))
                        .roles("ADMIN").build()
        );
        manager.createUser(
                User.withUsername("cygni")
                        .password(passwordEncoder.encode("AdamantLowlyCurlew"))
                        .roles("ADMIN").build()
        );
        manager.createUser(
                User.withUsername("bob")
                        .password(passwordEncoder.encode("UnusualAgreeableJaguar"))
                        .roles("USER").build()
        );

        return manager;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
