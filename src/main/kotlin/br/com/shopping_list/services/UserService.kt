package br.com.shopping_list.services

import br.com.shopping_list.dtos.UserDTO
import br.com.shopping_list.entities.User
import br.com.shopping_list.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    fun createUser(userDTO: UserDTO): UserDTO {
        val encryptedPassword = passwordEncoder.encode(userDTO.password)
        val user = User(name = userDTO.name, email = userDTO.email, password = encryptedPassword)
        return UserDTO(id = userRepository.save(user).id, name = user.name, email = user.email, password = user.password)
    }

    fun getUserById(id: String): UserDTO? {
        val uuid = try {
            UUID.fromString(id)
        } catch (e: java.lang.IllegalArgumentException) {
            throw IllegalArgumentException("Invalid UUID format")
        }


        return userRepository.findById(uuid).orElseThrow {
            IllegalArgumentException("User not found!")
        }.let { user ->
            UserDTO(user.id, user.name, user.email, user.password)
        }
    }

    fun getAllUsers(): List<UserDTO> {
        val users =  userRepository.findAll();

        return users.map { user ->
            UserDTO(
                id = user.id,
                name = user.name,
                email = user.email,
                password = user.password
            )
        }
    }
}