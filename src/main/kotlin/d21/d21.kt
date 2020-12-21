package d21

import java.io.File

fun main() {
    val foods = File("input/d21.input").readLines().filter(String::isNotBlank)
        .map { line ->
            val ingredients = line.takeWhile { it != '(' }
            val allergens = line.drop(ingredients.length + 9).dropLast(1)
            Food(
                ingredients = ingredients.split(' ').filter(String::isNotBlank),
                allergens = allergens.split(',').map(String::trim)
            )
        }

    val allergensToPossibleIngredients = foods
        .flatMap { food -> food.allergens.map { it to food.ingredients } }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, v) ->
            v.map { it.toSet() }.reduce { a, b -> a.intersect(b) }
        }

    val ingredientsWithAllergens = allergensToPossibleIngredients.flatMap { it.value }.toSet()
    foods.map { food -> food.ingredients.filter { it !in ingredientsWithAllergens }.count() }
        .sum()
        .let(::println)

    val allergenToIngredientList = allergensToPossibleIngredients
        .map { (k, v) -> k to v }
        .sortedBy { it.second.size }
        .fold(emptyList<Pair<String, String>>() to emptySet<String>())
        { (accAllergenMapping, accIngredients), (allergen, ingredients) ->
            val matchingIngredient = (ingredients - accIngredients).first()
            (accAllergenMapping + (allergen to matchingIngredient)) to (accIngredients + matchingIngredient)
        }
        .first

    allergenToIngredientList
        .sortedBy { it.first }
        .joinToString(",") { it.second }
        .let(::println)
}

data class Food(
    val ingredients: List<String>,
    val allergens: List<String>,
)
