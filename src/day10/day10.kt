package day10

import readInputForDay

fun main() {
    val input = readInputForDay(10).lines().map { it.toInt() }.sorted()

    var lastJoltage = 0
    var oneDiffCount = 0
    var threeDiffCount = 0
    run loop@{
        input.forEach {
            val diff = it - lastJoltage
            if (diff > 3) {
                return@loop
            }

            lastJoltage = it
            if (diff == 1) {
                oneDiffCount++
            } else if (diff == 3) {
                threeDiffCount++
            }
        }
    }
    threeDiffCount++

    println("Result 1: ${threeDiffCount * oneDiffCount}")

    val list = input.toMutableList().apply { add(0, 0) }.map { it to 0L }.toMutableList()
    list[0] = 0 to 1L
    list.forEachIndexed { index1, pair1 ->
        if (index1 == 0) return@forEachIndexed
        var count = 0L
        run loop@{
            list.subList(0, index1).asReversed().forEachIndexed { index2, pair2 ->
                if (pair1.first - pair2.first > 3) {
                    return@loop
                }
                count += pair2.second
            }
        }
        list[index1] = pair1.first to count
    }

    println("Result 2: ${list.last().second}")
}
