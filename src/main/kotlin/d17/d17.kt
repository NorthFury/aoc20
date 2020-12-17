package d17

import java.io.File

fun main() {
    doPart1()

    doPart2()
}

private fun doPart1() {
    val cubes = File("input/d17.input").readLines().filter(String::isNotBlank)
        .flatMapIndexed { x, line ->
            line.mapIndexedNotNull { y, c ->
                when (c) {
                    '#' -> Cube(x, y, 0)
                    else -> null
                }
            }
        }
        .toSet()

    (1..6).fold(cubes) { acc, _ -> getNextIteration(acc) }
        .size
        .let(::println)
}

private fun getNextIteration(cubes: Set<Cube>): Set<Cube> {
    val allCubes = cubes.asSequence().flatMap(Cube::getNeighbours).toSet() + cubes
    return allCubes
        .filter { cube ->
            val activeNeighbours = cube.getNeighbours().intersect(cubes).size
            when (cubes.contains(cube)) {
                true -> activeNeighbours == 2 || activeNeighbours == 3
                else -> activeNeighbours == 3
            }
        }
        .toSet()
}

private data class Cube(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    fun getNeighbours(): Set<Cube> {
        val values = listOf(-1, 0, 1)
        return values
            .flatMap { i ->
                values.flatMap { j ->
                    values.map { k ->
                        Cube(x + i, y + j, z + k)
                    }
                }
            }
            .toSet() - this
    }
}

private fun doPart2() {
    val cubes = File("input/d17.input").readLines().filter(String::isNotBlank)
        .flatMapIndexed { x, line ->
            line.mapIndexedNotNull { y, c ->
                when (c) {
                    '#' -> HyperCube(x, y, 0, 0)
                    else -> null
                }
            }
        }
        .toSet()

    (1..6).fold(cubes) { acc, _ -> getNextHyperIteration(acc) }
        .size
        .let(::println)
}

private fun getNextHyperIteration(cubes: Set<HyperCube>): Set<HyperCube> {
    val allCubes = cubes.asSequence().flatMap(HyperCube::getNeighbours).toSet() + cubes
    return allCubes
        .filter { cube ->
            val activeNeighbours = cube.getNeighbours().intersect(cubes).size
            when (cubes.contains(cube)) {
                true -> activeNeighbours == 2 || activeNeighbours == 3
                else -> activeNeighbours == 3
            }
        }
        .toSet()
}

private data class HyperCube(
    val x: Int,
    val y: Int,
    val z: Int,
    val w: Int
) {
    fun getNeighbours(): Set<HyperCube> {
        val values = listOf(-1, 0, 1)
        return values
            .flatMap { i ->
                values.flatMap { j ->
                    values.flatMap { k ->
                        values.map { l ->
                            HyperCube(x + i, y + j, z + k, w + l)
                        }
                    }
                }
            }
            .toSet() - this
    }
}
