package day9

import readInputForDay

fun isValid(number: Long, preamble: List<Long>): Boolean {
    preamble.forEachIndexed { index, i ->
        if (preamble.toMutableList().apply { removeAt(index) }.contains(number - i)) {
            return true
        }
    }
    return false
}

fun main() {
    val input = readInputForDay(9).lines().map { it.toLong() }

    var invalidIndex = 0
    var invalidNumber = 0L

    for (i in 25..input.size) {
        if (!(isValid(input[i], input.subList(i - 25, i)))) {
            invalidIndex = i
            invalidNumber = input[i]
            break
        }
    }
    println("Result 1: $invalidNumber")
    val sumList = mutableListOf<Long>()
    var sum = 0L

    input.subList(0, invalidIndex).forEach {
        sum += it
        sumList.add(it)
        while (sum > invalidNumber) {
            val removed = sumList.removeAt(0)
            sum -= removed
        }
        if (sumList.size > 1 && sum == invalidNumber) {
            println("Result 2: ${sumList.min()!! + sumList.max()!!}")
            return
        }
    }
}