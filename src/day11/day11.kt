package day11

import readInputForDay

fun visibleSeatOccupied(
        state: Map<Pair<Int, Int>, Char>,
        currentIndex: Pair<Int, Int>,
        direction: Pair<Int, Int>,
        toEnd: Boolean = false,
        lastRow: Int,
        lastColumn: Int
): Boolean {
    var end = false
    var multiplier = 1
    do {
        val index = currentIndex.first + direction.first * multiplier to currentIndex.second + direction.second * multiplier
        if (index.first < 0 || index.first > lastRow || index.second < 0 || index.second > lastColumn) {
            end = true
        } else {
            when (state[index]) {
                'L' -> return false
                '#' -> return true
            }
            multiplier++
        }
    } while (!end && toEnd)
    return false
}

fun findResult(input: Map<Pair<Int, Int>, Char>, tolerance: Int, toEnd: Boolean, lastRow: Int, lastColumn: Int): Int {
    var changed = true
    var state = input.toMap()
    while (changed) {
        changed = false
        val newState = mutableMapOf<Pair<Int, Int>, Char>()
        state.forEach {
            val index = it.key
            val c = it.value
            var occupied = 0
            if (c != '.') {
                for (i in -1..1) {
                    for (j in -1..1) {
                        if (i != 0 || j != 0) {
                            if (visibleSeatOccupied(state, index, i to j, toEnd, lastRow, lastColumn)) {
                                occupied++
                            }
                        }
                    }
                }
            }
            val newLetter =  when {
                c == 'L' && occupied == 0 -> {
                    changed = true
                    '#'
                }
                c == '#' && occupied >= tolerance -> {
                    changed = true
                    'L'
                }
                else -> c
            }
            newState[index] = newLetter
        }
        state = newState.toMutableMap()
    }
    return state.count { it.value == '#' }
}

fun main() {
    val input = readInputForDay(11).lines().mapIndexed { index1, s ->
        s.toList().mapIndexed { index2, c ->
            (index1 to index2 ) to c
        }
    }.flatten().toMap()

    val lastRow = input.entries.last().key.first
    val lastColumn = input.entries.last().key.second

    val p1 = findResult(input, 4, false, lastRow, lastColumn)
    println("Part 1: $p1")

    val p2 = findResult(input, 5, true, lastRow, lastColumn)
    println("Part 2: $p2")
}