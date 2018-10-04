package com.chairbender.bounce.model

import com.chairbender.bounce.Drawable
import com.chairbender.bounce.Physical
import com.chairbender.bounce.box2d
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.dynamics.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

/**
 * The thing that bounces around, a circle
 */
class Bouncer(var center: Vector2, var radius: Float, world: World) : Physical(world), Drawable {
    val velocitySizeRatio = 0.5
    val baseVelocity = 1.0
    val bd = BodyDef()
    val cs = CircleShape()
    val fd = FixtureDef()
    var body: Body? = null
    constructor(world: World) : this(Vector2.ZERO, 0.0f, world)

    init {
        //add to the physic simulation
        bd.position.set(center.box2d())
        bd.type = BodyType.DYNAMIC

        val cs = CircleShape()
        cs.m_radius = radius

        fd.shape = cs
        fd.density = .5f
        fd.restitution = 1.0f
        fd.friction = 1.0f

        body = world.createBody(bd)
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = ColorRGBa.RED
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 1.0

        // -- draw a circle with its center at (140,140) with radius 130
        drawer.circle(center, radius)
    }

    /**
     * Expand this bouncer's radius so that it touches touch
     *
     * @param touch position to expand to
     */
    fun expandTo(touch: Vector2) {
        radius = (center - touch).length.toFloat()
        cs.radius = radius
    }

    /**
     *
     * @param from position away from which bouncer will be sent at a velocity based on its speed
     */
    fun goFrom(from: Vector2) {
        if (body != null) {
            val dir = (center - from).normalized
            val amount = (baseVelocity * velocitySizeRatio * radius)
            body.linearVelocity = (dir * amount).box2d()
        }
    }
}