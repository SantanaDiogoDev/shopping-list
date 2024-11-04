package br.com.shopping_list.services

import br.com.shopping_list.dtos.ShoppingListDTO
import br.com.shopping_list.entities.ShoppingList
import br.com.shopping_list.repositories.SharingRepository
import br.com.shopping_list.repositories.ShoppingListRepository
import br.com.shopping_list.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingListServiceTest {

    private val shoppingListRepository = mock(ShoppingListRepository::class.java)
    private val userRepository = mock(UserRepository::class.java)
    private val sharingRepository = mock(SharingRepository::class.java)

    private val shoppingListService = ShoppingListService(shoppingListRepository, userRepository, sharingRepository)

    @Test
    fun `test create shopping list successfully`() {
        val creatorId = UUID.randomUUID()
        val shoppingListDTO = ShoppingListDTO(
            id = null,
            name = "Grocery List",
            description = "Weekly groceries",
            creationTime = null,
            creatorId = creatorId
        )

        `when`(userRepository.existsById(creatorId)).thenReturn(true)

        val savedList = ShoppingList(
            id = UUID.randomUUID(),
            name = shoppingListDTO.name,
            description = shoppingListDTO.description,
            creationTime = LocalDateTime.now(),
            creatorId = creatorId
        )

        `when`(shoppingListRepository.save(any(ShoppingList::class.java))).thenReturn(savedList)

        val result = shoppingListService.createList(shoppingListDTO)

        assertNotNull(result.id)
        assertEquals(shoppingListDTO.name, result.name)
    }

    @Test
    fun `test create shopping list throws exception when creator not found`() {
        val shoppingListDTO = ShoppingListDTO(
            id = null,
            name = "Grocery List",
            description = "Weekly groceries",
            creationTime = null,
            creatorId = UUID.randomUUID()
        )

        `when`(userRepository.existsById(shoppingListDTO.creatorId)).thenReturn(false)

        assertThrows<IllegalArgumentException> {
            shoppingListService.createList(shoppingListDTO)
        }
    }

    @Test
    fun `test get list by id successfully`() {
        val listId = UUID.randomUUID()
        val shoppingList = ShoppingList(
            id = listId,
            name = "Grocery List",
            description = "Weekly groceries",
            creationTime = LocalDateTime.now(),
            creatorId = UUID.randomUUID()
        )

        `when`(shoppingListRepository.findById(listId)).thenReturn(Optional.of(shoppingList))

        val result = shoppingListService.getListById(listId)

        assertEquals(shoppingList.id, result?.id)
        assertEquals(shoppingList.name, result?.name)
    }

    @Test
    fun `test get list by id returns null when not found`() {
        val listId = UUID.randomUUID()

        `when`(shoppingListRepository.findById(listId)).thenReturn(Optional.empty())

        val result = shoppingListService.getListById(listId)

        assertNull(result)
    }
}
