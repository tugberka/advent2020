package day7

import readInputForDay

fun getContainerBags(color: String, bags: Map<String, Map<String, Int>>): Map<String, Map<String, Int>> {
    val continerBags = bags.filter {
        it.value.keys.contains(color)
    }

    return if (continerBags.isEmpty()) {
        continerBags
    } else {
        continerBags + continerBags.map {
            getContainerBags(it.key, bags).toList()
        }.flatten().toMap()
    }
}

fun getContainedBags(color: String, bags: Map<String, Map<String, Int>>): Int {
    val containedBags = bags[color]!!

    return if (containedBags.isEmpty()) {
        0
    } else {
        containedBags.map {
            it.value + it.value * getContainedBags(it.key, bags)
        }.sum()
    }
}

fun main() {
    val input = readInputForDay(7).lines().map { rule ->
        val sp1 = rule.split(" contain ")
        val bagColor = sp1.first().removeSuffix(" bags")
        val contains = sp1.last().let {
            if (it.startsWith("no other bags")) {
                emptyMap()
            } else {
                it.split(", ").map {
                    it.split(' ').let {
                        it.subList(1, it.lastIndex).reduce { acc, s -> "$acc $s" } to it.first().toInt()
                    }
                }.toMap()
            }
        }
        bagColor to contains
    }.toMap()

    val part1 = getContainerBags("shiny gold", input).size
    println("Result 1: $part1")

    val part2 = getContainedBags("shiny gold", input)
    println("Result 2: $part2")
}