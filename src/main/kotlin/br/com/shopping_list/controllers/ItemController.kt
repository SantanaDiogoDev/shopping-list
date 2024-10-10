package br.com.shopping_list.controllers

import br.com.shopping_list.dtos.ItemDTO
import br.com.shopping_list.services.ItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/items")
class ItemController @Autowired constructor(
    private val itemService: ItemService
){
    @PostMapping
    fun createList(@RequestBody itemDTO: ItemDTO): ResponseEntity<ItemDTO>{
        val newItem = itemService.createItem(itemDTO)
        return ResponseEntity.ok(newItem)
    }

    @GetMapping("/{id}")
    fun getItemById(@PathVariable id: UUID): ResponseEntity<ItemDTO>{
        val itemDTO = itemService.getItemById(id)
        return if (itemDTO != null) {
            ResponseEntity.ok(itemDTO)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllItems(): List<ItemDTO>{
        return itemService.getAllItems()
    }
}