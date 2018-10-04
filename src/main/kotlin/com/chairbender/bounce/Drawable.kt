package com.chairbender.bounce

import org.openrndr.Program
import org.openrndr.draw.Drawer

/**
 * Something which can be drawn
 */
interface Drawable {

    /**
     * Draw this object
     *
     * @param drawer drawer to use to draw
     */
    fun draw(drawer: Drawer)
}