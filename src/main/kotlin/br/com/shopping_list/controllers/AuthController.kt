import br.com.shopping_list.configuration.JwtTokenProvider
import br.com.shopping_list.dto.LoginRequest
import br.com.shopping_list.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val jwtTokenProvider: JwtTokenProvider,
                     private val userService: UserService,
                     private val passwordEncoder: BCryptPasswordEncoder) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Map<String, String>> {
        val user = userService.getUserByName(request.name)
        return if (user != null && passwordEncoder.matches(request.password, user.password)) {
            val token = jwtTokenProvider.createToken(request.name)
            ResponseEntity.ok(mapOf("token" to token))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))
        }
    }

    @GetMapping("/validate")
    fun validateToken(@RequestParam token: String): ResponseEntity<Map<String, Any>> {
        val isValid = jwtTokenProvider.validateToken(token)
        return if (isValid) {
            val username = jwtTokenProvider.getUsername(token)
            ResponseEntity.ok(mapOf("valid" to true, "username" to username))
        } else {
            ResponseEntity.badRequest().body(mapOf("valid" to false))
        }
    }
}