import br.com.shopping_list.ShoppingListApplication
import br.com.shopping_list.configuration.UserNotFoundException
import br.com.shopping_list.dtos.UserDTO
import br.com.shopping_list.services.UserService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [ShoppingListApplication::class])
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    private fun basicAuthHeader(username: String, password: String): String {
        val auth = "$username:$password"
        val encodedAuth = Base64.getEncoder().encodeToString(auth.toByteArray())
        return "Basic $encodedAuth"
    }

    val username = "cUser"
    val password = "customPassword"

    @Test
    fun `test create user`() {
        val userDTO = UserDTO(
            id = UUID.randomUUID(),
            name = "John Doe",
            email = "john@example.com",
            password = "password123"
        )

        `when`(userService.createUser(any(UserDTO::class.java))).thenReturn(userDTO)

        val requestContent = """
            {
                "name": "John Doe",
                "email": "john@example.com",
                "password": "password123"
            }
        """

        mockMvc.perform(
            post("/api/users")
                .header("Authorization", basicAuthHeader(username, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("John Doe"))
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
            .header("Authorization", basicAuthHeader(username, password))
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
            .header("Authorization", basicAuthHeader(username, password))
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
            .header("Authorization", basicAuthHeader(username, password))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("John Doe"))
            .andExpect(jsonPath("$[1].name").value("Jane Doe"))
    }
}
