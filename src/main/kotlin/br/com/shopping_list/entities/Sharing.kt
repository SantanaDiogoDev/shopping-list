package br.com.shopping_list.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "shares")
data class Sharing (
    @Id
    @GeneratedValue(generator = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val listId: UUID,

    @Column(nullable = false)
    val userId: UUID
)