package com.chairbender.bounce.model

import com.chairbender.bounce.*
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.dynamics.*
import org.openrndr.math.Vector2
import org.openrndr.shape.Circle

const val BASE_VELOCITY = 5.0
//min and max radius in box2d units
const val MAX_RADIUS = 1.0f
const val MIN_RADIUS = 0.1f

/**
 * The thing that bounces around, a circle. Initially it is drawn and is not part of the physics simulation.
 * Once it is finished, it is added to the physics simulation and launched.
 */
class Bouncer(center: Vector2, radius: Float, private val audioWorld: AudioWorld,
              world: World) : Physical(world), Circular {
    private val bd = BodyDef()
    private val cs = CircleShape()
    private val fd = FixtureDef()
    private var body: Body? = null
    private val audioBody = audioWorld.createBody()



    init {
        bd.position.set(center.box2d())
        bd.type = BodyType.DYNAMIC

        cs.radius = radius

        fd.shape = cs
        fd.density = .5f
        fd.restitution = 1.0f
        fd.friction = .0f
        fd.userData = this

    }

    override fun circle(): Circle {
        if (body != null) {
            return Circle(body!!.position.pixels(), pixels(cs.radius))
        } else {
            return Circle(bd.position.pixels(), pixels(cs.radius))
        }

    }

    /**
     * Expand or shrink this bouncer's radius so that it touches touch (not to
     * exceed MAX_RADIUS or to go below MIN_RADIUS
     *
     * @param touch position to expand to
     */
    fun expandOrShrinkTo(touch: Vector2) {
        if (body != null) {
            throw Exception("body is already launched!")
        }
        cs.radius = Math.min(MAX_RADIUS, Math.max(MIN_RADIUS, (bd.position.sub(touch.box2d())).length()))
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

        playNote()
    }

    private fun playNote() {
        audioBody.playNote(cs.radius, body!!.position.x)
    }

    /**
     * Invoke when a bounce has occurred, causes it to play its sound
     */
    fun bounce() {
        playNote()
    }
}