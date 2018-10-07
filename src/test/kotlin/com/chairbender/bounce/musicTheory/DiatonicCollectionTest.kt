package com.chairbender.bounce.musicTheory

import org.junit.Assert.*
import org.junit.Test

class DiatonicCollectionTest {

    @Test
    fun createsFromRoot() {
        var actual = DiatonicCollection("C").pitches
        var expected = listOf(
            TonalPitchClass("C"),
            TonalPitchClass("D"),
            TonalPitchClass("E"),
            TonalPitchClass("F"),
            TonalPitchClass("G"),
            TonalPitchClass("A"),
            TonalPitchClass("B")
        )
        assertEquals(expected, actual)

        actual = DiatonicCollection("G#").pitches
        expected = listOf(
            TonalPitchClass("G#"),
            TonalPitchClass("A#"),
            TonalPitchClass("B#"),
            TonalPitchClass("C#"),
            TonalPitchClass("D#"),
            TonalPitchClass("E#"),
            TonalPitchClass("F##")
        )
        assertEquals(expected, actual)

        actual = DiatonicCollection("Fb").pitches
        expected = listOf(
            TonalPitchClass("Fb"),
            TonalPitchClass("Gb"),
            TonalPitchClass("Ab"),
            TonalPitchClass("Bbb"),
            TonalPitchClass("Cb"),
            TonalPitchClass("Db"),
            TonalPitchClass("Eb")
        )
        assertEquals(expected, actual)
    }
}