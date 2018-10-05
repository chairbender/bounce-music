package com.chairbender.bounce.model

import com.chairbender.bounce.Drawable
import com.chairbender.bounce.Physical
import com.chairbender.bounce.box2d
import com.chairbender.bounce.pixels
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.dynamics.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

const val BASE_VELOCITY = 5.0

/**
 * The thing that bounces around, a circle. Initially it is drawn and is not part of the physics simulation.
 * Once it is finished, it is added to the physics simulation and launched.
 */
class Bouncer(center: Vector2, radius: Float, world: World) : Physical(world), Drawable {


    val bd = BodyDef()
    val cs = CircleShape()
    val fd = FixtureDef()
    var body: Body? = null

    init {
        bd.position.set(center.box2d())
        bd.type = BodyType.DYNAMIC

        cs.radius = radius

        fd.shape = cs
        fd.density = .5f
        fd.restitution = 1.0f
        fd.friction = .0f
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.RED
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 1.0

        if (body != null) {
            drawer.circle(body!!.position.pixels(), pixels(cs.radius))
        } else {
            drawer.circle(bd.position.pixels(), pixels(cs.radius))
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
    }
}