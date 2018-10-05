package com.chairbender.bounce

import org.jbox2d.common.Vec2
import org.openrndr.math.Vector2

var WINDOW_WIDTH = 800
var WINDOW_HEIGHT = 600

//we must scale from pixels to box2d ranges where it functions better.
//we assume the simulation aspect ratio == window aspect ratio
fun scale(): Double {
    return 10 / WINDOW_WIDTH.toDouble()
}

/**
 * @param scalar value to scale from pixels to box2d meters
 */
fun box2d(pixels: Double): Float {
    return (scale() * pixels).toFloat()
}

/**
 * @param box2d measurement value to scale from box2d meters to pixel units
 */
fun pixels(box2d: Float): Double {
    return box2d / scale()
}

/**
 * @return the vec2 representation of this vector, scaled from pixel units to box2d units.
 */
fun Vector2.box2d(): Vec2 {
    return Vec2((this.x * scale()).toFloat(), (this.y * scale()).toFloat())
}

/**
 * @return the vector2 representation of this vec2, scaled from box2d units to pixel units.
 */
fun Vec2.pixels(): Vector2 {
    return Vector2((this.x / scale()), (this.y / scale()))
}