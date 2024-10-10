package br.com.shopping_list.controllers

import br.com.shopping_list.dtos.ShoppingListDTO
import br.com.shopping_list.services.ShoppingListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/lists")
class ShoppingListController @Autowired constructor(private val listService: ShoppingListService) {

    @PostMapping
    fun createList(@RequestBody listDTO: ShoppingListDTO): ResponseEntity<ShoppingListDTO> {
        val newList = listService.createList(listDTO)
        return ResponseEntity.ok(newList)
    }

    @GetMapping("/{id}")
    fun getListById(@PathVariable id: UUID): ResponseEntity<ShoppingListDTO> {
        val listDTO = listService.getListById(id)
        return if (listDTO != null) {
            ResponseEntity.ok(listDTO)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllLists(): List<ShoppingListDTO>{
        return listService.getAllLists()
    }
}