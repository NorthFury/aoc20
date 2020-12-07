package d5

import java.io.File

fun main() {
    val lines = File("input/d5.input").readLines().filter(String::isNotBlank)

    lines.asSequence()
        .map { it.take(7).decodeRow() * 8 + it.drop(7).decodeColumn() }
        .sorted()
        .windowed(2, 1)
        .find { (a, b) -> a + 2 == b }
        ?.let { it[0] + 1 }
        ?.let(::println)
}

private fun String.decodeRow() = fold(0) { acc, c ->
    acc.shl((1)) + c.let { if (c == 'F') 0 else 1 }
}

private fun String.decodeColumn() = fold(0) { acc, c ->
    acc.shl((1)) + c.let { if (c == 'L') 0 else 1 }
}