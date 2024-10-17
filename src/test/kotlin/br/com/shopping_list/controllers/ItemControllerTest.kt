import br.com.shopping_list.dtos.ItemDTO
import br.com.shopping_list.services.ItemService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var itemService: ItemService

    @Test
    fun `test create item`() {
        val itemDTO = ItemDTO(
            id = UUID.randomUUID(),
            name = "Milk",
            quantity = 2,
            status = false,
            listId = UUID.randomUUID(),
            creationTime = null
        )

        `when`(itemService.createItem(any(ItemDTO::class.java))).thenReturn(itemDTO)

        val requestContent = """
            {
                "name": "Milk",
                "quantity": 2,
                "status": false,
                "listId": "${itemDTO.listId}"
            }
        """

        mockMvc.perform(
            post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Milk"))
            .andExpect(jsonPath("$.quantity").value(2))
    }

    @Test
    fun `test get item by id - success`() {
        val itemId = UUID.randomUUID()
        val itemDTO = ItemDTO(
            id = itemId,
            name = "Milk",
            quantity = 2,
            status = false,
            listId = UUID.randomUUID(),
            creationTime = null
        )

        `when`(itemService.getItemById(itemId)).thenReturn(itemDTO)

        mockMvc.perform(get("/api/items/$itemId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(itemId.toString()))
            .andExpect(jsonPath("$.name").value("Milk"))
    }

    @Test
    fun `test get item by id - not found`() {
        val itemId = UUID.randomUUID()

        `when`(itemService.getItemById(itemId)).thenReturn(null)

        mockMvc.perform(get("/api/items/$itemId"))
            .andExpect(status().isNotFound)
    }
}
