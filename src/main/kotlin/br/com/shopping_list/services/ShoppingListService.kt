package br.com.shopping_list.services

import br.com.shopping_list.dtos.ListDTO
import br.com.shopping_list.entities.List
import br.com.shopping_list.repositories.ListRepository
import br.com.shopping_list.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListService @Autowired constructor(
    private val listRepository: ListRepository,
    private val userRepository: UserRepository
){
    fun createList(listDTO: ListDTO): ListDTO {
        val user = userRepository.findById(listDTO.creatorId) ?: throw IllegalArgumentException("User not found")
        val newList = List(
            name = listDTO.name,
            description = listDTO.description,
            creatorId = listDTO.creatorId
        )
    }
}