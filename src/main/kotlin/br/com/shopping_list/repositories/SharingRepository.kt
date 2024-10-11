package br.com.shopping_list.repositories

import br.com.shopping_list.entities.Sharing
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SharingRepository : JpaRepository<Sharing, UUID> {
    fun findByListId(listId: UUID): List<Sharing>
    fun findByUserId(userId: UUID): List<Sharing>
}