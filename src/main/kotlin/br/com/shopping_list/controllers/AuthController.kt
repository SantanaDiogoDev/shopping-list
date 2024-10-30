package br.com.shopping_list.controllers

import br.com.shopping_list.configuration.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun authenticateUser(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            logger.info("Tentando autenticar usuário: ${request.name}")
            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.name, request.password)
            )
            logger.debug("Usuário autenticado com sucesso: ${authentication.name}")
            SecurityContextHolder.getContext().authentication = authentication
            val jwt = jwtUtil.createToken(request.name)

            ResponseEntity.ok(JwtResponse(jwt))

        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor")
        }
    }

    @GetMapping("/validateToken")
    fun validateToken(@RequestParam token: String): ResponseEntity<TokenValidationResponse> {
        val isValid = jwtUtil.validateToken(token)
        val remainingTime = if (isValid) jwtUtil.getRemainingTime(token) else "00:00:00"


        return ResponseEntity.ok(TokenValidationResponse(isValid, remainingTime))
    }
}

data class LoginRequest(val name: String, val password: String)
data class JwtResponse(val token: String)
data class TokenValidationResponse(val isValid: Boolean, val remainingTimeMillis: String)