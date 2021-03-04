package day24

import readInputForDay

typealias Tile = Pair<Int, Int>

sealed class Direction(val xComp: Int, val yComp: Int) {
    object East: Direction(2, 0)
    object SouthEast: Direction(1, -1)
    object NorthEast: Direction(1, 1)
    object West: Direction(-2, 0)
    object SouthWest: Direction(-1, -1)
    object NorthWest: Direction(-1, 1)

    companion object {
        val allDirections = listOfNotNull(East, SouthEast, NorthEast, West, SouthWest, NorthWest)
        fun getNeighborAtDirection(tile: Tile, direction: Direction): Tile {
            return tile.first + direction.xComp to tile.second + direction.yComp
        }
    }
}

fun parseLine(line: String): List<Direction> {
    var skipNext = false
    return line.mapIndexed { i, c ->
        if (skipNext) {
            skipNext = false
            return@mapIndexed null
        }
        when (c) {
            'e' -> Direction.East
            'w' -> Direction.West
            's' -> {
                skipNext = true
                if (line[i + 1] == 'e') Direction.SouthEast else Direction.SouthWest
            }
            'n' -> {
                skipNext = true
                if (line[i + 1] == 'e') Direction.NorthEast else Direction.NorthWest
            }
            else -> null
        }
    }.filterNotNull()
}

fun findDestination(directions: List<Direction>): Tile {
    var xComp = 0
    var yComp = 0

    directions.forEach {
        xComp += it.xComp
        yComp += it.yComp
    }

    return xComp to yComp
}

fun generateMissingTiles(currentTiles: Map<Tile, Boolean>): MutableMap<Tile, Boolean> {
    val retTiles = currentTiles.toMutableMap()
    currentTiles.filter { it.value }.forEach { (tile, _) ->
        Direction.allDirections
                .map { Direction.getNeighborAtDirection(tile, it) }
                .forEach { retTiles.getOrPut(it) {false} }
    }
    return retTiles
}

fun getBlackCount(tilesMap: Map<Tile, Boolean>) = tilesMap.count { it.value }

fun main() {
    val input = readInputForDay(24).lines().map {
        parseLine(it)
    }

    val dests = input.map { findDestination(it).let { it.first to it.second } }

    val countMap = mutableMapOf<Tile, Int>()
    dests.forEach { orig ->
        if (!countMap.containsKey(orig)) {
            countMap[orig] = dests.count { it == orig }
        }
    }

    val blackTiles = countMap.filter { it.value % 2 == 1 }
    val result1 = blackTiles.size
    println("Result 1: $result1")

    var currentTiles = blackTiles.map { it.key to true }.toMap().toMutableMap()

    println("Day 0: ${getBlackCount(currentTiles)}")

    repeat(100) {
        currentTiles = generateMissingTiles(currentTiles)
        val tilesToFlip = currentTiles.mapNotNull { (tile, isBlack) ->
            val blackNeighborCount = Direction.allDirections.count { direction ->
                currentTiles[Direction.getNeighborAtDirection(tile, direction)] == true
            }
            if (isBlack) {
                if (blackNeighborCount == 0 || blackNeighborCount > 2) {
                    tile
                } else null
            } else {
                if (blackNeighborCount == 2) {
                    tile
                } else null
            }
        }
        tilesToFlip.forEach { currentTiles[it] = !currentTiles[it]!! }
        println("Day ${it + 1}: ${getBlackCount(currentTiles)}")
    }
}