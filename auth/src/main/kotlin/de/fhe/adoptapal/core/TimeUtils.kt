package de.fhe.adoptapal.core

class TimeUtils {
    enum class Unit {
        Seconds,
        Milliseconds,
    }

    companion object {
        fun convert(fromUnit: Unit, toUnit: Unit, value: Long): Long = when (fromUnit to toUnit) {
            Unit.Seconds to Unit.Milliseconds -> value * 1000
            Unit.Milliseconds to Unit.Seconds -> value / 1000
            else -> value
        }

        fun currentTime(unit: Unit): Long {
            val currentMilliseconds = System.currentTimeMillis()
            return convert(Unit.Milliseconds, unit, currentMilliseconds)
        }
    }
}