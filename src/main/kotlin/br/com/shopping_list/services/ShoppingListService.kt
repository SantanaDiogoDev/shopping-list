package br.com.shopping_list.services

import br.com.shopping_list.dtos.ShoppingListDTO
import br.com.shopping_list.entities.Sharing
import br.com.shopping_list.entities.ShoppingList
import br.com.shopping_list.repositories.SharingRepository
import br.com.shopping_list.repositories.ShoppingListRepository
import br.com.shopping_list.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ShoppingListService @Autowired constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val userRepository: UserRepository,
    private val sharingRepository: SharingRepository,
){
    fun createList(shoppingListDTO: ShoppingListDTO): ShoppingListDTO {
        if (!userRepository.existsById(shoppingListDTO.creatorId)) {
            throw IllegalArgumentException("Creator with id ${shoppingListDTO.creatorId} not found")
        }

        val newList = ShoppingList (
            name = shoppingListDTO.name,
            description = shoppingListDTO.description,
            creatorId = shoppingListDTO.creatorId,
            creationTime = LocalDateTime.now()
        )

        val sharing = Sharing (
            listId = newList.id,
            userId = newList.creatorId
        )
        sharingRepository.save(sharing)

        return ShoppingListDTO(
            id = shoppingListRepository.save(newList).id,
            name = newList.name,
            description = newList.description!!,
            creationTime = newList.creationTime,
            creatorId = newList.creatorId
        )
    }

    fun getListById(id: UUID): ShoppingListDTO? {
        return shoppingListRepository.findById(id).map { list ->
            ShoppingListDTO(
                id = list.id,
                name = list.name,
                description = list.description!!,
                creationTime = list.creationTime,
                creatorId = list.creatorId
            )
        }.orElse(null)
    }

    fun getAllLists(): List<ShoppingListDTO> {
        val lists =  shoppingListRepository.findAll();

        return lists.map { list ->
            ShoppingListDTO(
                id = list.id,
                name = list.name,
                description = list.description!!,
                creationTime = list.creationTime,
                creatorId = list.creatorId
            )
        }
    }

    fun shareListWithUser(listId: UUID, userId: UUID) {
        if (!shoppingListRepository.existsById(listId)) {
            throw IllegalArgumentException("List with id $listId not found")
        }

        if (!userRepository.existsById(userId)) {
            throw IllegalArgumentException("User with id $userId not found")
        }

        val sharing = Sharing(
            listId = listId,
            userId = userId
        )
        sharingRepository.save(sharing)
    }
}