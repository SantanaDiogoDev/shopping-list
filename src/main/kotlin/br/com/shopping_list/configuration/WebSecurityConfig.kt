package br.com.shopping_list.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer

@Configuration
class WebSecurityConfig {

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web -> web.ignoring().requestMatchers("/api/auth/login") }
    }
}
