package br.com.shopping_list.repositories

import br.com.shopping_list.entities.ShoppingList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShoppingListRepository : JpaRepository<ShoppingList, UUID>