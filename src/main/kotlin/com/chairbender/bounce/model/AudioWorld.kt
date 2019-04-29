package com.chairbender.bounce.model

import com.chairbender.bounce.Director
import net.beadsproject.beads.core.AudioContext
import net.beadsproject.beads.data.Buffer
import net.beadsproject.beads.ugens.Add
import net.beadsproject.beads.ugens.LPRezFilter
import net.beadsproject.beads.ugens.WavePlayer

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
    val audioContext = AudioContext()
    val drone = WavePlayer(audioContext, 440.0f, Buffer.SQUARE)

    fun createBody(): AudioBody {
        //TODO: Output to a compressor which tries to keep the volume
        //of the audiobodies up to a certain level so they don't get too quiet during sparse sections
        val body = AudioBody(audioContext.audioInput, audioContext, this, director)
        bodies.add(body)

        bodies.forEach { it.setVolume(1.0 / bodies.size) }

        return body
    }

    init {
        audioContext.start()

        //set up and start the drone
        val filterOsc = WavePlayer(audioContext, 0.1f, Buffer.SINE)
        val adder = Add(audioContext, 300, filterOsc)
        val droneFilter = LPRezFilter(audioContext, adder, 0.2f)

        droneFilter.addInput(drone)
        drone.frequency = director.scale[0].hz().toFloat()
        //TODO: Adjust drone volume
        //TODO: Spatialize / reveb / pan ?
        audioContext.audioInput.addInput(droneFilter)
    }

}