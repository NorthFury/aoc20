package d19

import java.io.File

fun main() {
    val allLines = File("input/d19.input").readLines()
    val rules = allLines.takeWhile(String::isNotBlank)
        .map { line ->
            val id = line.takeWhile { it != ':' }
            id to line.drop(id.length + 2)
        }
        .toMap()

    val messages = allLines.dropWhile(String::isNotBlank).filter(String::isNotBlank)

    val ruleRegex = getRegexString(rules, "0").toRegex()
    messages.count(ruleRegex::matches).let(::println)

    val newRules = rules +
            extendToRulesWithoutLoops("8", "42 | 42 8", 5) +
            extendToRulesWithoutLoops("11", "42 31 | 42 11 31", 5)

    val newRuleRegex = getRegexString(newRules, "0").toRegex()
    messages.count(newRuleRegex::matches).let(::println)
}

private fun getRegexString(rules: Map<String, String>, ruleNumber: String): String {
    val rule = rules[ruleNumber] ?: throw Exception("up")
    if (rule.startsWith('"')) return rule.drop(1).dropLast(1)

    val parts = rule.split('|').map { part ->
        part.trim().split(' ').joinToString("") { getRegexString(rules, it) }
    }

    if (parts.size == 1) return parts.first()
    return parts.joinToString("|", "(", ")")
}


private fun extendToRulesWithoutLoops(id: String, rule: String, levels: Int): Map<String, String> {
    val parts = rule.split('|').map { part -> part.trim().split(' ') }

    if (levels == 0) return mapOf(
        id to parts.filter { !it.contains(id) }.joinToString(" | ") { it.joinToString(" ") }
    )

    val newId = "$id+"
    val newRule = parts.map { part -> part.map { if (it == id) newId else it } }
        .joinToString(" | ") { it.joinToString(" ") }
    return mapOf(id to newRule) + extendToRulesWithoutLoops(newId, newRule, levels - 1)
}
