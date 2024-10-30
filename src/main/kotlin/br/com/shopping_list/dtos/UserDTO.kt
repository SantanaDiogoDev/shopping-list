package br.com.shopping_list.dtos

import br.com.shopping_list.entities.User
import java.util.*

data class UserDTO(
    val id: UUID?,
    val name: String,
    val email: String,
    val password: String?
) {
    companion object {
        fun from(user: User): UserDTO {
            return UserDTO(
                id = user.id,
                name = user.name,
                email = user.email,
                password = user.password
            )
        }
    }

    fun toEntity(): User {
        return User(
            id = id ?: UUID.randomUUID(),
            name = name,
            email = email,
            password = password ?: ""
        )
    }
}