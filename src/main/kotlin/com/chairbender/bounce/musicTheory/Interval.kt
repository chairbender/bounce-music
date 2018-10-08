package com.chairbender.bounce.musicTheory

val DIMINISHED_5TH = Interval("d5")
val MINOR_2ND = Interval("m2")
val MINOR_6TH = Interval("m6")
val MINOR_3RD = Interval("m3")
val MINOR_7TH = Interval("m7")
val PERFECT_4TH = Interval("P4")
val PERFECT_UNISON = Interval("P1")
val PERFECT_5TH = Interval("P5")
val MAJOR_2ND = Interval("M2")
val MAJOR_6TH = Interval("M6")
val MAJOR_3RD = Interval("M3")
val MAJOR_7TH = Interval("M7")
val AUGMENTED_4TH = Interval("A4")



private val F_NATURAL = TonalPitchClass("F")
private val C_NATURAL = TonalPitchClass("C")
private val G_NATURAL = TonalPitchClass("G")
private val D_NATURAL = TonalPitchClass("D")
private val A_NATURAL = TonalPitchClass("A")
private val E_NATURAL = TonalPitchClass("E")
private val B_NATURAL = TonalPitchClass("B")


private val CIRCLE_OF_FIFTHS = listOf(
    TonalPitchClass("F"),
    TonalPitchClass("C"),
    TonalPitchClass("G"),
    TonalPitchClass("D"),
    TonalPitchClass("A"),
    TonalPitchClass("E"),
    TonalPitchClass("B")
    )

private val INTERVAL_PROGRESSION = listOf(4, 1, 5, 2, 6, 3, 7)
private val NEXT_INTERVAL_MAP = INTERVAL_PROGRESSION.withIndex()
    .map {INTERVAL_PROGRESSION[it.index] to INTERVAL_PROGRESSION[(it.index+1) % 7]}.toMap()
private val PREV_INTERVAL_MAP = NEXT_INTERVAL_MAP.entries.associateBy({ it.value }){ it.key }


private val NEXT_FIFTH_MAP = CIRCLE_OF_FIFTHS.withIndex()
    .map {CIRCLE_OF_FIFTHS[it.index] to CIRCLE_OF_FIFTHS[(it.index+1) % 7]}.toMap()
private val PREV_FIFTH_MAP = NEXT_FIFTH_MAP.entries.associateBy({ it.value }){ it.key }

private val QUALITY_PROGRESSION = listOf(
    Quality.fromAbbreviation('d'),
    Quality.fromAbbreviation('m'),
    Quality.fromAbbreviation('P'),
    Quality.fromAbbreviation('M'),
    Quality.fromAbbreviation('A')
)
private val NEXT_QUALITY_MAP = QUALITY_PROGRESSION.withIndex()
    .map {QUALITY_PROGRESSION[it.index] to QUALITY_PROGRESSION[(it.index+1) % 5]}.toMap()
private val PREV_QUALITY_MAP = NEXT_QUALITY_MAP.entries.associateBy({ it.value }){ it.key }
/**
 * The interval naming convention is as follows <quality><number>,
* where quality is a (augmented) d (diminished) m (minor), P (perfect) or M (major).
* there can also be any number of a's or d's to indicate things like doubly
* or triply augmented/diminished intervals. number is any integer
* greater than or equal to 1.
 */
class Interval(val quality: List<Quality>, val number: Int) {

    /**
     * @param bottom note from which interval should be applied
     * @return the note which is this interval above bottom
     */
    fun upFrom(bottom: Note): Note {
        //reduce the distance so they are within an octave (we will add back in the octave later)
        val octaves = (number - 1) / 7
        val octaveInterval = Interval(quality, (number % 8) + octaves)

        //now walk in the line of fifths until we reach the target interval
        var curTPC = bottom.asTonalPitchClass()
        var curInterval = PERFECT_UNISON

        //figure out which direction to walk
        if (this == PERFECT_4TH || this.quality[0] == Quality.MINOR ||
                this.quality[0] == Quality.DIMINISHED) {
            //walk down
            while (curInterval != octaveInterval) {
                curTPC = prevCircleOfFifthsTPC(curTPC)
                curInterval = prevCircleOfFifthsInterval(curInterval)
            }

        } else {
            //walk up
            while (curInterval != octaveInterval) {
                curTPC = nextCircleOfFifthsTPC(curTPC)
                curInterval = nextCircleOfFifthsInterval(curInterval)
            }
        }

        return Note(curTPC, bottom.octave + octaves)
    }

    /**
     * @param top note from which interval should be applied
     * @return the note which is this interval below top
     */
    fun downFrom(top: Note): Note {
        //reduce the distance so they are within an octave (we will add back in the octave later)
        val octaves = (number - 1) / 7
        val octaveInterval = Interval(quality, (number % 8) + octaves)

        // to do the opposite of upFrom, simply
        // go in the circle of fifths until we encouter our interval, then traverse in the opposite direction
        //figure out which direction to walk
        var curInterval = Interval("P1")
        var steps = 0
        if (this == PERFECT_4TH || this.quality[0] == Quality.MINOR ||
            this.quality[0] == Quality.DIMINISHED) {
            //walk down
            while (curInterval != octaveInterval) {
                curInterval = prevCircleOfFifthsInterval(curInterval)
                steps--
            }

        } else {
            //walk up
            while (curInterval != octaveInterval) {
                curInterval = nextCircleOfFifthsInterval(curInterval)
                steps++
            }
        }

        //now we know the number of steps, so go that many steps in the opposite direction
        // on the TPC circle starting on
        var curTPC = top.asTonalPitchClass()
        for (i in 1..Math.abs(steps)) {
            if (steps < 0) {
                curTPC = nextCircleOfFifthsTPC(curTPC)
            } else {
                curTPC = prevCircleOfFifthsTPC(curTPC)
            }
        }

        return Note(curTPC, top.octave - octaves)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Interval

        if (quality != other.quality) return false
        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        var result = quality.hashCode()
        result = 31 * result + number
        return result
    }

    override fun toString(): String {
        return quality.map { it.abbreviation }.joinToString("") + number
    }

    /**
     * @param name interval name as string
     */
    constructor(name: String) : this(
        name.filter { !it.isDigit() }.map { Quality.fromAbbreviation(it) },
        Integer.parseInt(name.filter { it.isDigit() })
    )

    companion object {

        /**
         *
         * @param note first note
         * @param other second note
         *
         * @return the interval from the lower note to the higher note
         */
        fun between(note: Note, other: Note): Interval {
            //figure out lower / higher by comparing how they would look on a staff, not their pitch
            val lower = when {
                note.octave < other.octave -> note
                note.octave > other.octave -> other
                note.letter < other.letter -> note
                else -> other
            }
            val higher = if (note == lower) other else note

            //compare octaves by how they appear on the staff
            val octaveDistance = when {
                lower.letter == higher.letter -> higher.octave - lower.octave
                lower.octave == higher.octave -> 0
                else -> higher.octave - lower.octave - if (lower.letter < higher.letter) 0 else 1
            }

            val lowTPC = lower.asTonalPitchClass()
            val highTPC = higher.asTonalPitchClass()


            //walk along the circle of fifths from low until we encounter high
            //figure out which direction to walk
            var curTPC = lowTPC
            var curInterval = PERFECT_UNISON
            if (isLowerInCircle(lowTPC, highTPC)) {
                //count down
                while (highTPC != curTPC) {
                    curTPC = prevCircleOfFifthsTPC(curTPC)
                    curInterval = prevCircleOfFifthsInterval(curInterval)
                }
            } else {
                //count up
                while (highTPC != curTPC) {
                    curTPC = nextCircleOfFifthsTPC(curTPC)
                    curInterval = nextCircleOfFifthsInterval(curInterval)
                }
            }

            return Interval(curInterval.quality, curInterval.number + octaveDistance*7)
        }

        /**
         * return true iff otherTPC would be below TPC in the circle of fifths
         */
        private fun isLowerInCircle(tpc: TonalPitchClass, otherTPC: TonalPitchClass): Boolean {
            if (tpc.semiAlterations > otherTPC.semiAlterations) {
                return true
            } else if (tpc.semiAlterations == otherTPC.semiAlterations) {
                return CIRCLE_OF_FIFTHS.indexOf(tpc) > CIRCLE_OF_FIFTHS.indexOf(otherTPC)
            } else {
                return false
            }
        }

        private fun nextCircleOfFifthsInterval(curInterval: Interval): Interval {
            val nextNumber = NEXT_INTERVAL_MAP[curInterval.number]
            val nextQuality = getNextQuality(curInterval)

            return Interval(nextQuality, nextNumber!!)
        }

        private fun prevCircleOfFifthsInterval(curInterval: Interval): Interval {
            val nextNumber = PREV_INTERVAL_MAP[curInterval.number]
            val nextQuality = getPrevQuality(curInterval)

            return Interval(nextQuality, nextNumber!!)
        }

        private fun getNextQuality(curInterval: Interval): List<Quality> {
            if (curInterval == DIMINISHED_5TH ||
                curInterval == MINOR_2ND ||
                curInterval == MINOR_6TH ||
                curInterval == MINOR_3RD
            ) {
                return listOf(Quality.MINOR)
            } else if (curInterval == MINOR_7TH ||
                curInterval == PERFECT_4TH ||
                curInterval == PERFECT_UNISON
            ) {
                return listOf(Quality.PERFECT)
            } else if (curInterval == PERFECT_5TH ||
                curInterval == MAJOR_2ND ||
                curInterval == MAJOR_6TH ||
                curInterval == MAJOR_3RD
            ) {
                return listOf(Quality.MAJOR)
            } else if (curInterval == MAJOR_7TH) {
                return listOf(Quality.AUGMENTED)
            } else if (curInterval.quality[0] == Quality.DIMINISHED) {
                //drop a diminished on the transition from 5 to 2,
                //otherwise maintain
                return if (curInterval.number == 5) {
                    curInterval.quality.dropLast(1)
                } else {
                    curInterval.quality
                }
            } else {
                //augmented,
                //add an augmented on the transition from 7 to 4, otherwise maintain
                return if (curInterval.number == 7) {
                    curInterval.quality.plus(Quality.AUGMENTED)
                } else {
                    curInterval.quality
                }
            }
        }

        private fun getPrevQuality(curInterval: Interval): List<Quality> {
            if (curInterval == AUGMENTED_4TH ||
                curInterval == MAJOR_7TH ||
                curInterval == MAJOR_3RD ||
                curInterval == MAJOR_6TH) {
                return listOf(Quality.MAJOR)
            } else if (curInterval == MAJOR_2ND ||
                curInterval == PERFECT_5TH ||
                curInterval == PERFECT_UNISON) {
                return listOf(Quality.PERFECT)
            } else if (curInterval == PERFECT_4TH ||
                curInterval == MINOR_7TH ||
                curInterval == MINOR_3RD ||
                curInterval == MINOR_6TH) {
                return listOf(Quality.MINOR)
            } else if (curInterval == MINOR_2ND) {
                return listOf(Quality.DIMINISHED)
            } else if (curInterval.quality[0] == Quality.AUGMENTED) {
                //drop an augmented on the transition from 4 to 7,
                //otherwise maintain
                return if (curInterval.number == 4) {
                    curInterval.quality.dropLast(1)
                } else {
                    curInterval.quality
                }
            } else {
                //diminshed,
                //add a diminished on the transition from 2 to 5, otherwise maintain
                return if (curInterval.number == 2) {
                    curInterval.quality.plus(Quality.DIMINISHED)
                } else {
                    curInterval.quality
                }
            }
        }

        private fun nextCircleOfFifthsTPC(curTPC: TonalPitchClass): TonalPitchClass {
            //find the next natural in the circle
            val nextNatural = NEXT_FIFTH_MAP[curTPC.natural()]!!
            //have we wrapped around?
            val wrapIncrement = if (nextNatural == F_NATURAL) {
                1
            } else {
                0
            }

            //next note will be the next natural, preserving the alterations, but will
            // increment the alterations if we have wrapped around
            return TonalPitchClass(nextNatural.letter, curTPC.semiAlterations + wrapIncrement)
        }

        private fun prevCircleOfFifthsTPC(curTPC: TonalPitchClass): TonalPitchClass {
            //find the next natural in the circle
            val nextNatural = PREV_FIFTH_MAP[curTPC.natural()]!!
            //have we wrapped around?
            val wrapDecrement = if (nextNatural == B_NATURAL) {
                1
            } else {
                0
            }

            //next note will be the next natural, preserving the alterations, but will
            // increment the alterations if we have wrapped around
            return TonalPitchClass(nextNatural.letter, curTPC.semiAlterations - wrapDecrement)
        }

    }
}