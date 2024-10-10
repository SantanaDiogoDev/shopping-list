package br.com.shopping_list.services

import br.com.shopping_list.dtos.ItemDTO
import br.com.shopping_list.entities.Item
import br.com.shopping_list.repositories.ItemRepository
import br.com.shopping_list.repositories.ShoppingListRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class ItemService @Autowired constructor(
    private val itemRepository: ItemRepository,
    private val shoppingListRepository: ShoppingListRepository
){
    fun createItem(itemDTO: ItemDTO): ItemDTO {
        if(shoppingListRepository.existsById(itemDTO.listId)){
            throw IllegalArgumentException("List with id ${itemDTO.listId} not found")
        }

        val newItem = Item (
            name = itemDTO.name,
            quantity = itemDTO.quantity,
            status = itemDTO.status,
            listId = itemDTO.listId,
            creationTime = LocalDateTime.now()
        )

        return ItemDTO (
            id = itemRepository.save(newItem).id,
            name = newItem.name,
            quantity = newItem.quantity,
            status = newItem.status,
            listId = newItem.listId,
            creationTime = newItem.creationTime
        )

    }

    fun getItemById(id: UUID): ItemDTO? {
        return itemRepository.findById(id).map {item ->
            ItemDTO(
                id = item.id,
                name = item.name,
                quantity = item.quantity,
                status = item.status,
                listId = item.listId,
                creationTime = item.creationTime
            )

        }.orElse(null)
    }

    fun getAllItems(): List<ItemDTO> {
        val items = itemRepository.findAll()

        return items.map { item ->
            ItemDTO(
                id = item.id,
                name = item.name,
                quantity = item.quantity,
                status = item.status,
                listId = item.listId,
                creationTime = item.creationTime
            )
        }
    }
}