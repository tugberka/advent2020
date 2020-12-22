package day22

import readInputForDay

fun playGame(p1: List<Int>, p2: List<Int>, recurse: Boolean = true): Pair<Boolean, List<Int>> {
    val prevGames = mutableListOf<Pair<List<Int>, List<Int>>>()

    val deck1 = p1.toMutableList()
    val deck2 = p2.toMutableList()

    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        val card1 =  deck1.removeAt(0)
        val card2 =  deck2.removeAt(0)
        if (recurse && card1 <= deck1.size && card2 <= deck2.size) {
            val result = playGame(deck1.subList(0, card1), deck2.subList(0, card2))
            if (result.first) {
                deck1.add(card1)
                deck1.add(card2)
            } else {
                deck2.add(card2)
                deck2.add(card1)
            }
        } else {
            if (card1 > card2) {
                deck1.add(card1)
                deck1.add(card2)
            } else {
                deck2.add(card2)
                deck2.add(card1)
            }
        }

        val game = deck1.toList() to deck2.toList()
        if (prevGames.contains(game)) {
            return true to deck1
        }
        prevGames.add(game)
    }

    return if (deck1.isNotEmpty()) {
        true to deck1
    } else {
        false to deck2
    }
}

fun main() {
    val (player1, player2) = readInputForDay(22).split("\n\n").map {
        it.lines().let { it.subList(1, it.size) }.map { it.toInt() }
    }

    val result1 = playGame(player1, player2, false).second.let { deck ->
        val size = deck.size
        deck.foldIndexed(0L) {index, acc, i -> acc + i * (size - index) }
    }
    println("Result 1: $result1")

    val result2 = playGame(player1, player2).second.let { deck ->
        val size = deck.size
        deck.foldIndexed(0L) {index, acc, i -> acc + i * (size - index) }
    }
    println("Result 2: $result2")
}