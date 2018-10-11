package com.chairbender.bounce.model

import com.chairbender.bounce.Director
import com.chairbender.bounce.WINDOW_WIDTH
import com.chairbender.bounce.box2d
import com.chairbender.bounce.musicTheory.Note
import com.jsyn.Synthesizer
import com.jsyn.ports.UnitInputPort
import com.jsyn.ports.UnitOutputPort
import com.jsyn.unitgen.EnvelopeAttackDecay
import com.jsyn.unitgen.Pan
import com.jsyn.unitgen.PulseOscillator
import org.jbox2d.collision.shapes.CircleShape
import kotlin.math.roundToInt

/**
 * An audio-producing body within an audio world, through which bouncers can talk to AudioWorld to
 * create audio
 */
class AudioBody(private val audioOut: UnitInputPort, private val synth: Synthesizer, private val world: AudioWorld,
                private val director: Director) {
    private val pan = Pan()
    private val adsr = EnvelopeAttackDecay()
    private val pulse = PulseOscillator()

    init {
        //hook up the ugen
        /*
        hadsr -> pulse -> pan -> audioOut
         */
        synth.add(adsr)
        synth.add(pulse)
        synth.add(pan)
        adsr.output.connect(pulse.amplitude)
        pulse.output.connect(pan.input)
        pan.output.connect(0, audioOut, 0)
        pan.output.connect(1, audioOut, 1)

        adsr.attack.set(0.0)
        adsr.decay.set(1.0)
    }

    /**
     * Plays this audiobody's note
     *
     * @param radius radius of the bouncer, used to determine the pitch to play
     */
    fun playNote(radius: Float, xPos: Float) {
        //pick a note in the current scale
        val scale = director.scale

        val radiusRatio = 1 - ((radius - MIN_RADIUS) / (MAX_RADIUS - MIN_RADIUS))

        val scalePosition = (scale.size - 1) * radiusRatio
        val chosenNote = scale[scalePosition.roundToInt()]

        pulse.frequency.set(chosenNote.hz())
        val panAmount = (xPos / box2d(WINDOW_WIDTH.toDouble())).toDouble()
        pan.pan.set((panAmount - 0.5)*2)
        adsr.input.set(1.0)
        adsr.input.set(0.0,synth.createTimeStamp().makeRelative(0.01))
    }

}