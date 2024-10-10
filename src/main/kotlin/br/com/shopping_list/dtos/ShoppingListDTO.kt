package br.com.shopping_list.dtos

import java.time.LocalDateTime
import java.util.*

data class ShoppingListDTO (
    val id: UUID?,
    val name: String,
    val description: String,
    val creationTime: LocalDateTime?,
    val creatorId: UUID
)