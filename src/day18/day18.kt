package day18

import readInputForDay
import java.util.*

fun toPostfix(exp: String, precedence: (Char)-> Int ): String {
    val stack = Stack<Char>().apply { push('#') }
    var postfix = ""

    exp.filter { !it.isWhitespace() }.forEach {
        when {
            it.isDigit() -> {
                postfix += it
            }
            it == '(' -> {
                stack.push(it)
            }
            it == ')' -> {
                while (stack.peek() != '#' && stack.peek() != '(') {
                    postfix += stack.pop()
                }
                stack.pop()
            }
            else -> {
                if (precedence(it) > precedence(stack.peek())) {
                    stack.push(it)
                } else {
                    while (stack.peek() != '#' && precedence(it) <= precedence(stack.peek())) {
                        postfix += stack.pop()
                    }
                    stack.push(it)
                }
            }
        }
    }

    while (stack.peek() != '#') {
        postfix += stack.pop()
    }

    return postfix
}

fun computePostfix(exp: String): Long {
    val stack = Stack<Long>()
    exp.forEach {
        when (it) {
            '+' -> stack.push(stack.pop() + stack.pop())
            '*' -> stack.push(stack.pop() * stack.pop())
            else -> stack.push(Character.getNumericValue(it).toLong())
        }
    }
    return stack.pop()
}

fun main() {
    val input = readInputForDay(18).lines()

    val precedence1: (Char) -> Int = { if (it == '+' || it == '*') 1 else 0 }
    val precedence2: (Char) -> Int = { if (it == '+') 2 else if (it == '*') 1 else 0 }

    val inputPostfix1 = input.map { toPostfix(it, precedence1) }
    val inputPostfix2 = input.map { toPostfix(it, precedence2) }

    val computeResults1 = inputPostfix1.map { computePostfix(it) }
    val computeResults2 = inputPostfix2.map { computePostfix(it) }
    println("Result 1: ${computeResults1.sum()}")
    println("Result 2: ${computeResults2.sum()}")
}