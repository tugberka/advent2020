package day4

import readInputForDay

fun main() {
    val input = readInputForDay(4).split("\n\n").map {
        it.split(" ", "\n").map {
            it.split(':').let { it.first() to it.last() }
        }.toMap() }

    val valid1 = input.filter {
        it.size == 8 || (it.size == 7 && !it.containsKey("cid"))
    }

    print("Result 1: ${valid1.size} \n")

    val result2 = valid1.count { map ->
        try {
            IntRange(1920, 2002).contains(map["byr"]?.toInt()) &&
                    IntRange(2010, 2020).contains(map["iyr"]?.toInt()) &&
                    IntRange(2020, 2030).contains(map["eyr"]?.toInt()) &&
                    map["hgt"]?.let {
                        IntRange(150, 193).contains(it.split("cm").first().toIntOrNull()) ||
                                IntRange(59, 76).contains(it.split("in").first().toIntOrNull()) } ?: false &&
                    map["hcl"]?.let {
                        it.first() == '#' && it.substring(1).let {
                            it.matches("^[a-f0-9]{6}\$".toRegex())
                        }
                    } ?: false &&
                    listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").any { it == map["ecl"] } &&
                    map["pid"]?.let {
                        it.length == 9 && it.matches("^[0-9]+\$".toRegex())
                    } ?: false
        } catch (e: Throwable) {
            false
        }
    }

    print("Result 2: $result2 \n")
}