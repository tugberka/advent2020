package day16

import readInputForDay

data class TicketRange(
        val name: String,
        val ranges: List<IntRange>
)

fun parseTicketLine(line: String) = line.split(',').map { it.toInt() }

fun main() {
    val input = readInputForDay(16).split("\n\n")
    val ticketRanges = input[0].lines().map {
        it.split(": ").let {
            val name = it.first()
            val ranges = it[1].split(" or ").map {
                it.split('-').let { IntRange(it[0].toInt(), it[1].toInt()) }
            }
            TicketRange(name, ranges)
        }
    }
    val ticket = parseTicketLine(input[1].lines()[1])
    val nearbyTickets = input[2].lines().let { it.subList(1, it.size) }.map { parseTicketLine(it) }
    val allranges = ticketRanges.map { it.ranges }.flatten()

    val invalidSum = nearbyTickets.fold(0) { acc, list ->
        acc + list.filter { value -> allranges.firstOrNull { range -> range.contains(value) } == null }.sum()
    }

    println("Result 1: $invalidSum")

    val validTickets = nearbyTickets.filter { list ->
        list.none { value -> allranges.firstOrNull { range -> range.contains(value) } == null }
    }.toMutableList().apply { add(ticket) }

    val indexedValues = mutableListOf<MutableList<Int>>()
    val ticketLength = ticket.lastIndex
    for (i in 0..ticketLength) {
        validTickets.forEach {
            if (indexedValues.lastIndex < i) {
                indexedValues.add(i, mutableListOf(it[i]))
            } else {
                indexedValues[i].add(it[i])
            }
        }
    }

    val tempResultMap = mutableMapOf<Int, MutableList<TicketRange>>()

    for (i in 0..ticketLength) {
        val values = indexedValues[i]
        val ranges = ticketRanges.filter { ticketRange ->
            values.all { value -> ticketRange.ranges.firstOrNull { it.contains(value) } != null }
        }

        tempResultMap[i] = ranges.toMutableList()
    }

    val resultMap = mutableMapOf<TicketRange, Int>()
    tempResultMap.entries.sortedBy { it.value.size }.forEach {
        resultMap[it.value.first { !resultMap.keys.contains(it) }] = it.key
    }
    val result2 = resultMap
            .filter { it.key.name.startsWith("departure") }
            .values
            .map { it.toLong() }
            .fold(1L) { acc, i -> acc * ticket[i.toInt()].toLong() }
    println("Result 2: $result2")
}