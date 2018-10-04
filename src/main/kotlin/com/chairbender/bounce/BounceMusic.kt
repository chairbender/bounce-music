package com.chairbender.bounce

import com.chairbender.bounce.model.Bouncer
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.configuration

class BounceMusic: Program() {
    val world = World(Vec2(0.0f, 0.0f))
    val bouncers = mutableListOf<Bouncer>()
    var nextBouncer: Bouncer = Bouncer(world)
    var drawingBouncer = false


    override fun setup() {
        mouse.buttonDown.listen {
            if (!drawingBouncer) {
                drawingBouncer = true
                nextBouncer.center = mouse.position
            }
        }

        mouse.dragged.listen {
            nextBouncer.expandTo(mouse.position)
        }

        mouse.buttonUp.listen {
            if (drawingBouncer) {
                drawingBouncer = false
                bouncers.add(nextBouncer)
                nextBouncer = Bouncer(world)
                nextBouncer.goFrom(mouse.position)
            }
        }
    }

    override fun draw() {
        bouncers.forEach { bouncer -> bouncer.draw(drawer) }

        if (drawingBouncer) {
            nextBouncer.draw(drawer)
        }
    }
}

fun main(args: Array<String>) {
    application(
        PROGRAM,
            configuration {
                width = 800
                height = 600
            })
}