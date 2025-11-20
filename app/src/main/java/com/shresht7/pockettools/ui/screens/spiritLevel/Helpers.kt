package com.shresht7.pockettools.ui.screens.spiritLevel

/**
 * Maps the tilt angle of the device to a visual offset for the spirit level bubble.
 *
 * This function takes a tilt angle (in degrees) and calculates a corresponding
 * offset value. The offset is constrained within a specified range to ensure the
 * bubble doesn't move off-screen.
 *
 * @param angle The tilt angle of the device in degrees.
 * @param maxOffset The maximum absolute offset value. The bubble will not move beyond
 *                  -maxOffset or +maxOffset.
 * @return The calculated offset as a [Float], constrained between -[maxOffset] and [maxOffset].
 */
fun mapTiltToOffset(angle: Float, maxOffset: Float): Float {
    return (angle / 45f).coerceIn(-1f, 1f) * maxOffset
}