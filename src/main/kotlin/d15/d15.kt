package d15

import java.io.File

fun main() {
    val numbers = File("input/d15.input").readLines().first()
        .split(',')
        .mapNotNull(String::toIntOrNull)

    iterate(numbers, 2020).let(::println)
    iterate(numbers, 30000000).let(::println)
}

private fun iterate(initialNumber: List<Int>, n: Int): Int {
    val acc = initialNumber
        .dropLast(1)
        .mapIndexed { i, it -> it to i }
        .toMap().toMutableMap()
    var last = initialNumber.last()
    for (i in initialNumber.lastIndex.until(n - 1)) {
        acc[last]?.let { lastIndex ->
            acc[last] = i
            last = i - lastIndex
        } ?: run {
            acc[last] = i
            last = 0
        }
    }
    return last
}
