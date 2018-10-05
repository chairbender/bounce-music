package com.chairbender.bounce

import com.chairbender.bounce.model.Bouncer
import com.jsyn.JSyn
import com.jsyn.unitgen.LineOut
import com.jsyn.unitgen.SineOscillator
import org.jbox2d.collision.shapes.EdgeShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
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

        //test()
    }

    private fun test() {

        // Create a context for the synthesizer.
        val synth = JSyn.createSynthesizer()
        val osc = SineOscillator()
        val lineOut = LineOut()

        // Start synthesizer using default stereo output at 44100 Hz.
        synth.start()

        // Add a tone generator.
        synth.add(osc)
        // Add a stereo audio output unit.
        synth.add(lineOut)

        // Connect the oscillator to both channels of the output.
        osc.output.connect(0, lineOut.input, 0)
        osc.output.connect(0, lineOut.input, 1)

        // Set the frequency and amplitude for the sine wave.
        osc.frequency.set(345.0)
        osc.amplitude.set(0.6)

        // We only need to start the LineOut. It will pull data from the
        // oscillator.
        lineOut.start()
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
        drawer.fill = ColorRGBa.RED
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 1.0

        drawer.circles(bouncers.map(Bouncer::circle))

        if (drawingBouncer) {
            drawer.circle(nextBouncer!!.circle())
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