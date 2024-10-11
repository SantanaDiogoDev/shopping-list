package br.com.shopping_list.services

import br.com.shopping_list.dtos.SharingDTO
import br.com.shopping_list.repositories.SharingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
@Service
class SharingService @Autowired constructor(
    private val sharingRepository: SharingRepository
) {

    fun findById(id: UUID): SharingDTO {
        val sharing = sharingRepository.findById(id).orElseThrow {
            NoSuchElementException("Sharing with id $id not found")
        }
        return SharingDTO(
            id = sharing.id,
            listId = sharing.listId,
            userId = sharing.userId
        )
    }

    fun findAll(): List<SharingDTO> {
        return sharingRepository.findAll().map { sharing ->
            SharingDTO(
                id = sharing.id,
                listId = sharing.listId,
                userId = sharing.userId
            )
        }
    }
}