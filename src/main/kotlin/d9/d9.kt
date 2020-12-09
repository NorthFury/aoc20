package d9

import java.io.File

fun main() {
    val values = File("input/d9.input").readLines()
        .filter(String::isNotBlank)
        .mapNotNull(String::toIntOrNull)

    val invalidNumber = findInvalidNumber(values) ?: return
    println(invalidNumber)

    val weakness = findWeakness(values, invalidNumber)
    weakness.minOrNull()
        ?.let { min -> weakness.maxOrNull()?.let { max -> min + max } }
        ?.let(::println)
}

private fun findWeakness(values: List<Int>, invalidNumber: Int): List<Int> =
    (0..values.lastIndex).map { i ->
        var sum = 0
        values.drop(i).takeWhile {
            sum += it
            sum - it < invalidNumber
        }
    }.first { it.size > 1 && it.sum() == invalidNumber }

private fun findInvalidNumber(values: List<Int>, preamble: Int = 25): Int? {
    return values.windowed(preamble + 1)
        .find { it.dropLast(1).getCombinations().all { (a, b) -> a + b != it.last() } }
        ?.last()
}

private fun List<Int>.getCombinations(): List<Pair<Int, Int>> = flatMapIndexed { i: Int, value: Int ->
    asSequence().drop(i + 1).map { value to it }
}
