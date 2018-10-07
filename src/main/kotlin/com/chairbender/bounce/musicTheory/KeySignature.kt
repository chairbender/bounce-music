package com.chairbender.bounce.musicTheory

private val CIRCLE_OF_FLATS = listOf(
    TonalPitchClass("B"),
    TonalPitchClass("E"),
    TonalPitchClass("A"),
    TonalPitchClass("D"),
    TonalPitchClass("G"),
    TonalPitchClass("C"),
    TonalPitchClass("F")
)

private val CIRCLE_OF_SHARPS = listOf(
    TonalPitchClass("F"),
    TonalPitchClass("C"),
    TonalPitchClass("G"),
    TonalPitchClass("D"),
    TonalPitchClass("A"),
    TonalPitchClass("E"),
    TonalPitchClass("B")
)

private val C_NATURAL = TonalPitchClass("C")
private val F_NATURAL = TonalPitchClass("F")

/**
 * Represents a key signature in music theory, which has a name defined by a tonal pitch class. All
 * key signatures are defined in terms of the major key.
 * and which has zero or more accidentals, following the appropriate circle of flats / sharps.
 *
 * For theoretical key signatures, like G#,
 */
class KeySignature(majorRoot: TonalPitchClass) {
    /**
     * The accidentals of the key signature, as tonal pitch classes.
     *
     * For theoretical key signatures, like G#, the accidental's tonal pitch class will have an appropriate
     * accidental. For example, the key of G's first and only
     * sharp would be just F. The key of G#'s first accidental would be F#.
     */
    private val hasFlats = majorRoot == F_NATURAL || majorRoot.semiAlterations < 0
    val accidentals: List<TonalPitchClass> = generateAccidentals(majorRoot)
    private val naturalSemitoneMap: Map<TonalPitchClass,Int> =
        accidentals.map { it.natural() to it.semiAlterations }.toMap()

    private fun generateAccidentals(root: TonalPitchClass): List<TonalPitchClass> {
        val result = mutableListOf<TonalPitchClass>()

        if (root.equals(C_NATURAL)) {
            return result
        }

        //determine which circle to use
        val circle = if (hasFlats) CIRCLE_OF_FLATS else CIRCLE_OF_SHARPS

        //keep counting in the circle of flats until we reach our destination major key name
        var curTPC = circle[0]
        result.add(curTPC)
        var circleIndex = 0

        while (!hasArrived(root, result, circleIndex)) {
            circleIndex++
            if (circleIndex > 6) {
                //we've looped, start increasing the number of accidentals in existing TPCs
                curTPC = if (hasFlats) {
                    result[circleIndex % 7].flatten()
                } else {
                    result[circleIndex % 7].sharpen()
                }
                result[circleIndex % 7] = curTPC
            } else {
                curTPC = circle[circleIndex]
                result.add(curTPC)
            }
        }

        return result
    }

    private fun hasArrived(root: TonalPitchClass, result: List<TonalPitchClass>, index: Int): Boolean {
        if (hasFlats) {
            //check for F, the special case
            if (root == F_NATURAL) {
                return result.size == 1
            }
            //flat key is defined by one back from the last flat accidental.
            return if (result.size < 2) {
                false
            } else {
                //TODO: FIx
                val prevAccidental = result[(index - 1) % 7].flatten()
                prevAccidental == root
            }
        } else {
            //sharp key is defined by one up from the last sharp accidental
            if (result.isEmpty()) {
                return false
            }
            var oneUp = result[index % 7].nextLetter()

            //if there are already 6 or 7 accidentals, then the oneUp will already
            //have an accidental in the signature, so we need to sharpen oneUp for comparison
            if (index % 7 == 5 || index % 7 == 6) {
                oneUp = oneUp.sharpen()
            }

            return oneUp == root
        }
    }

    /**
     * @param natural a TPC representing a natural (with no accidentals)
     *
     * @return a TPC created by applying the relevant accidentals in the key signature to this natural
     */
    fun applyTo(natural: TonalPitchClass): TonalPitchClass {
        if (!naturalSemitoneMap.contains(natural)) {
            return natural
        } else {
            val mod = if (hasFlats) -1 else 1
            return natural.applyAccidentals(naturalSemitoneMap[natural]!! + mod)
        }

    }

    constructor(rootName: String) : this(TonalPitchClass(rootName))
}