package d16

import java.io.File

fun main() {
    val (rulesInput, ticketInput, otherTicketsInput) = File("input/d16.input").readText().split("\n\n")

    val rules = rulesInput.reader().readLines().map(::Rule)
    val ticket = ticketInput.reader().readLines().drop(1).first()
        .split(',').mapNotNull(String::toIntOrNull)
    val otherTickets = otherTicketsInput.reader().readLines().drop(1).map {
        it.split(',').mapNotNull(String::toIntOrNull)
    }

    otherTickets.flatten()
        .filter { value -> rules.all { !it.match(value) } }
        .sum()
        .let(::println)

    val validTickets =
        otherTickets.filter { it.all { value -> rules.any { rule -> rule.match(value) } } } + listOf(ticket)

    val rulesWithPosition = rules
        .map { rule ->
            val validPositions = (0..ticket.lastIndex).filter { i ->
                validTickets.map { it[i] }.all { rule.match(it) }
            }.toSet()
            rule to validPositions
        }
        .sortedBy { it.second.size }
        .fold(emptyList<Pair<Rule, Int>>() to emptySet<Int>()) { (accRules, accPositions), (rule, positions) ->
            val matchingPosition = (positions - accPositions).first()
            (accRules + (rule to matchingPosition)) to accPositions + matchingPosition
        }
        .first

    rulesWithPosition.filter { it.first.name.startsWith("departure") }
        .map { (_, position) -> ticket[position] }
        .map(Int::toLong)
        .reduce { acc, it -> acc * it }
        .let(::println)
}

private class Rule(input: String) {
    private companion object {
        private val pattern = Regex("""([a-z ]+): (\d+)-(\d+) or (\d+)-(\d+)""")
    }

    private val values = pattern.matchEntire(input)?.groupValues ?: throw Exception("up")
    val name = values[0]
    val ranges = values.drop(1).mapNotNull(String::toIntOrNull).chunked(2) { it[0]..it[1] }

    fun match(value: Int): Boolean = ranges.any { it.contains(value) }
}
