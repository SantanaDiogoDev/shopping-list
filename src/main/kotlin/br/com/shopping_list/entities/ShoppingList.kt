package br.com.shopping_list.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "lists")
data class ShoppingList (
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val description: String? = null,

    @Column(nullable = false)
    val creationTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val creatorId: UUID
)