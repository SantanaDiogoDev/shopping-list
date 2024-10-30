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

    fun getRemainingTime(token: String): String {
        val claims = getClaimsFromToken(token)
        val expiration = claims.expiration.time
        val now = System.currentTimeMillis()
        val remainingTimeMillis = if (expiration > now) expiration - now else 0L

        // Converte para formato "HH:mm:ss"
        val hours = (remainingTimeMillis / 3600000) % 24
        val minutes = (remainingTimeMillis / 60000) % 60
        val seconds = (remainingTimeMillis / 1000) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private final fun generateSecretKey(): SecretKey {
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance("HmacSHA256")
        keyGenerator.init(256)

        return keyGenerator.generateKey()
    }
}
