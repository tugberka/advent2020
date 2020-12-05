package day5

import readInputForDay
import kotlin.math.pow

fun getSeatPart(code: String, highLetter: Char): Int {
    return code.toList().asReversed().map {
        if (it == highLetter) 2 else 0
    }.foldIndexed(0) { index, acc, c ->
        acc + c.toDouble().pow(index).toInt() * c / 2
    }
}

fun main() {
    val input = readInputForDay(5).lines()

    val seats = input.map {
        val row = getSeatPart(it.substring(0, 7), 'B')
        val column = getSeatPart(it.substring(7, 10), 'R')
        val id = 8 * row + column
        Triple(row, column, id)
    }.sortedBy { it.third }
    println("Part 1 result: ${seats.maxBy { it.third }?.third}")

    seats.forEachIndexed { index, triple ->
        if (index == 0 || index == seats.lastIndex) return@forEachIndexed
        if (triple.third - seats[index - 1].third > 1) {
            println("Part 2 resut: ${triple.third - 1}")
            return
        }
    }
}