package day14

import readInputForDay
import kotlin.math.pow

data class MaskOp(val maskValue: Long, val isAnd: Boolean)

sealed class Operation

data class MemWrite(val address: Int, val value: Long): Operation()
data class Mask(val ops: List<MaskOp>, val floatingOps: List<List<MaskOp>>): Operation()

fun maskStrToMaskOps(maskStr: String): List<MaskOp> {
    val maskStrReversed = maskStr.reversed()
    return maskStrReversed.toList().mapIndexed { index, c ->
        var isAnd = false
        val res = when (c) {
            '0' -> {
                isAnd = true
                var str = ""
                str = str.padStart(index, '1').padStart(index + 1, '0').padStart(36, '1')
                str
            }
            '1' -> {
                var str = ""
                str = str.padStart(index, '0').padStart(index + 1, '1').padStart(36, '0')
                str
            }
            else -> null
        }
        res?.let { MaskOp(it.toLong(2), isAnd) }
    }.filterNotNull()
}

fun main() {
    val input: List<Operation> = readInputForDay(14).lines().map {
        if (it.startsWith("mask = ")) {
            val idx = it.indexOf("= ") + 2
            val maskStr = it.substring(idx)
            val ops = maskStrToMaskOps(maskStr)
            val maskStrReversed = maskStr.reversed()

            val xIndexList = maskStrReversed.toList().mapIndexed { index, c ->
                if (c == 'X') index else null
            }.filterNotNull()

            val possibleList = mutableListOf<Int>()
            repeat(2.0.pow(xIndexList.size.toDouble()).toInt()) {
                possibleList.add(it)
            }

            val floatingOps = possibleList.map {
                val raw = it.toString(2).padStart(xIndexList.size, '0').reversed()
                var rawIdx = 0
                val maskBuilder = StringBuilder()
                for (i in 0..35) {
                    if (!xIndexList.contains(i)) {
                        maskBuilder.append('X')
                    } else {
                        maskBuilder.append(raw[rawIdx])
                        rawIdx++
                    }
                }
                maskBuilder.toString().reversed()
            }.map { maskStrToMaskOps(it) }
            Mask(ops, floatingOps)
        } else {
            val address = it.substring(IntRange(it.indexOf('[') + 1, it.indexOf(']') - 1)).toInt()
            val value = it.substring(it.indexOf("= ") + 2).toLong()
            MemWrite(address, value)
        }
    }

    var latestMask = Mask(emptyList(), emptyList())
    val memory1 = mutableMapOf<Int, Long>()

    input.forEach {
        when (it) {
            is Mask -> latestMask = it
            is MemWrite -> {
                var value = it.value
                latestMask.ops.forEach {
                    value = if (it.isAnd) {
                        value and it.maskValue
                    } else {
                        value or it.maskValue
                    }
                }
                memory1[it.address] = value
            }
        }
    }

    val res1 = memory1.values.sum()
    println("Result 1: $res1")

    latestMask = Mask(emptyList(), emptyList())
    val memory2 = mutableMapOf<Long, Long>()

    input.forEach { op ->
        when (op) {
            is Mask -> latestMask = op
            is MemWrite -> {
                var value = op.address.toLong()
                latestMask.ops.forEach {
                    value = if (it.isAnd) {
                        value
                    } else {
                        value or it.maskValue
                    }
                }
                latestMask.floatingOps.forEach {
                    var value2 = value
                    it.forEach { op ->
                        value2 = if (op.isAnd) {
                            value2 and op.maskValue
                        } else {
                            value2 or op.maskValue
                        }
                    }

                    memory2[value2] = op.value
                }
            }
        }
    }

    val res2 = memory2.values.sum()
    println("Result 2: $res2")
}
