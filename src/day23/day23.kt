package day23

import readInputForDay

data class Cup(
        val value: Int,
        var next: Cup? = null,
        var prev: Cup? = null
)

class Cups {
    private lateinit var head: Cup
    private lateinit var tail: Cup
    lateinit var currentCup: Cup
    private var cupsMap = mutableMapOf<Int, Cup>()

    fun addCup(value: Int) {
        val cup = Cup(value)
        if (!(::head.isInitialized)) {
            head = cup
            head.next = cup
            head.prev = cup
        } else {
            tail.next = cup
            cup.prev = tail
        }

        tail = cup
        tail.next = head
    }

    fun findCupWithValue(value: Int): Cup? {
        return cupsMap[value]
    }

    fun addCupAfter(afterCup: Cup, value: Int) {
        val cup = Cup(value)

        val curNext = afterCup.next!!
        afterCup.next = cup
        cup.next = curNext
        cup.prev = afterCup
        curNext.prev = cup

        if (afterCup.value == tail.value) {
            tail = cup
        }
        cupsMap[cup.value] = cup
        cupsMap[cup.next!!.value] = cup.next!!
        cupsMap[cup.prev!!.value] = cup.prev!!
    }

    fun removeCupsAfter(value: Int): List<Int> {
        val pickedUpCupValues = mutableListOf<Int>()
        val afterCup = findCupWithValue(value) ?: throw IllegalStateException()
        repeat(3) {
            val removed = afterCup.next!!
            val next = removed.next!!
            afterCup.next = next
            next.prev = afterCup
            if (removed == head) {
                head = next
            } else if (removed == tail) {
                tail = afterCup
            }
            pickedUpCupValues.add(removed.value)
            cupsMap[afterCup.value] = afterCup
            cupsMap[afterCup.next!!.value] = afterCup.next!!
            cupsMap[afterCup.prev!!.value] = afterCup.prev!!
        }

        return pickedUpCupValues
    }

    fun initGame() {
        currentCup = head
        var cup = head
        do {
            cupsMap[cup.value] = cup
            cup = cup.next!!
        }
        while (cup != head)
    }

    fun playRound(maxValue: Int) {
        val pickedUpCupValues = removeCupsAfter(currentCup.value)

        var destValue = currentCup.value
        var destination: Cup? = null
        while (destination == null) {
            destValue--
            if (destValue < 1) {
                destValue = maxValue
            }
            if (pickedUpCupValues.contains(destValue)) {
                continue
            } else {
                destination = findCupWithValue(destValue)
            }
        }

        pickedUpCupValues.reversed().forEach {
            addCupAfter(destination, it)
        }

        currentCup = currentCup.next!!
    }

    fun printCups() {
        var current = head

        print("cups: ")
        while (true) {
            if (currentCup == current) print("(")
            print(current.value)
            if (currentCup == current) print(")")
            if (current == tail) {
                print("\n")
                break
            } else {
                current = current.next!!
                print(" ")
            }
        }
    }
}

fun main() {
    val input = readInputForDay(23)
            .toList()
            .map { Integer.parseInt(it.toString()) }

    val cups = Cups()
    input.forEach { cups.addCup(it) }

    cups.initGame()
    repeat(100) {
        cups.playRound(9)
    }

    print("part 1: ")
    cups.printCups()

    val cups2 = Cups()
    input.forEach { cups2.addCup(it) }
    for(i in 10..1_000_000) {
        cups2.addCup(i)
    }
    cups2.initGame()

    repeat(10_000_000) {
        cups2.playRound(1_000_000)
    }

    val c1 = cups2.findCupWithValue(1)!!.next!!
    val c2 = c1.next!!

    val p2 = c1.value.toLong() * c2.value.toLong()
    println("part 2: $p2")
}