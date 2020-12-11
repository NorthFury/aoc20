package d11

import java.io.File

fun main() {
    val seats = File("input/d11.input").readLines().filter(String::isNotBlank)

    val stableConfiguration = findStableConfiguration(seats)
    stableConfiguration.map { it.count { it == '#' } }.sum().let(::println)
}

private fun findStableConfiguration(seats: List<String>): List<String> {
    var previous = seats
    while (true) {
        val next = getNextSeatIteration(previous)
        if (previous == next) return previous
        previous = next
    }
}

private fun getNextSeatIteration(seats: List<String>): List<String> {
    return seats.mapIndexed { i, row ->
        row.mapIndexed { j, c ->
            val occupiedAdjacentSeats = countOccupiedAdjacentSeats(seats, i, j)
            when {
                c == 'L' && occupiedAdjacentSeats == 0 -> '#'
                c == '#' && occupiedAdjacentSeats >= 4 -> 'L'
                else -> c
            }
        }.joinToString("")
    }
}

private fun countOccupiedAdjacentSeats(seats: List<String>, i: Int, j: Int): Int {
    return listOf(
        i - 1 to j - 1,
        i - 1 to j,
        i - 1 to j + 1,
        i to j - 1,
        i to j + 1,
        i + 1 to j - 1,
        i + 1 to j,
        i + 1 to j + 1,
    )
        .mapNotNull { seats.getOrNull(it.first, it.second) }
        .count { it == '#' }
}

private fun List<String>.getOrNull(i: Int, j: Int): Char? {
    if (i < 0 || i > lastIndex) return null

    val row = this[i]
    if (j < 0 || j > row.lastIndex) return null

    return row[j]
}
