package me.pqpo.smartcropperlib

import android.graphics.*
import kotlin.math.*

fun getPointsDistance(p1: Point, p2: Point): Float {
    return getPointsDistance(p1.x.toFloat(), p1.y.toFloat(), p2.x.toFloat(), p2.y.toFloat())
}

fun getPointsDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    return sqrt((x1 - x2).pow(2F) + (y1 - y2).pow(2F))
}
