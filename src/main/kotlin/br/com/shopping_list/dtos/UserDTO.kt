package br.com.shopping_list.dtos

import java.util.*

data class UserDTO(
    val id: UUID?,
    val name: String,
    val email: String,
    val password: String?
)