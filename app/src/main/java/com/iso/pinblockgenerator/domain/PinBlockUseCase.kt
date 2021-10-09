package com.iso.pinblockgenerator.domain

import com.iso.pinblockgenerator.setHiNibbleValue
import com.iso.pinblockgenerator.setLowNibbleValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.experimental.or
import kotlin.experimental.xor

class PinBlockUseCase @Inject constructor() {

    suspend operator fun invoke(pin: String): String = withContext(Dispatchers.Default) {
        val pinNibbles = convertPinToNibbles(pin)
        val panNibbles = convertPanToNibbles()

        val pinBlockString = StringBuilder()

        for (i in pinNibbles.indices) {
            pinBlockString.append(String.format("%02X", pinNibbles[i] xor panNibbles[i]))
        }

        pinBlockString.toString()
    }

    private fun convertPinToNibbles(pin: String): ByteArray {
        val pinNibbles = ByteArray(BYTE_COUNT)
        val plainPinByteArray = pin.toByteArray()

        val i = plainPinByteArray.size / NIBBLE_COUNT_PER_BYTE
        val j = plainPinByteArray.size % NIBBLE_COUNT_PER_BYTE

        var plainPinByteIndex = 0
        var nibbledPinByteIndex = 0

        pinNibbles[nibbledPinByteIndex++] =
            setHiNibbleValue(ISO_3) or setLowNibbleValue(plainPinByteArray.size.toByte())

        while (nibbledPinByteIndex <= i) {
            pinNibbles[nibbledPinByteIndex++] =
                setHiNibbleValue(plainPinByteArray[plainPinByteIndex++]) or setLowNibbleValue(
                    plainPinByteArray[plainPinByteIndex++]
                )
        }

        if (j != 0) {
            pinNibbles[nibbledPinByteIndex++] =
                setHiNibbleValue(plainPinByteArray[plainPinByteIndex]) or
                        setLowNibbleValue(randomFill())
        }

        while (nibbledPinByteIndex < BYTE_COUNT) {
            pinNibbles[nibbledPinByteIndex++] = setHiNibbleValue(randomFill()) or
                    setLowNibbleValue(randomFill())

        }

        return pinNibbles
    }

    private fun convertPanToNibbles(): ByteArray {
        val panNibbles = ByteArray(BYTE_COUNT)
        var panNibbleIndex = 0
        var panByteIndex = PAN_NULL_BYTE_COUNT

        val excludeCheckDigitPan = PAN.substring(
            PAN.length - PAN_RIGHT_MOST_COUNT - PAN_CHECK_BIT_LEN, PAN.length - PAN_CHECK_BIT_LEN
        ).toByteArray()

        while (panByteIndex < BYTE_COUNT) {
            panNibbles[panByteIndex++] =
                setHiNibbleValue(excludeCheckDigitPan[panNibbleIndex++]) or setLowNibbleValue(
                    excludeCheckDigitPan[panNibbleIndex++]
                )
        }

        return panNibbles
    }

    private fun randomFill() = (RANDOM_MIN..RANDOM_MAX).random().toByte()


    companion object {
        private const val BYTE_COUNT = 8

        private const val ISO_3: Byte = 3
        private const val NIBBLE_COUNT_PER_BYTE = 2

//        private const val PAN = "1111222233334444"
        private const val PAN = "43219876543210987"
//        private const val PAN = "4234234456789012345"

        private const val PAN_CHECK_BIT_LEN = 1
        private const val PAN_RIGHT_MOST_COUNT = 12
        private const val PAN_NULL_BYTE_COUNT = 2

        private const val RANDOM_MIN = 0
        private const val RANDOM_MAX = 15
    }
}