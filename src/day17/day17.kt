package day17

import readInputForDay

fun part1(input: List<List<Boolean>>) {
    var space = mutableListOf(input.map { it.toMutableList() }.toMutableList())
    repeat(6) {
        space = space.map { yList ->
            yList.map { xList ->
                xList.add(0, false)
                xList.add(false)
                xList
            }.toMutableList()
            val emptyAdd = MutableList(yList[0].size) { false }
            yList.add(0, emptyAdd)
            yList.add(emptyAdd)
            yList
        }.toMutableList()
        val emptyAdd = MutableList(space[0].size) { MutableList(space[0][0].size) { false } }
        space.add(0, emptyAdd)
        space.add(emptyAdd)

        val newSpace = MutableList(space.size) { MutableList(space[0].size) { MutableList(space[0][0].size) { false } } }

        for (z in 0..space.lastIndex) {
            for (y in 0..space[z].lastIndex) {
                for (x in 0..space[z][y].lastIndex) {
                    var activeCount = 0
                    val isActive = space[z][y][x]
                    val zRange = (z-1).coerceAtLeast(0)..(z+1).coerceAtMost(space.lastIndex)
                    val yRange = (y-1).coerceAtLeast(0)..(y+1).coerceAtMost(space[z].lastIndex)
                    val xRange = (x-1).coerceAtLeast(0)..(x+1).coerceAtMost(space[z][y].lastIndex)
                    for (z1 in zRange) {
                        for (y1 in yRange) {
                            for (x1 in xRange) {
                                if (z1 != z || y1 != y || x1 !=x) {
                                    if (space[z1][y1][x1]) {
                                        activeCount++
                                    }
                                }
                            }
                        }
                    }
                    newSpace[z][y][x] = if (isActive) {
                        !(activeCount != 2 && activeCount != 3)
                    } else {
                        activeCount == 3
                    }
                }
            }
        }
        space = newSpace
    }
    val result1 = space.fold(0) { acc1, list1 ->
        acc1 + list1.fold(0) { acc2, list2 ->
            acc2 + list2.fold(0) { acc3, b -> acc3 + if (b) 1 else 0 }
        }
    }
    println("Result 1: $result1")
}

fun part2(input: List<List<Boolean>>) {
    var space = mutableListOf(mutableListOf(input.map { it.toMutableList() }.toMutableList()))
    repeat(6) {
        space = space.map { zList ->
            zList.map { yList ->
                yList.map { xList ->
                    xList.add(0, false)
                    xList.add(false)
                    xList
                }.toMutableList()
                val emptyAdd = MutableList(yList[0].size) { false }
                yList.add(0, emptyAdd)
                yList.add(emptyAdd)
                yList
            }.toMutableList()
            val emptyAdd = MutableList(zList[0].size) { MutableList(zList[0][0].size) { false } }
            zList.add(0, emptyAdd)
            zList.add(emptyAdd)
            zList
        }.toMutableList()
        val emptyAdd = MutableList(space[0].size) {
            MutableList(space[0][0].size) { MutableList(space[0][0][0].size) { false } }
        }
        space.add(0, emptyAdd)
        space.add(emptyAdd)

        val newSpace = MutableList(space.size) {
            MutableList(space[0].size) { MutableList(space[0][0].size) { MutableList(space[0][0][0].size) { false } } }
        }

        for(w in 0..space.lastIndex) {
            for (z in 0..space[w].lastIndex) {
                for (y in 0..space[w][z].lastIndex) {
                    for (x in 0..space[w][z][y].lastIndex) {
                        var activeCount = 0
                        val isActive = space[w][z][y][x]
                        val wRange = (w-1).coerceAtLeast(0)..(w+1).coerceAtMost(space.lastIndex)
                        val zRange = (z-1).coerceAtLeast(0)..(z+1).coerceAtMost(space[w].lastIndex)
                        val yRange = (y-1).coerceAtLeast(0)..(y+1).coerceAtMost(space[w][z].lastIndex)
                        val xRange = (x-1).coerceAtLeast(0)..(x+1).coerceAtMost(space[w][z][y].lastIndex)
                        for (w1 in wRange) {
                            for (z1 in zRange) {
                                for (y1 in yRange) {
                                    for (x1 in xRange) {
                                        if (w1 != w || z1 != z || y1 != y || x1 !=x) {
                                            if (space[w1][z1][y1][x1]) {
                                                activeCount++
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        newSpace[w][z][y][x] = if (isActive) {
                            !(activeCount != 2 && activeCount != 3)
                        } else {
                            activeCount == 3
                        }
                    }
                }
            }
        }
        space = newSpace
    }
    val result = space.fold(0) { acc1, list1 ->
        acc1 + list1.fold(0) { acc2, list2 ->
            acc2 + list2.fold(0) { acc3, list3 ->
                acc3 + list3.fold(0) { acc4, b ->
                    acc4+ if (b) 1 else 0
                }
            }
        }
    }
    println("Result 2: $result")
}

fun main() {
    val input = readInputForDay(17).lines().mapIndexed { index1, s ->
        s.toList().mapIndexed { index2, c ->
            c == '#'
        }
    }

    part1(input)
    part2(input)
}