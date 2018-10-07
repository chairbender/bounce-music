package com.chairbender.bounce.musicTheory

import org.junit.Assert.*
import org.junit.Test

class NoteTest {

    @Test
    fun createNote() {
        val note = Note("Abbb-11")

        assertEquals('A', note.letter)
        assertEquals(-3, note.semiAlterations)
        assertEquals(-11, note.octave)

        val note2 = Note("g##12")

        assertEquals('G', note2.letter)
        assertEquals(2, note2.semiAlterations)
        assertEquals(12, note2.octave)
    }

    @Test
    fun semitones() {
        assertEquals(0, Note("C-1").semitones())
        assertEquals(1, Note("C#-1").semitones())
        assertEquals(2, Note("D-1").semitones())
        assertEquals(-1, Note("Cb-1").semitones())
        assertEquals(12, Note("C0").semitones())
        assertEquals(14, Note("D0").semitones())
        assertEquals(24, Note("C1").semitones())
        assertEquals(28, Note("D##1").semitones())
    }
}