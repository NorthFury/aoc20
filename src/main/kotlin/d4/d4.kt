package d4

import java.io.File

fun main() {
    val propertyPattern = Regex("""(\w+):([#\w]+)""")

    val lines = File("input/d4.input").readLines()
    val splitIndexes = listOf(-1) +
            lines.mapIndexedNotNull { i, s -> if (s.isBlank()) i else null } +
            listOf(lines.size)

    val passports = splitIndexes.windowed(2).map { (start, end) ->
        lines.subList(start + 1, end)
            .flatMap(propertyPattern::findAll)
            .map { it.groupValues[1] to it.groupValues[2] }
    }

    val mandatoryProperties = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    passports.filter { properties -> properties.map { it.first }.containsAll(mandatoryProperties) }
        .filter { properties ->
            properties.all { (key, value) ->
                when (key) {
                    "byr" -> isValidBirthYear(value)
                    "iyr" -> isValidIssueYear(value)
                    "eyr" -> isValidExpirationYear(value)
                    "hgt" -> isValidHeight(value)
                    "hcl" -> isValidHairColor(value)
                    "ecl" -> isValidEyeColor(value)
                    "pid" -> isValidPassportId(value)
                    else -> true
                }
            }
        }
        .count()
        .let(::println)
}

private val EYE_COLORS = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
private fun isValidEyeColor(value: String): Boolean = EYE_COLORS.contains(value)

private val HAIR_COLOR_PATTERN = Regex("#[0-9a-f]{6}")
private fun isValidHairColor(value: String): Boolean = HAIR_COLOR_PATTERN.matches(value)

private fun isValidPassportId(value: String): Boolean = value.length == 9 && value.all { it in '0'..'9' }

private fun isValidHeight(value: String): Boolean = when (value.takeLast(2)) {
    "cm" -> value.dropLast(2).toIntOrNull()
        ?.let { it in 150..193 }
        ?: false
    "in" -> value.dropLast(2).toIntOrNull()
        ?.let { it in 59..76 }
        ?: false
    else -> false
}

private fun isValidBirthYear(value: String): Boolean = isValidYear(value, 1920..2002)
private fun isValidIssueYear(value: String): Boolean = isValidYear(value, 2010..2020)
private fun isValidExpirationYear(value: String): Boolean = isValidYear(value, 2020..2030)

private fun isValidYear(value: String, range: IntRange): Boolean {
    return value.toIntOrNull()
        ?.let { it in range }
        ?: false
}
