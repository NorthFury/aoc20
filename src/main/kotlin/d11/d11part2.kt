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
                c == '#' && occupiedAdjacentSeats >= 5 -> 'L'
                else -> c
            }
        }.joinToString("")
    }
}

private fun countOccupiedAdjacentSeats(seats: List<String>, i: Int, j: Int): Int {
    fun List<String>.getSeatInSightOrNull(iDelta: Int, jDelta: Int): Char? {
        var k = 1
        while (true) {
            val ii = iDelta * k + i
            val jj = jDelta * k + j

            if (ii < 0 || ii > lastIndex) return null

            val row = this[ii]
            if (jj < 0 || jj > row.lastIndex) return null

            val c = row[jj]
            if (c == 'L' || c == '#') return c

            k++
        }
    }
    return listOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1,
    )
        .mapNotNull { seats.getSeatInSightOrNull(it.first, it.second) }
        .count { it == '#' }
}

