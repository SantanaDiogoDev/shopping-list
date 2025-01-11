package br.com.shopping_list.repositories

import br.com.shopping_list.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID>{
    fun findByName(name: String): User?
    fun findByEmail(email: String): User?
}