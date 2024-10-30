package br.com.shopping_list.configuration

import br.com.shopping_list.services.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService
) {

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtUtil, userDetailsService)
    }
}
