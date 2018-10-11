package com.chairbender.bounce.musicTheory

/**
 * A key is defined by a tonal pitch class and a mode (major or minor), and from those the
 * diatonic degrees (I-VII) can be defined.
 *
 * @param root tonic of the key
 * @param mode type of tonic triad that the key should have
 */
class Key(root: TonalPitchClass, val mode: Mode) {
    val pitches = generatePitches(root, mode)

    private fun generatePitches(root: TonalPitchClass, mode: Mode): List<TonalPitchClass> {
        if (mode == Mode.MAJOR) {
            return DiatonicCollection(root).pitches
        } else {
            //construct the minor from the relative major
            //the relative major is a minor third above the tonic
            val relativeMajorPitches: List<TonalPitchClass> =
                DiatonicCollection(Interval("m3").upFrom(Note(root,3)).asTonalPitchClass()).pitches

            //the minor starts on the VI of the relative major
            return listOf(relativeMajorPitches[5], relativeMajorPitches[6],
                relativeMajorPitches[0], relativeMajorPitches[1], relativeMajorPitches[2],
                relativeMajorPitches[3], relativeMajorPitches[4])
        }
    }

    /**
     * @param startOctave octave to start the scale on
     * @param numOctaves number of octaves to generate
     * @param scaleMode if this is a minor key, type of minor scale to use
     */
    fun scale(startOctave: Int, numOctaves: Int = 1, scaleMode: ScaleMode = ScaleMode.NATURAL): List<Note> {
        val result = mutableListOf<Note>()
        val switchLetter = if (pitches[0].letter == 'A') 7 else 'G' - pitches[0].letter
        for (i in startOctave..(numOctaves + startOctave - 1)) {
            result.addAll(
                pitches
                    .asSequence()
                    .mapIndexed { index, tonalPitchClass ->
                        when {
                            scaleMode == ScaleMode.HARMONIC && index == 6 -> tonalPitchClass.sharpen()
                            scaleMode == ScaleMode.MELODIC_ASCENDING && (index == 5 || index == 6) -> tonalPitchClass.sharpen()
                            else -> tonalPitchClass
                        }
                    }
                    .mapIndexed { index, tonalPitchClass ->
                        if (index > switchLetter) Note(tonalPitchClass,i + 1)
                        else Note(tonalPitchClass, i)
                    }
                    .toList()
            )
        }

        return result
    }

    /**
     * @param root string representation of the tonal pitch class
     * @param mode type of tonic triad the key should have
     */
    constructor(root: String, mode: Mode) : this(TonalPitchClass(root), mode)

}