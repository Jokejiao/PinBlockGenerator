package com.iso.pinblockgenerator.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.experimental.or

class PinBlockUseCase @Inject constructor() {

    suspend operator fun invoke(pin: String) : String = withContext(Dispatchers.Default) {

        ""
    }

    private suspend fun convertPinToNibbles(pin: String): ByteArray {
        val pinNibbles = ByteArray(PIN_BYTE_COUNT)
        val plainByteArray = pin.toByteArray()

        pinNibbles[0] = setHiNibbleValue(ISO_3) or setLowNibbleValue(plainByteArray.size.toByte())



        return pinNibbles
    }

    private fun setHiNibbleValue(value: Byte): Byte = (0xF0 and (value.toInt() shl 4)).toByte()

    private fun setLowNibbleValue(value: Byte): Byte = (0x0F and value.toInt()).toByte()

    companion object {
        private const val PIN_BYTE_COUNT = 8
        private const val ISO_3: Byte = 3
    }
}