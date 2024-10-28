package br.com.shopping_list.configuration

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {

    private val secretKey = generateSecretKey()
    private val validityInMilliseconds: Long = 3600000 //1h validation

    fun createToken(username: String): String {
        val claims = Jwts.claims().setSubject(username)
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    private final fun generateSecretKey(): SecretKey {
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance("HmacSHA256")
        keyGenerator.init(256)

        return keyGenerator.generateKey()
    }
}
