package com.chairbender.bounce.musicTheory

/**
 * interval qualities
 */
enum class Quality(val abbreviation: Char) {
    DIMINISHED('d'), MINOR('m'),
    PERFECT('P'), MAJOR('M'),
    AUGMENTED('A');

    companion object {
        fun fromAbbreviation(abbreviation: Char): Quality {
            return Quality.values().find { it.abbreviation == abbreviation }!!
        }
    }
}
