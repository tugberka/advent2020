package day19

import readInputForDay

fun getRules(rawRules: List<List<String>>): List<String> {
    return rawRules.foldIndexed(rawRules.first().toMutableList()) { i, acc, list ->
        if (i == 0) {
            acc
        } else {
            getMerged(acc, list).toMutableList()
        }
    }
}

fun getMerged(rule1: List<String>, rule2: List<String>): List<String> {
    return rule1.map { s1 ->
        rule2.map { s2 ->
            s1 + s2
        }
    }.flatten()
}

fun parseRules(rules: List<String>): Map<Int, List<String>> {
    val ruleMap = mutableMapOf<Int, List<String>>()
    val rawRuleMap = mutableMapOf<Int, List<List<Int>>>()
    rules.forEach {
        it.split(": ").let {
            val id = it.first().toInt()
            val ruleStr = it[1]
            if (ruleStr.startsWith("\"")) {
                ruleMap[id] = listOf(ruleStr[1].toString())
            } else {
                val rawRules = ruleStr.split(" | ").map {
                    it.split(" ").map { it.toInt() }
                }
                rawRuleMap[id] = rawRules
            }
        }
    }

    while (rawRuleMap.isNotEmpty()) {
        val idsToRemove = mutableListOf<Int>()
        rawRuleMap.forEach { entry ->
            val rawRules = entry.value
            val rawRulesFlat = rawRules.flatten()
            if (rawRulesFlat.all { ruleMap.containsKey(it) }) {
                ruleMap[entry.key] = rawRules.map {
                    getRules(it.map { ruleMap[it]!! })
                }.flatten()

                idsToRemove.add(entry.key)
            }
        }
        idsToRemove.forEach { rawRuleMap.remove(it) }
    }
    return ruleMap
}

fun checkIfRestMatches11(line: String, ruleMap1: Map<Int, List<String>>): Pair<Boolean, String> {
    var lineMutable = line
    var allfound = false
    if (lineMutable.isNotEmpty()) {
        val found1 = ruleMap1[42]!!.firstOrNull { lineMutable.startsWith(it) }
        if (found1 != null) {
            lineMutable = lineMutable.removePrefix(found1)
            while (true) {
                val found2 = ruleMap1[11]!!.firstOrNull { lineMutable.startsWith(it) }
                lineMutable = if (found2 != null) {
                    lineMutable.removePrefix(found2)
                } else {
                    val (matches, rest) = checkIfRestMatches11(lineMutable, ruleMap1)
                    if (matches) {
                        rest
                    } else {
                        break
                    }
                }
            }
            val found3 = ruleMap1[31]!!.firstOrNull { lineMutable.startsWith(it) }
            if (found3 != null) {
                lineMutable = lineMutable.removePrefix(found3)
                allfound = true
            }
        }
    }
    return allfound to lineMutable
}

fun main() {
    val (rules, data) = readInputForDay(19).split("\n\n").map { it.lines() }

    val ruleMap1 = parseRules(rules)
    val zeroRules1 = ruleMap1[0]!!
    val p1 = data.count { zeroRules1.contains(it) }
    println("Result 1: $p1")

    var p2 = 0

    data.forEach { line ->
        var lineMutable = line
        while (true)  {
            val found = ruleMap1[8]!!.firstOrNull { lineMutable.startsWith(it) }
            if (found != null) {
                lineMutable = lineMutable.removePrefix(found)
                val (matches, rest) = checkIfRestMatches11(lineMutable, ruleMap1)
                if (matches && rest.isEmpty()) {
                    p2++
                    break
                }
            } else {
                break
            }
        }
    }

    println("Result 2: $p2")
}