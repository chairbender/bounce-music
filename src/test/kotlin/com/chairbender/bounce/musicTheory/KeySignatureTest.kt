package com.chairbender.bounce.musicTheory

import org.junit.Assert.*
import org.junit.Test

class KeySignatureTest {

    @Test
    fun createsFromRoot() {
        val c = KeySignature("C")
        assertTrue(c.accidentals.isEmpty())

        val fSharp = KeySignature("F#")
        assertEquals(TonalPitchClass("F"), fSharp.accidentals[0])
        assertEquals(TonalPitchClass("G"), fSharp.accidentals[2])
        assertEquals(TonalPitchClass("E"), fSharp.accidentals[5])

        val gFlat = KeySignature("Gb")
        assertEquals(TonalPitchClass("B"), gFlat.accidentals[0])
        assertEquals(TonalPitchClass("C"), gFlat.accidentals[5])

        val aSharp = KeySignature("A#")
        assertEquals(TonalPitchClass("F#"), aSharp.accidentals[0])
        assertEquals(TonalPitchClass("D"), aSharp.accidentals[3])

        //TODO: Fix
        val fFlat = KeySignature("Fb")
        assertEquals(TonalPitchClass("Bb"), fFlat.accidentals[0])
        assertEquals(TonalPitchClass("F"), fFlat.accidentals[6])
    }
}