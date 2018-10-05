package com.chairbender.bounce

import com.chairbender.bounce.model.Bouncer
import org.jbox2d.collision.shapes.EdgeShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.configuration

class BounceMusic: Program() {
    val world = World(Vec2(0.0f, 0.0f))
    val bouncers = mutableListOf<Bouncer>()
    var nextBouncer: Bouncer? = null
    var drawingBouncer = false
    val UPS = 60
    val FPS = 60
    var running = true


    override fun setup() {
        mouse.buttonDown.listen {
            if (!drawingBouncer) {
                nextBouncer = Bouncer(mouse.position, 0.0f, world)
                drawingBouncer = true
            }
        }

        mouse.dragged.listen {
            nextBouncer!!.expandTo(mouse.position)
        }

        mouse.buttonUp.listen {
            if (drawingBouncer) {
                drawingBouncer = false
                nextBouncer!!.launch(mouse.position)
                bouncers.add(nextBouncer!!)
            }
        }

        window.sized.listen {
            WINDOW_WIDTH = it.size.x.toInt()
            WINDOW_HEIGHT = it.size.y.toInt()
        }

        //add the walls of the world
        createWalls()
    }

    private fun createWalls() {
        createWall(0f, 0f, box2d(width.toDouble()), 0f)
        createWall(0f, 0f, 0f, box2d(height.toDouble()))
        createWall(box2d(width.toDouble()), 0f, box2d(width.toDouble()), box2d(height.toDouble()))
        createWall(0f, box2d(height.toDouble()), box2d(width.toDouble()), box2d(height.toDouble()))



    }

    private fun createWall(x1: Float, y1: Float, x2: Float, y2: Float) {
        val wall = BodyDef()
        wall.position = Vec2(0f,0f)
        wall.type = BodyType.STATIC
        val edge = EdgeShape()
        edge.set(Vec2(x1,y1), Vec2(x2, y2))
        val fixture = FixtureDef()
        fixture.shape = edge
        fixture.friction = .0f
        fixture.restitution = 1.0f

        val body = world.createBody(wall)
        body.createFixture(fixture)
    }

    override fun draw() {
        world.step(1.0f / 60.0f, 6, 2)
        bouncers.forEach { bouncer -> bouncer.draw(drawer) }
        if (drawingBouncer) {
            nextBouncer!!.draw(drawer)
        }
    }
}

fun main(args: Array<String>) {
    application(
        BounceMusic(),
            configuration {
                width = WINDOW_WIDTH
                height = WINDOW_HEIGHT
            })
}