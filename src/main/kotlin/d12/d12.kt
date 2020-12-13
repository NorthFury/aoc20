package d12

import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val instructions = File("input/d12.input").readLines().filter(String::isNotBlank)
        .mapNotNull { line -> line.drop(1).toIntOrNull()?.let { line.first() to it } }

    val part1Position = moveShipPart1(instructions)
    println(part1Position.n.absoluteValue + part1Position.e.absoluteValue)

    val part2Position = moveShipPart2(instructions)
    println(part2Position.n.absoluteValue + part2Position.e.absoluteValue)
}

private fun moveShipPart1(instructions: List<Pair<Char, Int>>): FirstAcc =
    instructions.fold(FirstAcc(0, 0, 'E')) { acc, (instruction, value) ->
        when (instruction) {
            'L' -> acc.copy(direction = rotateShip(acc.direction, -value))
            'R' -> acc.copy(direction = rotateShip(acc.direction, value))
            'F' -> when (acc.direction) {
                'N' -> acc.copy(n = acc.n + value)
                'S' -> acc.copy(n = acc.n - value)
                'E' -> acc.copy(e = acc.e + value)
                'W' -> acc.copy(e = acc.e - value)
                else -> throw Exception("up")
            }
            'N' -> acc.copy(n = acc.n + value)
            'S' -> acc.copy(n = acc.n - value)
            'E' -> acc.copy(e = acc.e + value)
            'W' -> acc.copy(e = acc.e - value)
            else -> throw Exception("up")
        }
    }

private fun rotateShip(direction: Char, value: Int): Char {
    val directions = listOf('E', 'S', 'W', 'N')
    val newIndex = (directions.indexOf(direction) + value / 90) % 4

    if (newIndex < 0) return directions[newIndex + 4]
    return directions[newIndex]
}

private fun moveShipPart2(instructions: List<Pair<Char, Int>>): Position =
    instructions.fold(Position(0, 0) to Position(10, 1)) { (ship, waypoint), (instruction, value) ->
        when (instruction) {
            'L' -> when (value) {
                90 -> ship to Position(e = -waypoint.n, n = waypoint.e)
                180 -> ship to Position(e = -waypoint.e, n = -waypoint.n)
                270 -> ship to Position(e = waypoint.n, n = -waypoint.e)
                else -> throw Exception("up")
            }
            'R' -> when (value) {
                90 -> ship to Position(e = waypoint.n, n = -waypoint.e)
                180 -> ship to Position(e = -waypoint.e, n = -waypoint.n)
                270 -> ship to Position(e = -waypoint.n, n = waypoint.e)
                else -> throw Exception("up")
            }
            'F' -> Position(ship.e + value * waypoint.e, ship.n + value * waypoint.n) to waypoint
            'N' -> ship to waypoint.copy(n = waypoint.n + value)
            'S' -> ship to waypoint.copy(n = waypoint.n - value)
            'E' -> ship to waypoint.copy(e = waypoint.e + value)
            'W' -> ship to waypoint.copy(e = waypoint.e - value)
            else -> throw Exception("up")
        }
    }.first

private data class FirstAcc(
    val e: Int,
    val n: Int,
    val direction: Char,
)

private data class Position(
    val e: Int,
    val n: Int,
)
