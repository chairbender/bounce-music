package com.chairbender.bounce.model

import com.chairbender.bounce.Director
import com.chairbender.bounce.musicTheory.Note
import com.jsyn.JSyn
import com.jsyn.unitgen.*

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
    val drone = PulseOscillatorBL()

    fun createBody(): AudioBody {
        //TODO: Output to a compressor which tries to keep the volume
        //of the audiobodies up to a certain level so they don't get too quiet during sparse sections
        val body = AudioBody(lineOut.input, synth, this, director)
        bodies.add(body)

        bodies.forEach { it.setVolume(1.0 / bodies.size) }

        return body
    }

    init {
        synth.start()
        synth.add(lineOut)
        lineOut.start()

        //set up and start the drone
        val filterOsc = SineOscillator()
        val droneFilter = FilterLowPass()
        val adder = Add()
        synth.add(filterOsc)
        synth.add(drone)
        synth.add(droneFilter)
        synth.add(adder)
        val pan = Pan()
        synth.add(pan)
        adder.inputB.set(300.0)
        filterOsc.output.connect(adder.inputA)
        filterOsc.frequency.set(0.1)
        filterOsc.amplitude.set(100.0)
        adder.output.connect(droneFilter.frequency)
        drone.output.connect(droneFilter.input)
        drone.frequency.set(director.scale[0].hz())
        drone.amplitude.set(0.25)

        droneFilter.output.connect(pan.input)
        droneFilter.output.connect(0, lineOut.input, 1)

        pan.pan.set(0.0)
        pan.output.connect(0, lineOut.input, 0)
        pan.output.connect(1, lineOut.input, 1)
    }

}