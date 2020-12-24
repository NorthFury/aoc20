package d24

import d24.Direction.*
import java.io.File

fun main() {
    val paths = File("input/d24.input")
        .readLines().filter(String::isNotBlank)
        .map { line ->
            line.replace("ne", "a")
                .replace("nw", "b")
                .replace("se", "c")
                .replace("sw", "d")
                .map {
                    when (it) {
                        'e' -> E
                        'w' -> W
                        'a' -> NE
                        'b' -> NW
                        'c' -> SE
                        'd' -> SW
                        else -> throw Exception("up")
                    }
                }
        }

    val blackHexes = paths.map { moves ->
        moves.fold(Hex(0, 0)) { acc, direction ->
            when (direction) {
                E -> acc.copy(x = acc.x + 2)
                NE -> acc.copy(x = acc.x + 1, y = acc.y + 1)
                NW -> acc.copy(x = acc.x - 1, y = acc.y + 1)
                W -> acc.copy(x = acc.x - 2)
                SW -> acc.copy(x = acc.x - 1, y = acc.y - 1)
                SE -> acc.copy(x = acc.x + 1, y = acc.y - 1)
            }
        }
    }
        .groupBy { it }
        .filterValues { it.size % 2 == 1 }
        .keys
    println(blackHexes.size)


    1.rangeTo(100)
        .fold(blackHexes) { acc, _ -> getNextIteration(acc) }
        .size.let(::println)
}

private fun getNextIteration(cubes: Set<Hex>): Set<Hex> {
    val allCubes = cubes.asSequence().flatMap(Hex::getNeighbours).toSet() + cubes
    return allCubes
        .filter { hex ->
            val blackNeighbours = hex.getNeighbours().intersect(cubes).size
            when (cubes.contains(hex)) {
                true -> blackNeighbours == 1 || blackNeighbours == 2
                else -> blackNeighbours == 2
            }
        }
        .toSet()
}

private data class Hex(
    val x: Int,
    val y: Int,
) {
    fun getNeighbours(): Set<Hex> = setOf(
        this.copy(x = x + 2),
        this.copy(x = x - 2),
        this.copy(x = x + 1, y = y + 1),
        this.copy(x = x + 1, y = y - 1),
        this.copy(x = x - 1, y = y + 1),
        this.copy(x = x - 1, y = y - 1),
    )
}

private enum class Direction {
    E, NE, NW, W, SW, SE
}
