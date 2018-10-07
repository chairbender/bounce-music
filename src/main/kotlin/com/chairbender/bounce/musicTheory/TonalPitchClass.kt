package com.chairbender.bounce.musicTheory

//semitone distance from A for each natural TPC
private val LETTER_SEMITONES = hashMapOf('A' to 0, 'B' to 2, 'C' to 3, 'D' to 5, 'E' to 7, 'F' to 8, 'G' to 10)

/**
 * Tonal Pitch classes are represented by strings of the form (naturalnote)(alterations),
 * where naturalnote is a-g or A-G and alterations is either any number of flats (b) or any number of sharps (#)
*We use the word tonal to describe these pitch classes to indicate that enharmonic equivalence is not assumed,
*unlike with standard pitch classes.
 * @param letter - letter of the TPC
 * @param semiAlterations - semitone alterations due to sharps or flats. E.g. if no sharp or flat, 0. If
 *      one sharp, 1. If 2 sharps, 2. If 1 flat, -1, etc...
 */
open class TonalPitchClass(val letter: Char, val semiAlterations: Int) {

    /**
     * @param other other pitch class to compare to
     * @return an integer indicating the offset in semitones of other from this, if they were notes
     *      in the same octave. Distance will be positive if other is greater than this, otherwise negative. Think
     *      about it as the number of semitones we would need to "walk" to get from this to other.
     */
    fun semitoneDistance(other: TonalPitchClass): Int {
        var sum = other.semiAlterations - this.semiAlterations
        return sum + (LETTER_SEMITONES[other.letter]!! - LETTER_SEMITONES[this.letter]!!)
    }

    /**
     * @param tpc - tpc like (natural note)(alterations), i.e. Ab, G#, etc...octave numbers are permitted
     *  at the end but will be ignored
     */
    constructor(tpc: String): this(tpc[0].toUpperCase(),
        tpc.filter { s -> s == '#' }.count() - tpc.filter { s -> s == 'b' }.count())
}