package br.com.shopping_list.configuration

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@Component
class JwtUtil {

    private val secretKey = generateSecretKey()
    private val jwtExpirationMs : Long = 3600000 //1h validation

    fun createToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = getClaimsFromToken(token)
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private final fun generateSecretKey(): SecretKey {
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance("HmacSHA256")
        keyGenerator.init(256)

        return keyGenerator.generateKey()
    }
}
