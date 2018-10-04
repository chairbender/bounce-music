package com.chairbender.bounce

import org.jbox2d.common.Vec2
import org.openrndr.math.Vector2

//we want it to scale so that the box2d simulation is around 10m width and height but matches the
// aspect ratio of the actual window. For that we need constant access to the program window to do our calculation
val PROGRAM = BounceMusic()

//we must scale from pixels to box2d ranges where it functions better.
//we assume the simulation aspect ratio == window aspect ratio

fun scale(): Double {
    return 10 / PROGRAM.width.toDouble()
}

/**
 * @param scalar value to scale from pixels to box2d meters
 */
fun box2d(pixels: Double): Float {
    return (scale() * pixels).toFloat()
}

/**
 * @return the vec2 representation of this vector, scaled from pixel units to box2d units.
 */
fun Vector2.box2d(): Vec2 {
    return Vec2((this.x * scale()).toFloat(), (this.y * scale()).toFloat())
}