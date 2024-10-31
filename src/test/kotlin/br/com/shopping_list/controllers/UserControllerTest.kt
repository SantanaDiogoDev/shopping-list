package br.com.shopping_list.controllers

import br.com.shopping_list.configuration.JwtUtil
import br.com.shopping_list.configuration.UserNotFoundException
import br.com.shopping_list.dtos.UserDTO
import br.com.shopping_list.services.UserService
import org.junit.jupiter.api.Test
import org.mockito.*
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    val username = "Arthur"

    private fun jwtAuthHeader(username: String): String {
        val token = jwtUtil.createToken(username)
        return "Bearer $token"
    }

    @Test
    fun `create new user and return the user data`() {
        val userDTO = UserDTO(
            id = UUID.randomUUID(),
            name = "Arthur",
            email = "arthur@example.com",
            password = null
        )

        given(userService.createUser(org.mockito.kotlin.any())).willReturn(userDTO)

        val requestContent = """
            {
                "name": "Arthur",
                "email": "arthur@example.com",
                "password": "password123"
            }
        """

        mockMvc.perform(
            post("/api/users")
                .header("Authorization", jwtAuthHeader(username))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Arthur"))
            .andExpect(jsonPath("$.email").value("arthur@example.com"))
            .andExpect(jsonPath("$.password").doesNotExist())
    }

    @Test
    fun `test get user by id - success`() {
        val userId = UUID.randomUUID()
        val userDTO = UserDTO(
            id = userId,
            name = "John Doe",
            email = "john@example.com",
            password = "encryptedPassword"
        )

        `when`(userService.getUserById(userId.toString())).thenReturn(userDTO)

        mockMvc.perform(get("/api/users/$userId")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.name").value("John Doe"))
    }

    @Test
    fun `test get user by id - not found`() {
        val userId = UUID.randomUUID()

        `when`(userService.getUserById(userId.toString())).thenThrow(UserNotFoundException("User not found!"))

        mockMvc.perform(get("/api/users/$userId")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `test get all users`() {
        val user1 = UserDTO(
            id = UUID.randomUUID(),
            name = "John Doe",
            email = "john@example.com",
            password = "encryptedPassword"
        )
        val user2 = UserDTO(
            id = UUID.randomUUID(),
            name = "Jane Doe",
            email = "jane@example.com",
            password = "encryptedPassword"
        )

        `when`(userService.getAllUsers()).thenReturn(listOf(user1, user2))

        mockMvc.perform(get("/api/users")
            .header("Authorization", jwtAuthHeader(username))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("John Doe"))
            .andExpect(jsonPath("$[1].name").value("Jane Doe"))
    }
}

inline fun <reified T> anyNonNull(): T = Mockito.any(T::class.java) ?: createInstance()

inline fun <reified T> createInstance(): T = T::class.java.getDeclaredConstructor().newInstance()

inline fun <reified T : Any> captureNonNull(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)
