package day21

import readInputForDay

data class Food(
        val ingredients: List<String>,
        val allergens: List<String>
)

fun main() {
    val foods = readInputForDay(21).lines().map {
        it.split('(').let {
            val ingredients = it.first().trim().split(' ')
            val allergens = it[1].removePrefix("contains ").removeSuffix(")").split(", ")
            Food(ingredients, allergens)
        }
    }

    val allergens = foods.map { it.allergens }.flatten().distinct()
    var matchMap = mutableMapOf<String, MutableList<String>>()
    allergens.forEach { allergen ->
        val ingredients = foods.filter { it.allergens.contains(allergen) }
                .map { it.ingredients }
                .reduce { acc, list -> acc.intersect(list).toList() }
        matchMap[allergen] = ingredients.toMutableList()
    }

    val ingredients = foods.map { it.ingredients }.flatten()
    val ingredientsDistinct = ingredients.distinct()
    val matched = matchMap.values.flatten().distinct()

    val diff = ingredientsDistinct.subtract(matched)
    val result1 = diff.sumBy { ing -> ingredients.count { it == ing } }
    println("Result 1: $result1")

    val allergenMap = mutableMapOf<String, String>()
    while (matchMap.isNotEmpty()) {
        val removeAllergenList = mutableListOf<String>()
        val removeIngList = mutableListOf<String>()
        matchMap.filter { it.value.size == 1 }.forEach {
            val ing = it.value.first()
            allergenMap[it.key] = ing
            removeAllergenList.add(it.key)
            removeIngList.add(ing)
        }
        removeAllergenList.forEach { matchMap.remove(it) }

        matchMap = matchMap.map {
            it.key to it.value.filter { !removeIngList.contains(it) }.toMutableList()
        }.toMap().toMutableMap()
    }

    val result2 = allergenMap.toSortedMap(Comparator { o1, o2 ->
        o1.compareTo(o2)
    }).values.toString().filter { !it.isWhitespace() }
    println("Result 2: $result2")
}
