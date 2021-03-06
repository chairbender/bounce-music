package com.chairbender.bounce.musicTheory

private val F_NATURAL = TonalPitchClass("F")

/**
 * A diatonic collection is a collection of seven tonal pitch classes starting on a given
 * pitch class with the following pattern of intervals from the root note:
 * whole whol half whole whole whol half. A diatonic collection starting on some
 * note has the same pitches as a major scale starting on that note
 *
 * Where two pitch classes are equivalent (like D# and Eb), the
 *pitch class chosen will be the one whose accidental appears in the key
 *signature that describes that diatonic collection. For the special case of F,
 *the flat representation will be used (since we could use sharps or flats
 *to get the same diatonic collection, but there are fewer accidentals in the flat version
 *which is preferrable). One way to think about it is -
 *the key signature for a given starting note is equivalent to the key signature for the
 *major scale starting on that note.
 */
class DiatonicCollection(tonic: TonalPitchClass) {
    private val keySignature = KeySignature(tonic)
    val pitches: List<TonalPitchClass> = generatePitches(tonic)


    private fun generatePitches(tonic: TonalPitchClass): List<TonalPitchClass> {
        val result = mutableListOf<TonalPitchClass>()

        var currentNote = tonic
        result.add(currentNote)
        for (i in 1..6) {
            currentNote = keySignature.applyTo(currentNote.nextNatural())
            result.add(currentNote)
        }

        return result
    }

    /**
     * @param tonic string tonal pitch class to use as the tonic
     */
    constructor(tonic: String) : this(TonalPitchClass(tonic))
}