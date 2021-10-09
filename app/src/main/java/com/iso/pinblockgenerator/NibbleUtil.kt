package com.iso.pinblockgenerator

fun setHiNibbleValue(value: Byte): Byte = (0xF0 and (value.toInt() shl 4)).toByte()

fun setLowNibbleValue(value: Byte): Byte = (0x0F and value.toInt()).toByte()