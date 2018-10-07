package com.chairbender.bounce.musicTheory

private val MIDDLE_C = Note("C-1")

private fun parseOctave(name: String): Int {
    val negative = name.contains('-')
    val octave = Integer.parseInt(name.filter { c -> c.isDigit() })

    return if (negative) -octave else octave
}

/**
 * Notes are tonal pitch classes with an octave
 *
 * Notes are named as follows
 *
 * (letter)(alterations)(octave)
 *
 * letter is a-g (or uppercase), alterations is any number of # or b or nothing, octave is an integer.
 *
 * E.g. Abb3 represents A double-flat in the third octave
 */
class Note(letter: Char, semiAlterations: Int, val octave: Int): TonalPitchClass(letter, semiAlterations) {



    /**
     * @return number of semitones above C1 (negative if below)
     */
    fun semitones(): Int {
        return MIDDLE_C.semitoneDistance(this) + (12 * (octave + 1))
    }

    /**
     * @return a note a half step higher, either by removing a flat or adding a sharp
     */
    fun raise(): Note {
        return Note(letter, semiAlterations + 1, octave)
    }

    /**
     * @return a note a half step lower, either by adding a flat or removing a sharp
     */
    fun lower(): Note {
        return Note(letter, semiAlterations - 1, octave)
    }

    /**
     * @return the hz frequency of this note
     */
    fun hz(): Double {
        return Math.pow(2.0, (semitones() - 69) / 12.0) * 440
    }

    fun asTonalPitchClass(): TonalPitchClass {
        return TonalPitchClass(letter, semiAlterations)
    }

    override fun toString(): String {
        return super.toString() + octave
    }


    constructor(tpc: TonalPitchClass, octave: Int) : this(tpc.letter, tpc.semiAlterations, octave)
    constructor(note: String) : this(TonalPitchClass(note), parseOctave(note))

}