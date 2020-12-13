package day13

import readInputForDay

fun findForTwo(bussesWithTimes: List<Pair<Long, Int>>, start: Long, increment: Long): Long {
    var value = start
    while (true) {
        if (bussesWithTimes.firstOrNull { (value + it.second) % it.first != 0L } == null) {
            break
        }
        value += increment
    }
    return value
}

fun findLCM(numbers: List<Long>): Long {
    val max = numbers.max()!!
    var lcm = max

    while (true) {
        if (numbers.firstOrNull { lcm.rem(it) != 0L } == null) {
            break
        }
        lcm += max
    }
    return lcm
}

fun main() {
    val (timestamp, busses) = readInputForDay(13).lines().let {
        it[0].toInt() to it[1].split(',')
    }

    val validBusses = busses.filter { it != "x" }.map { it.toInt() }
    val result1 = validBusses.map {
        it to it - (timestamp % it)
    }.minBy { it.second }!!

    println("Result 1: ${result1.first * result1.second}")

    val bussesWithTimes = busses.mapIndexed { index, s ->
        s to index
    }.filter { it.first != "x" }.map { it.first.toLong() to it.second % it.first.toInt() }.sortedByDescending {
        it.first
    }

    var start = bussesWithTimes.first().first
    var increment = 1L

    for (i in 1..bussesWithTimes.lastIndex) {
        start = findForTwo(bussesWithTimes.subList(i-1, i+1), start, increment)
        increment = findLCM(listOf(increment, bussesWithTimes[i-1].first, bussesWithTimes[i].first))
    }

    println("Result 1: $start")
}