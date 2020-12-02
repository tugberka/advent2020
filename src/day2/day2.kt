package day2

import readInputForDay

data class PasswordHint(
        val letter: Char,
        val range: IntRange,
        val password: String
)


fun main() {
    val input = readInputForDay(2).lines().map { line ->
        line.split("-", " ", ": ").let {
            PasswordHint(
                    it[2][0],
                    IntRange(it[0].toInt(), it[1].toInt()),
                    it[3]
            )
        }
    }

    val result1 = input.count { hint ->
        with(hint) {
            range.contains(password.count { it == letter })
        }
    }

    val result2 = input.count { hint ->
        with(hint) {
            password.filterIndexed { index, _ -> index == range.first - 1 || index == range.last - 1 }.count { it == letter } == 1
        }
    }

    print("Part 1 result: $result1 \n")
    print("Part 2 result: $result2")
}