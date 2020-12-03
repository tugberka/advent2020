package day3

import readInputForDay

data class SlopeTraverse(
        val rightMove: Int,
        val bottomMove: Int,
        var count: Long = 0,
        var currentIndex: Int = 0
)

fun main() {
    val input = readInputForDay(3).lines()

    val p1 = getResult(input, listOf(SlopeTraverse(3, 1)))
    print("Part 1 result: $p1 \n")

    val p2 = getResult(input,
            listOf(SlopeTraverse(1, 1),
                    SlopeTraverse(3, 1),
                    SlopeTraverse(5, 1),
                    SlopeTraverse(7, 1),
                    SlopeTraverse(1, 2))
    )
    print("Part 2 result: $p2")
}

fun getResult(lines: List<String>, traverseList: List<SlopeTraverse>): Long {
    val length = lines[0].length
    lines.forEachIndexed { index, line ->
        traverseList.forEach {
            if (index % it.bottomMove == 0) {
                if (line[it.currentIndex % length] == '#') it.count++
                it.currentIndex += it.rightMove
            }
        }
    }
    return traverseList.map { it.count }.reduce { acc, l -> acc * l }
}