package com.chairbender.bounce

import org.openrndr.shape.Circle

/**
 * Represents an object that is drawn as a circle, so batching can be performed.
 */
interface Circular {
    fun circle(): Circle
}