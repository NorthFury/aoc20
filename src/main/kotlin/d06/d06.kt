package d06

import java.io.File

fun main() {
    val lines = File("input/d06.input").readLines()
    val splitIndexes = listOf(-1) +
            lines.mapIndexedNotNull { i, s -> if (s.isBlank()) i else null } +
            listOf(lines.size)
    val groups = splitIndexes.windowed(2).map { (start, end) ->
        lines.subList(start + 1, end)
    }

    groups.map { group -> group.flatMap(String::toSet).toSet().size }
        .sum()
        .let(::println)

    groups.map { group -> group.map(String::toSet).reduce { acc, it -> acc.intersect(it) }.size }
        .sum()
        .let(::println)
}
