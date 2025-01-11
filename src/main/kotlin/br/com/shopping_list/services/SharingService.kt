package br.com.shopping_list.services

import br.com.shopping_list.dtos.SharingDTO
import br.com.shopping_list.entities.Sharing
import br.com.shopping_list.repositories.SharingRepository
import br.com.shopping_list.repositories.ShoppingListRepository
import br.com.shopping_list.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SharingService @Autowired constructor(
    private val sharingRepository: SharingRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val userRepository: UserRepository
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

    fun createShare(sharingDTO: SharingDTO): SharingDTO {
        if(!shoppingListRepository.existsById(sharingDTO.listId)){
            throw IllegalArgumentException("List with id ${sharingDTO.listId} not found")
        }
        if(!userRepository.existsById(sharingDTO.userId)){
            throw IllegalArgumentException("User with id ${sharingDTO.listId} not found")
        }

        val newShare = Sharing (
            listId = sharingDTO.listId,
            userId = sharingDTO.userId
        )

        return SharingDTO(
            id = sharingRepository.save(newShare).id,
            listId = newShare.listId,
            userId = newShare.userId
        )
    }
}