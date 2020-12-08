package day8

import readInputForDay

sealed class Command {

    abstract fun processLine(currentState: CodeState): CodeState

    data class Nop(val amount: Int): Command() {
        override fun processLine(currentState: CodeState): CodeState {
            return currentState.copy(currentLine = currentState.currentLine + 1)
        }
    }
    data class Acc(val amount: Int): Command() {
        override fun processLine(currentState: CodeState): CodeState {
            return currentState.copy(
                    currentLine = currentState.currentLine + 1,
                    accumulated = currentState.accumulated + amount
            )
        }
    }
    data class Jmp(val amount: Int): Command() {
        override fun processLine(currentState: CodeState): CodeState {
            return currentState.copy(currentLine = currentState.currentLine + amount)
        }
    }
    object Term: Command() {
        override fun processLine(currentState: CodeState): CodeState {
            return currentState.copy(terminated = true)
        }
    }

    companion object {
        fun parseLine(inputLine: String): Command {
            return inputLine.split(' ').let {
                val amount = it[1].toInt()
                when (it.first()) {
                    "nop" -> Nop(amount)
                    "acc" -> Acc(amount)
                    "jmp" -> Jmp(amount)
                    else -> throw IllegalStateException()
                }
            }
        }
    }
}

data class CodeState(
        val currentLine: Int = 0,
        val accumulated: Int = 0,
        val terminated: Boolean = false
)

fun processCode(code: MutableList<Pair<Command, Boolean>>): Pair<Int, Boolean> {
    var state = CodeState()

    while (true) {
        val currentLine = state.currentLine
        val currentCode = code[currentLine]
        if (currentCode.first is Command.Term) {
            return state.accumulated to false
        }
        if (currentCode.second) {
            return state.accumulated to true
        }
        state = currentCode.first.processLine(state)
        code[currentLine] = currentCode.first to true
    }
}

fun main() {
    val code = readInputForDay(8).lines().map {
        Command.parseLine(it)
    }.map {
        it to false
    }.toMutableList().apply { add(Command.Term to false) }.toList()

    val p1 = processCode(code.toMutableList()).first
    println("Result 1: $p1")

    code.forEachIndexed { index, pair ->
        if (pair.first is Command.Jmp) {
            val newCode = code.toMutableList()
            newCode[index] = Command.Nop((pair.first as Command.Jmp).amount) to pair.second
            val result = processCode(newCode)
            if (!result.second) {
                println("Result 2: ${result.first}")
                return
            }
        } else if (pair.first is Command.Nop) {
            val newCode = code.toMutableList()
            newCode[index] = Command.Jmp((pair.first as Command.Nop).amount) to pair.second
            val result = processCode(newCode)
            if (!result.second) {
                println("Result 2: ${result.first}")
                return
            }
        }
    }
}