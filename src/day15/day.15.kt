package day15

import readInputForDay
import kotlin.system.measureTimeMillis

fun getNumberSpoken(input: List<Int>, numOfTurns: Int): Int {
    val gameMap = input.mapIndexed { index, s ->
        s to mutableListOf(index + 1)
    }.toMap().toMutableMap()

    var lastSpokenNumber = input.last()
    var turn = input.size

    while (turn < numOfTurns) {
        turn++
        val lastNumberTurns = gameMap[lastSpokenNumber]!!
        val numberToSpeak = if (lastNumberTurns.size > 1) {
            lastNumberTurns.last() - lastNumberTurns[lastNumberTurns.lastIndex - 1]
        } else {
            0
        }
        if (gameMap.containsKey(numberToSpeak)) {
            gameMap[numberToSpeak] = gameMap[numberToSpeak]!!.apply { add(turn) }
        } else {
            gameMap[numberToSpeak] = mutableListOf(turn)
        }
        lastSpokenNumber = numberToSpeak
    }

    return lastSpokenNumber
}

fun main() {
    val input = readInputForDay(15).split(',').map { it.toInt() }

    println("Part 1: ${getNumberSpoken(input, 2020)}")
    val time = measureTimeMillis {
        val res = getNumberSpoken(input, 30000000)
        println("Part 2: ${res}")
    }

    println("took $time")

}