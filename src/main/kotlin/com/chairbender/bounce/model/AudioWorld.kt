package com.chairbender.bounce.model

import com.chairbender.bounce.Director
import com.chairbender.bounce.musicTheory.Note
import com.jsyn.JSyn
import com.jsyn.unitgen.LineOut

/**
 * Handles audio
 *
 * Central point of control for everything that makes sound.
 * Nothing makes sound without going through this. Tracks all bouncers as a list of AudioBody.
 *
 *  Objects  that want to produce audio can generate an AudioBody within this by invoking createBody,
 *  and can then use the returned AudioBody to create audio.
 *
 * Allows us to apply various effects and
 * modify tonal behavior of bouncers, all centrally.
 *
 * Audio synthesis starts as soon as this is instantiated
 */
class AudioWorld(private val director: Director) {
    val bodies = mutableListOf<AudioBody>()
    val synth = JSyn.createSynthesizer()
    val lineOut = LineOut()

    fun createBody(): AudioBody {
        val body = AudioBody(lineOut.input, synth, this, director)
        bodies.add(body)

        bodies.forEach { it.setVolume(1.0 / bodies.size) }

        return body
    }

    init {
        synth.start()
        synth.add(lineOut)
        lineOut.start()
    }

}