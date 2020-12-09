package d8

import java.io.File

fun main() {
    val instructions = File("input/d8.input").readLines().filter(String::isNotBlank)
        .map { it.take(3) to it.drop(4).toInt() }

    executeUntilLoopOrEnd(instructions).let(::println)

    (0..instructions.lastIndex)
        .filter { instructions[it].first in setOf("jmp", "nop") }
        .find { i -> isItFixed(instructions, i) }
        ?.let { brokenIndex ->
            val (operation, value) = instructions[brokenIndex]
            val changedOperation = if (operation == "jmp") "nop" else "jmp"
            val fixedInstructions = instructions.subList(0, brokenIndex) +
                    listOf(changedOperation to value) +
                    instructions.subList(brokenIndex + 1, instructions.size)

            executeUntilLoopOrEnd(fixedInstructions).let(::println)
        }
}

private fun executeUntilLoopOrEnd(instructions: List<Pair<String, Int>>): Int {
    val executedOperations = mutableSetOf<Int>()
    var cursor = 0
    var acc = 0
    while (!executedOperations.contains(cursor) && cursor <= instructions.lastIndex) {
        executedOperations.add(cursor)
        val (operation, value) = instructions[cursor]
        when (operation) {
            "acc" -> {
                cursor++
                acc += value
            }
            "jmp" -> cursor += value
            "nop" -> cursor++
        }
    }
    return acc
}

private fun isItFixed(instructions: List<Pair<String, Int>>, changedIndex: Int): Boolean {
    val executedOperations = mutableSetOf<Int>()
    var cursor = 0
    while (!executedOperations.contains(cursor) && cursor <= instructions.lastIndex) {
        executedOperations.add(cursor)
        val (operation, value) = instructions[cursor]
        when {
            operation == "jmp" && changedIndex == cursor -> cursor++
            operation == "nop" && changedIndex == cursor -> cursor += value
            operation == "jmp" -> cursor += value
            operation == "acc" -> cursor++
            operation == "nop" -> cursor++
        }
    }
    return cursor == instructions.lastIndex + 1
}
