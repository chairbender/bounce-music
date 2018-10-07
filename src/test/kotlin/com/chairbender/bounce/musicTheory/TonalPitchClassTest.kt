package com.chairbender.bounce.musicTheory

import org.junit.Assert.*
import org.junit.Test

class TonalPitchClassTest {

    @Test
    fun getsSemitoneDistance() {
        assertEquals(2, TonalPitchClass("A").semitoneDistance(TonalPitchClass("B")))
        assertEquals(-2, TonalPitchClass("B").semitoneDistance(TonalPitchClass("A")))
        assertEquals(0, TonalPitchClass("C").semitoneDistance(TonalPitchClass("C")))
        assertEquals(2, TonalPitchClass("Cb").semitoneDistance(TonalPitchClass("C#")))
        assertEquals(3, TonalPitchClass("C").semitoneDistance(TonalPitchClass("D#")))
    }
}