package br.com.shopping_list.controllers

import br.com.shopping_list.dtos.SharingDTO
import br.com.shopping_list.services.SharingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
@RestController
@RequestMapping("/api/shares")
class SharingController @Autowired constructor(private val sharingService: SharingService) {

    @GetMapping("/{id}")
    fun getSharingById(@PathVariable id: UUID): ResponseEntity<SharingDTO> {
        return try {
            val sharingDTO = sharingService.findById(id)
            ResponseEntity.ok(sharingDTO)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllShares(): List<SharingDTO> {
        return sharingService.findAll()
    }

    @PostMapping
    fun createShare(@RequestBody sharingDTO: SharingDTO): ResponseEntity<SharingDTO>{
        val newShare = sharingService.createShare(sharingDTO)
        return ResponseEntity.ok(newShare)
    }
}