package br.com.shopping_list.dtos

import br.com.shopping_list.entities.Sharing
import java.util.*

data class SharingDTO (
    val id: UUID,
    val listId: UUID,
    val userId: UUID
) {
    fun toEntity(): Sharing {
        return Sharing(
            id = id,
            listId = listId,
            userId = userId
        )
    }
}