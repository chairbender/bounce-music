package com.chairbender.bounce

import com.chairbender.bounce.musicTheory.Key
import com.chairbender.bounce.musicTheory.Mode
import com.chairbender.bounce.musicTheory.Note
import com.chairbender.bounce.musicTheory.ScaleMode
import java.util.*

/**
 * Controls the flow of the entire simulation, acting as a "director" and modulating things to
 * make it interesting.
 *
 * Basically, any "stylistic" or "artistic" decisions should come from the director
 */
class Director {
    private val rand = Random()

    //by adjusting these you can adjust the Director's overall behavior
    private val scaleOctaves = 4
    private val scaleStartOctave = 1

    /**
     * Current key
     */
    var key = randomKey()
    /**
     * Current mode of the scale
     */
    var scaleMode = ScaleMode.values().random(rand)!!
    /**
     * List of notes to draw from based on current key
     */
    val scale: List<Note>
        get() = key.scale(scaleStartOctave, scaleOctaves, scaleMode)

    private fun randomKey(): Key {
        //pick randomly A-G
        val letter = 'A' + rand.nextInt(7)
        //randomly pick sharp, natural, or flat
        val sign = listOf("b","#","").random(rand)
        //randomly pick major or minor
        val mode: Mode = Mode.values().random(rand)!!

        return Key(sign + letter, mode)
    }
}

fun <E> List<E>.random(random: java.util.Random): E? = if (size > 0) get(random.nextInt(size)) else null
fun <E> Array<E>.random(random: java.util.Random): E? = if (size > 0) get(random.nextInt(size)) else null