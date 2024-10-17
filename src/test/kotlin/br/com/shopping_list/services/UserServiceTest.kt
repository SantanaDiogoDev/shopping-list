import br.com.shopping_list.dtos.UserDTO
import br.com.shopping_list.entities.User
import br.com.shopping_list.repositories.UserRepository
import br.com.shopping_list.services.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@SpringBootTest
class UserServiceTest {

    private val userRepository = mock(UserRepository::class.java)
    private val passwordEncoder = mock(BCryptPasswordEncoder::class.java)

    private val userService = UserService(userRepository, passwordEncoder)

    @Test
    fun `test create user successfully`() {
        val userDTO = UserDTO(
            id = null,
            name = "John Doe",
            email = "john@example.com",
            password = "password123"
        )

        `when`(passwordEncoder.encode(userDTO.password)).thenReturn("encryptedPassword")

        val savedUser = User(
            id = UUID.randomUUID(),
            name = userDTO.name,
            email = userDTO.email,
            password = "encryptedPassword"
        )

        `when`(userRepository.save(any(User::class.java))).thenReturn(savedUser)

        val result = userService.createUser(userDTO)

        assertNotNull(result.id)
        assertEquals(userDTO.name, result.name)
        assertEquals(userDTO.email, result.email)
    }

    @Test
    fun `test get user by id successfully`() {
        val userId = UUID.randomUUID()
        val user = User(
            id = userId,
            name = "John Doe",
            email = "john@example.com",
            password = "encryptedPassword"
        )

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))

        val result = userService.getUserById(userId.toString())

        assertEquals(userId, result?.id)
        assertEquals(user.name, result?.name)
    }

    @Test
    fun `test get user by id throws exception when not found`() {
        val userId = UUID.randomUUID()

        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        assertThrows<IllegalArgumentException> {
            userService.getUserById(userId.toString())
        }
    }

    @Test
    fun `test get all users`() {
        val user1 = User(
            id = UUID.randomUUID(),
            name = "John Doe",
            email = "john@example.com",
            password = "encryptedPassword"
        )
        val user2 = User(
            id = UUID.randomUUID(),
            name = "Jane Doe",
            email = "jane@example.com",
            password = "encryptedPassword"
        )

        `when`(userRepository.findAll()).thenReturn(listOf(user1, user2))

        val result = userService.getAllUsers()

        assertEquals(2, result.size)
        assertEquals(user1.id, result[0].id)
        assertEquals(user2.id, result[1].id)
    }
}
