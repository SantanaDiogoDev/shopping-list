package br.com.shopping_list.controllers

import br.com.shopping_list.configuration.JwtUtil
import br.com.shopping_list.dtos.ItemDTO
import br.com.shopping_list.services.ItemService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
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

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    val username = "Arthur"

    private fun jwtAuthHeader(username: String): String {
        val token = jwtUtil.createToken(username)
        return "Bearer $token"
    }

    @Test
    fun `test create item and return item data`() {
        val itemDTO = ItemDTO(
            id = UUID.randomUUID(),
            name = "Milk",
            quantity = 2,
            status = false,
            listId = UUID.randomUUID(),
            creationTime = null
        )

        given(itemService.createItem(org.mockito.kotlin.any())).willReturn(itemDTO)

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
                .header("Authorization", jwtAuthHeader(username))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Milk"))
            .andExpect(jsonPath("$.quantity").value(2))
            .andExpect(jsonPath("$.status").value(false))
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

        mockMvc.perform(get("/api/items/$itemId")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(itemId.toString()))
            .andExpect(jsonPath("$.name").value("Milk"))
    }

    @Test
    fun `test get item by id - not found`() {
        val itemId = UUID.randomUUID()

        `when`(itemService.getItemById(itemId)).thenReturn(null)

        mockMvc.perform(get("/api/items/$itemId")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `test get all items`() {
        val item1 = ItemDTO(
            id = UUID.randomUUID(),
            name = "Milk",
            quantity = 2,
            status = false,
            listId = UUID.randomUUID(),
            creationTime = null
        )

        val item2 = ItemDTO(
            id = UUID.randomUUID(),
            name = "Bread",
            quantity = 6,
            status = true,
            listId = UUID.randomUUID(),
            creationTime = null
        )

        `when`(itemService.getAllItems()).thenReturn(listOf(item1, item2))

        mockMvc.perform(get("/api/items")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Milk"))
            .andExpect(jsonPath("$[1].name").value("Bread"))
    }
}
