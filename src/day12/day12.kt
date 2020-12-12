package day12

import readInputForDay
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

fun main() {
    val input = readInputForDay(12).lines().map {
        it.first() to it.substring(1).toInt()
    }

    var currentDirection = 0
    var currentPos = Pair(0 , 0)

    input.forEach {
        when (it.first) {
            'E' -> currentPos = currentPos.copy(first = currentPos.first + it.second)
            'W' -> currentPos = currentPos.copy(first = currentPos.first - it.second)
            'N' -> currentPos = currentPos.copy(second = currentPos.second + it.second)
            'S' -> currentPos = currentPos.copy(second = currentPos.second - it.second)
            'L' -> currentDirection += it.second
            'R' -> currentDirection -= it.second
            'F' -> {
                val angle = Math.toRadians(currentDirection.toDouble())
                val move = it.second * cos(angle) to it.second * sin(angle)
                currentPos = currentPos.first + move.first.roundToInt() to currentPos.second + move.second.roundToInt()
            }
        }
        currentDirection = when {
            currentDirection < 0 -> currentDirection + 360
            currentDirection >= 360 -> currentDirection - 360
            else -> currentDirection
        }
    }

    val p1 = abs(currentPos.first) + abs(currentPos.second)
    println("Part 1: $p1")

    var wp = 10 to 1
    currentPos = Pair(0 , 0)
    input.forEach {
        when (it.first) {
            'E' -> wp = wp.copy(first = wp.first + it.second)
            'W' -> wp = wp.copy(first = wp.first - it.second)
            'N' -> wp = wp.copy(second = wp.second + it.second)
            'S' -> wp = wp.copy(second = wp.second - it.second)
            'L' -> {
                val angle = Math.toRadians(it.second.toDouble())
                wp = (wp.first * cos(angle) - wp.second * sin(angle)).roundToInt() to
                        (wp.first * sin(angle) + wp.second * cos(angle)).roundToInt()
            }
            'R' -> {
                val angle = Math.toRadians(-it.second.toDouble())
                wp = (wp.first * cos(angle) - wp.second * sin(angle)).roundToInt() to
                        (wp.first * sin(angle) + wp.second * cos(angle)).roundToInt()
            }
            'F' -> {
                currentPos = currentPos.first + wp.first * it.second to currentPos.second + wp.second * it.second
            }
        }
    }

    val p2 = abs(currentPos.first) + abs(currentPos.second)
    println("Part 2: $p2")

}