package br.com.shopping_list.dtos

import java.time.LocalDateTime
import java.util.*

data class ItemDTO (
    val id: UUID,
    val name: String,
    val quantity: Int,
    val status: Boolean,
    val listId: UUID,
    val creationTime: LocalDateTime
)