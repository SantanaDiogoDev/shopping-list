package br.com.shopping_list.controllers

import br.com.shopping_list.configuration.UserNotFoundException
import br.com.shopping_list.dtos.UserDTO
import br.com.shopping_list.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody userDTO: UserDTO): ResponseEntity<UserDTO> {
        val newUser = userService.createUser(userDTO)
        return ResponseEntity.ok(newUser)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserDTO> {
        return try {
            val userDTO = userService.getUserById(id)
            ResponseEntity.ok(userDTO)
        } catch (e: UserNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(null)
        }
    }

    @GetMapping
    fun getAllUsers(): List<UserDTO> {
        return userService.getAllUsers();
    }
}