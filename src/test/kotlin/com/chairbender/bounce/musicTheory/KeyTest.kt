package com.chairbender.bounce.musicTheory

import org.junit.Assert.*
import org.junit.Test

class KeyTest {

    @Test
    fun create() {
        val cMajor = listOf(TonalPitchClass("C"), TonalPitchClass("D"),
            TonalPitchClass("E"), TonalPitchClass("F"), TonalPitchClass("G"),
            TonalPitchClass("A"), TonalPitchClass("B"))
        assertEquals(cMajor, Key("C", Mode.MAJOR).pitches)

        val aMinor = listOf(TonalPitchClass("A"), TonalPitchClass("B"),
            TonalPitchClass("C"), TonalPitchClass("D"), TonalPitchClass("E"),
            TonalPitchClass("F"), TonalPitchClass("G"))
        assertEquals(aMinor, Key("A", Mode.MINOR).pitches)

        val cSharpMinor = listOf(TonalPitchClass("C#"), TonalPitchClass("D#"),
            TonalPitchClass("E#"), TonalPitchClass("F#"), TonalPitchClass("G#"),
            TonalPitchClass("A#"), TonalPitchClass("B#"))
        assertEquals(cSharpMinor, Key("C#", Mode.MAJOR).pitches)
    }

    @Test
    fun scale() {
        val cMajor = listOf(
            Note("C4"),
            Note("D4"),
            Note("E4"),
            Note("F4"),
            Note("G4"),
            Note("A5"),
            Note("B5"),
            Note("C5"),
            Note("D5"),
            Note("E5"),
            Note("F5"),
            Note("G5"),
            Note("A6"),
            Note("B6")
        )
        assertEquals(cMajor, Key("C", Mode.MAJOR).scale(4,2))

        val aHarmonicMinor = listOf(
                Note("A4"),
                Note("B4"),
                Note("C4"),
                Note("D4"),
                Note("E4"),
                Note("F4"),
                Note("G4")
            )
        assertEquals(aHarmonicMinor, Key("A", Mode.MINOR).scale(4))

        val aMelodicMinor = listOf(
            Note("A4"),
            Note("B4"),
            Note("C4"),
            Note("D4"),
            Note("E4"),
            Note("F#4"),
            Note("G#4"),
            Note("A5"),
            Note("B5"),
            Note("C5"),
            Note("D5"),
            Note("E5"),
            Note("F#5"),
            Note("G#5")
        )
        assertEquals(aMelodicMinor, Key("A", Mode.MINOR).scale(4, 2, true))

    }
}
