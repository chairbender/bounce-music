package com.chairbender.bounce.musicTheory

/**
 * A key is defined by a tonal pitch class and a mode (major or minor), and from those the
 * diatonic degrees (I-VII) can be defined.
 */
class Key(val tpc: TonalPitchClass, val mode: Mode) {

    /**
     * @return the degrees of this Key, I-VII
     */
    fun degrees(): List<TonalPitchClass> {
        //TODO: implement
        return emptyList()
    }

}