package br.com.shopping_list.controllers

import br.com.shopping_list.configuration.JwtUtil
import br.com.shopping_list.dtos.SharingDTO
import br.com.shopping_list.services.SharingService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class SharingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var sharingService: SharingService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    val username = "Arthur"

    private fun jwtAuthHeader(username: String): String {
        val token = jwtUtil.createToken(username)
        return "Bearer $token"
    }

    @Test
    fun `test get sharing by id - success`() {
        val sharingId = UUID.randomUUID()
        val sharingDTO = SharingDTO(
            id = sharingId,
            listId = UUID.randomUUID(),
            userId = UUID.randomUUID()
        )

        `when`(sharingService.findById(sharingId)).thenReturn(sharingDTO)

        mockMvc.perform(get("/api/shares/$sharingId")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(sharingId.toString()))
    }

    @Test
    fun `test get sharing by id - not found`() {
        val sharingId = UUID.randomUUID()

        `when`(sharingService.findById(sharingId)).thenThrow(NoSuchElementException())

        mockMvc.perform(get("/api/shares/$sharingId")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `test get all shares`() {
        val sharing1 = SharingDTO(
            id = UUID.randomUUID(),
            listId = UUID.randomUUID(),
            userId = UUID.randomUUID()
        )
        val sharing2 = SharingDTO(
            id = UUID.randomUUID(),
            listId = UUID.randomUUID(),
            userId = UUID.randomUUID()
        )

        `when`(sharingService.findAll()).thenReturn(listOf(sharing1, sharing2))

        mockMvc.perform(get("/api/shares")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(sharing1.id.toString()))
            .andExpect(jsonPath("$[1].id").value(sharing2.id.toString()))
    }
}
