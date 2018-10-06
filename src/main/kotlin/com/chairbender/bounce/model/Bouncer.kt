package com.chairbender.bounce.model

import com.chairbender.bounce.*
import com.jsyn.Synthesizer
import com.jsyn.ports.UnitInputPort
import com.jsyn.unitgen.*
import com.softsynth.shared.time.TimeStamp
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.dynamics.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import org.openrndr.shape.Circle

const val BASE_VELOCITY = 5.0

/**
 * The thing that bounces around, a circle. Initially it is drawn and is not part of the physics simulation.
 * Once it is finished, it is added to the physics simulation and launched.
 */
class Bouncer(center: Vector2, radius: Float, private val audioOut: UnitInputPort,
              private val synth: Synthesizer, world: World) : Physical(world), Circular {
    private val bd = BodyDef()
    private val cs = CircleShape()
    private val fd = FixtureDef()
    private var body: Body? = null

    private val pan = Pan()
    private val adsr = EnvelopeAttackDecay()
    private val pulse = PulseOscillator()

    init {
        bd.position.set(center.box2d())
        bd.type = BodyType.DYNAMIC

        cs.radius = radius

        fd.shape = cs
        fd.density = .5f
        fd.restitution = 1.0f
        fd.friction = .0f
        fd.userData = this

        //hook up the ugen
        /*
        hadsr -> pulse -> pan -> output
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

    override fun circle(): Circle {
        if (body != null) {
            return Circle(body!!.position.pixels(), pixels(cs.radius))
        } else {
            return Circle(bd.position.pixels(), pixels(cs.radius))
        }

    }

    /**
     * Expand this bouncer's radius so that it touches touch
     *
     * @param touch position to expand to
     */
    fun expandTo(touch: Vector2) {
        if (body != null) {
            throw Exception("body is already launched!")
        }
        cs.radius = (bd.position.sub(touch.box2d())).length()
    }

    /**
     * Finish drawing the bouncer, add it to the physics simulation, and launch it away from the mouse.
     *
     * @param from position away from which bouncer will be sent at a velocity based on its speed
     */
    fun launch(from: Vector2) {
        if (body != null) {
            throw Exception("body is already launched")
        }
        val dir = bd.position.sub(from.box2d())
        dir.normalize()
        val amount = BASE_VELOCITY
        bd.linearVelocity = dir.mul(amount.toFloat())

        body = world.createBody(bd)
        body!!.createFixture(fd)

        pulse.frequency.set(220.0 + cs.radius*100)
        pulse.amplitude.set(0.6)
        val panAmount = (body!!.position.x / box2d(WINDOW_WIDTH.toDouble())).toDouble()
        pan.pan.set((panAmount - 0.5)*2)

        playNote()
    }

    private fun playNote() {
        adsr.input.set(1.0)
        adsr.input.set(0.0,synth.createTimeStamp().makeRelative(0.5))
    }

    /**
     * Invoke when a bounce has occurred, causes it to play its sound
     */
    fun bounce() {
        playNote()
    }
}