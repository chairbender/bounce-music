package com.chairbender.bounce.musicTheory

/**
 * A key is defined by a tonal pitch class and a mode (major or minor), and from those the
 * diatonic degrees (I-VII) can be defined.
 */
class Key(root: TonalPitchClass, val mode: Mode) {
    val pitches = generatePitches(root, mode)

    private fun generatePitches(root: TonalPitchClass, mode: Mode): List<TonalPitchClass> {
        if (mode == Mode.MAJOR) {
            return DiatonicCollection(root).pitches
        } else {
            //construct the minor from the relative major
            return emptyList()
        }

    }

    /**
     * @return the degrees of this Key, I-VII
     */
    fun degrees(): List<TonalPitchClass> {
        //TODO: implement
        return emptyList()
    }

}