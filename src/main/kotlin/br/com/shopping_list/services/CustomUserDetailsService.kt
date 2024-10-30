package br.com.shopping_list.services

import br.com.shopping_list.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByName(username)
            ?: throw UsernameNotFoundException("User not found with name: $username")

        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

        return org.springframework.security.core.userdetails.User(
            user.name, user.password, listOf()
        )
    }
}
