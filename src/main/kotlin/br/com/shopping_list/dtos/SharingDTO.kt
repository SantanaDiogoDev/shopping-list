package br.com.shopping_list.dtos

import java.util.*

data class SharingDTO (
    val id: UUID,
    val listId: UUID,
    val userId: UUID
)