import br.com.shopping_list.dtos.ItemDTO
import br.com.shopping_list.entities.Item
import br.com.shopping_list.repositories.ItemRepository
import br.com.shopping_list.repositories.ShoppingListRepository
import br.com.shopping_list.services.ItemService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class ItemServiceTest {

    private val itemRepository = mock(ItemRepository::class.java)
    private val shoppingListRepository = mock(ShoppingListRepository::class.java)

    private val itemService = ItemService(itemRepository, shoppingListRepository)

    @Test
    fun `test create item successfully`() {
        val itemDTO = ItemDTO(
            id = null,
            name = "Milk",
            quantity = 2,
            status = false,
            listId = UUID.randomUUID(),
            creationTime = null
        )

        `when`(shoppingListRepository.existsById(itemDTO.listId)).thenReturn(true)
        val savedItem = Item(
            id = UUID.randomUUID(),
            name = itemDTO.name,
            quantity = itemDTO.quantity,
            status = itemDTO.status,
            listId = itemDTO.listId,
            creationTime = LocalDateTime.now()
        )

        `when`(itemRepository.save(any(Item::class.java))).thenReturn(savedItem)

        val result = itemService.createItem(itemDTO)

        assertNotNull(result.id)
        assertEquals(itemDTO.name, result.name)
        assertEquals(itemDTO.quantity, result.quantity)
    }

    @Test
    fun `test create item throws exception when list not found`() {
        val itemDTO = ItemDTO(
            id = null,
            name = "Bread",
            quantity = 3,
            status = false,
            listId = UUID.randomUUID(),
            creationTime = null
        )

        `when`(shoppingListRepository.existsById(itemDTO.listId)).thenReturn(false)

        assertThrows<IllegalArgumentException> {
            itemService.createItem(itemDTO)
        }
    }
}
