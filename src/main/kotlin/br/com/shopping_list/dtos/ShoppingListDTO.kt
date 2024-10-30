package br.com.shopping_list.dtos

import br.com.shopping_list.entities.ShoppingList
import java.time.LocalDateTime
import java.util.*

data class ShoppingListDTO (
    val id: UUID?,
    val name: String,
    val description: String,
    val creationTime: LocalDateTime?,
    val creatorId: UUID
) {
    fun toEntity(): ShoppingList {
        return ShoppingList(
            id = id ?: UUID.randomUUID(),
            name = name,
            description = description,
            creationTime = creationTime ?: LocalDateTime.now(),
            creatorId = creatorId
        )
    }
}