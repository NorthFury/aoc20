package d20

import java.io.File
import kotlin.math.sqrt

fun main() {
    val tiles = File("input/d20.input").readText()
        .split("\n\n").filter(String::isNotBlank)
        .map { input ->
            val parts = input.split('\n').filter(String::isNotBlank)
            val id = parts.first()
                .dropWhile { !it.isDigit() }
                .takeWhile { it.isDigit() }
                .toInt()
            Tile(id, parts.drop(1))
        }

    val n = sqrt(tiles.size.toDouble()).toInt()
    val result = reassemble(n, tiles, emptyList())

    result?.chunked(n)
        ?.let { map ->
            listOf(map.first(), map.last())
                .flatMap { listOf(it.first(), it.last()) }
                .map { it.id.toLong() }
                .reduce { a, b -> a * b }
                .let(::println)

            val fullMap = map.flatMap { line ->
                line.map(Tile::dataWithoutBorders).reduce { acc, it -> acc.zip(it).map { it.first + it.second } }
            }

            Tile(0, fullMap).variations
                .map(Tile::data)
                .map { it to findSeaMonsters(it) }
                .find { it.second.isNotEmpty() }
                ?.let { (map, positions) ->
                    val newMap = markSeaMonsters(map, positions)
                    newMap.map { line -> line.count { it == '#' } }
                        .sum()
                        .let(::println)
                }
        }
}

private fun reassemble(n: Int, tiles: List<Tile>, acc: List<Tile>): List<Tile>? {
    if (tiles.isEmpty()) return acc

    val map = acc.chunked(n)
    val leftNeighbour = map.lastOrNull()?.let { if (it.size == n) null else it.lastOrNull() }
    val topNeighbour = map.dropLast(1).lastOrNull()?.drop(map.last().size)?.firstOrNull()

    tiles.forEach { tile ->
        val remainingTiles = tiles.filter { it.id != tile.id }
        tile.variations.forEach { variation ->
            if ((leftNeighbour == null || leftNeighbour.right == variation.left) &&
                (topNeighbour == null || topNeighbour.bottom == variation.top)
            ) {
                val result = reassemble(n, remainingTiles, acc + variation)
                if (result != null) return result
            }
        }
    }
    return null
}

private data class Tile(val id: Int, val data: List<String>) {
    val top = data.first()
    val bottom = data.last()
    val left = data.map { it.first() }.joinToString("")
    val right = data.map { it.last() }.joinToString("")
    val variations by lazy {
        val t90 = rotated()
        val t180 = t90.rotated()
        val t270 = t180.rotated()
        listOf(this, flipped(), t90, t90.flipped(), t180, t180.flipped(), t270, t270.flipped())
    }

    fun flipped(): Tile = Tile(id, data.map { it.reversed() })
    fun rotated(): Tile = Tile(id, 0.until(data.size).map { i -> data.map { it[i] }.joinToString("").reversed() })

    fun dataWithoutBorders() = data.drop(1).dropLast(1).map { it.drop(1).dropLast(1) }
}

private val pattern = listOf(
    "                  # ",
    "#    ##    ##    ###",
    " #  #  #  #  #  #   "
)
private val inlinePattern = pattern.joinToString("")

private fun findSeaMonsters(map: List<String>): List<Pair<Int, Int>> {
    val patternHeight = pattern.size
    val patternLength = pattern.first().length
    return 0.until(map.size - patternHeight).flatMap { i ->
        0.until(map.size - patternLength).filter { j ->
            val mapSlice = map.subList(i, i + patternHeight).map { it.substring(j, j + patternLength) }
            mapSlice.joinToString("")
                .zip(inlinePattern)
                .all { (mapChar, patternChar) -> patternChar != '#' || (patternChar == '#' && mapChar == '#') }
        }.map { i to it }
    }
}

private fun markSeaMonsters(map: List<String>, positions: List<Pair<Int, Int>>): List<String> {
    val newMap = map.map { it.toMutableList() }
    positions.forEach { (i, j) ->
        pattern.forEachIndexed { pi, patternLine ->
            patternLine.forEachIndexed { pj, c ->
                if (c == '#') {
                    newMap[i + pi][j + pj] = 'O'
                }
            }
        }
    }
    return newMap.map { it.joinToString("") }
}
