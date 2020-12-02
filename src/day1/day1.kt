package day1

import readInputForDay

fun main() {
    val input = readInputForDay(1).lines().map { it.toInt() }

    print("Part1: ${part1(input)} \n")
    print("Part2: ${part2(input)}")
}

fun part1(input: List<Int>, sum: Int = 2020, currentIndex: Int = -1): Int? {
    input.forEachIndexed { index, number ->
        if (index == currentIndex) {
            return@forEachIndexed
        }
        val diff = sum - number
        input.subList(index, input.lastIndex).forEach {
            if (it == diff) {
                return number * it
            }
        }
    }
    return null
}

fun part2(input: List<Int>): Int? {
    input.forEachIndexed { index, number ->
        val diff = 2020 - number
        val result = part1(input.subList(index, input.lastIndex), diff, index)
        if (result != null) {
            return number * result
        }
    }

    return null
}