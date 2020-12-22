package day20

import readInputForDay

data class Tile(
        val id: Int,
        val points: List<List<Char>>,
        val neighbors: MutableList<Tile> = mutableListOf()
) {
    val edges = listOf(
            points[0],
            points.map { it.last() },
            points.last(),
            points.map { it.first() },
            points[0].asReversed(),
            points.map { it.last() }.asReversed(),
            points.last().asReversed(),
            points.map { it.first() }.asReversed()
    )
}

fun main() {
    val input = readInputForDay(20).split("\n\n")
    val tiles = input.map { tileStr ->
        val lines = tileStr.lines()
        val id = lines.first().filter { it.isDigit() }.toInt()
        val points = lines.subList(1, lines.size).map { it.toList() }
        Tile(id, points)
    }

    tiles.forEachIndexed { index1, tile1 ->
        tiles.forEachIndexed { index2, tile2 ->
            if (index1 == index2) return@forEachIndexed
            if (tile2.edges.any { tile1.edges.contains(it) }) {
                tile1.neighbors.add(tile2)
            }
        }
    }

    println(tiles.map { it.id to it.neighbors.map { it.id } })

    val corners = tiles.filter { it.neighbors.size == 2 }
    println(corners.map { it.id.toLong() }.fold(1L){acc, i -> acc * i  })


}