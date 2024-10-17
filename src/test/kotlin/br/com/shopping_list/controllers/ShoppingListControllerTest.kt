import br.com.shopping_list.controllers.ShoppingListController
import br.com.shopping_list.dtos.ShoppingListDTO
import br.com.shopping_list.services.ShoppingListService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(ShoppingListController::class)
class ShoppingListControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var shoppingListService: ShoppingListService

    @Test
    fun `test create shopping list`() {
        val shoppingListDTO = ShoppingListDTO(
            id = UUID.randomUUID(),
            name = "Grocery List",
            description = "Weekly groceries",
            creationTime = null,
            creatorId = UUID.randomUUID()
        )

        `when`(shoppingListService.createList(any(ShoppingListDTO::class.java))).thenReturn(shoppingListDTO)

        val requestContent = """
            {
                "name": "Grocery List",
                "description": "Weekly groceries",
                "creatorId": "${shoppingListDTO.creatorId}"
            }
        """

        mockMvc.perform(
            post("/api/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Grocery List"))
    }

    @Test
    fun `test get list by id - success`() {
        val listId = UUID.randomUUID()
        val shoppingListDTO = ShoppingListDTO(
            id = listId,
            name = "Grocery List",
            description = "Weekly groceries",
            creationTime = null,
            creatorId = UUID.randomUUID()
        )

        `when`(shoppingListService.getListById(listId)).thenReturn(shoppingListDTO)

        mockMvc.perform(get("/api/lists/$listId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(listId.toString()))
            .andExpect(jsonPath("$.name").value("Grocery List"))
    }

    @Test
    fun `test get list by id - not found`() {
        val listId = UUID.randomUUID()

        `when`(shoppingListService.getListById(listId)).thenReturn(null)

        mockMvc.perform(get("/api/lists/$listId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `test get all lists`() {
        val shoppingList1 = ShoppingListDTO(
            id = UUID.randomUUID(),
            name = "Grocery List 1",
            description = "Weekly groceries",
            creationTime = null,
            creatorId = UUID.randomUUID()
        )
        val shoppingList2 = ShoppingListDTO(
            id = UUID.randomUUID(),
            name = "Grocery List 2",
            description = "Monthly groceries",
            creationTime = null,
            creatorId = UUID.randomUUID()
        )

        `when`(shoppingListService.getAllLists()).thenReturn(listOf(shoppingList1, shoppingList2))

        mockMvc.perform(get("/api/lists"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Grocery List 1"))
            .andExpect(jsonPath("$[1].name").value("Grocery List 2"))
    }

    @Test
    fun `test share list with user - success`() {
        val listId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        doNothing().`when`(shoppingListService).shareListWithUser(listId, userId)

        mockMvc.perform(post("/api/lists/$listId/share/$userId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test share list with user - bad request`() {
        val listId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        doThrow(IllegalArgumentException("List with id $listId not found"))
            .`when`(shoppingListService).shareListWithUser(listId, userId)

        mockMvc.perform(post("/api/lists/$listId/share/$userId"))
            .andExpect(status().isBadRequest)
    }
}
