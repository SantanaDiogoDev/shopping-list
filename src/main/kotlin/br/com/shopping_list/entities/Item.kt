package br.com.shopping_list.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "items")
data class Item (
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val quantity: Int,

    @Column(nullable = false)
    val status: Boolean = false, //false: Not bought

    @Column(nullable = false)
    val listId: UUID,

    @Column(nullable = false)
    val creationTime: LocalDateTime = LocalDateTime.now(),
)