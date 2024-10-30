package br.com.shopping_list

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["br.com.shopping_list"])
class ShoppingListApplication

fun main(args: Array<String>) {
	runApplication<ShoppingListApplication>(*args)
}
