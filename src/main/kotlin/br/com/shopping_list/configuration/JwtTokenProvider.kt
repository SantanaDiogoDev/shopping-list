package br.com.shopping_list.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {

    private val secretKey = "suaChaveSecretaSegura" // Substitua por uma chave segura
    private val validityInMilliseconds: Long = 3600000 // 1h de validade

    fun createToken(username: String): String {
        val claims = Jwts.claims().setSubject(username)
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims: Claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
            return !claims.expiration.before(Date())
        } catch (e: Exception) {
            return false
        }
    }
}
