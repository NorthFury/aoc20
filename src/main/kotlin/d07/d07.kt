package d07

import java.io.File

fun main() {
    val ruleLines = File("input/d07.input").readLines().filter(String::isNotBlank)

    val containerPattern = Regex("""\w+ \w+""")
    val bagsPattern = Regex("""(\d+) (\w+ \w+) bags?""")

    val rules = ruleLines.mapNotNull { rule ->
        containerPattern.find(rule)?.let { container ->
            val bags = when {
                rule.endsWith("no other bags.") ->
                    emptyMap()
                else ->
                    bagsPattern.findAll(rule)
                        .map { it.groupValues[2] to it.groupValues[1].toInt() }
                        .toMap()
            }
            Rule(container.value, bags)
        }
    }

    println(findPossibleContainerBags(rules, "shiny gold").size)
    println(countContainedBags(rules, "shiny gold"))
}

private fun findPossibleContainerBags(rules: List<Rule>, color: String): Set<String> {
    val possibleColors = rules.filter { it.bags.containsKey(color) }.map { it.containerColor }.toSet()
    return possibleColors + possibleColors.flatMap { findPossibleContainerBags(rules, it) }
}

private fun countContainedBags(rules: List<Rule>, color: String): Int {
    return rules.find { it.containerColor == color }
        ?.let { rule ->
            rule.bags.values.sum() +
                    rule.bags.map { (innerColor, amount) -> amount * countContainedBags(rules, innerColor) }.sum()
        }
        ?: 0
}

private data class Rule(
    val containerColor: String,
    val bags: Map<String, Int>
)