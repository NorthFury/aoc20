package d3

import java.io.File

fun main() {
    val lines = File("input/d3.input").readLines().filter(String::isNotEmpty)

    val angles = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)

    val result = angles.map { countCollisions(lines, it.first, it.second) }
        .also(::println)
        .reduce { acc, x -> acc * x }
    println(result)
}

private fun countCollisions(lines: List<String>, right: Int, down: Int): Long =
    lines.filterIndexed { i, line -> i % down == 0 && line[(i / down * right) % line.length] == '#' }
        .count()
        .toLong()