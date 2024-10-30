package br.com.shopping_list.dtos

import br.com.shopping_list.entities.Item
import java.time.LocalDateTime
import java.util.*

data class ItemDTO (
    val id: UUID?,
    val name: String,
    val quantity: Int,
    val status: Boolean = false,
    val listId: UUID,
    val creationTime: LocalDateTime?
) {
    fun toEntity(): Item {
        return Item(
            id = id ?: UUID.randomUUID(),
            name = name,
            quantity = quantity,
            status = status,
            listId = listId,
            creationTime = creationTime ?: LocalDateTime.now()
        )
    }
}