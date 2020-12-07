package day6

import readInputForDay

fun main() {
    val input = readInputForDay(6).split("\n\n")

    val result1 = input.map {
        it.replace("\n", "").toSet()
    }.fold(0) { acc, s ->
        acc + s.size
    }

    println("Part 1: $result1")

    val result2 = input.map {
        it.split("\n").map { it.toSet() }.reduce { acc, list -> acc.intersect(list)  }.size
    }.sum()

    println("Part 2: $result2")
}