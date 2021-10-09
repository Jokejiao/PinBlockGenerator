package com.iso.pinblockgenerator

import com.iso.pinblockgenerator.domain.PinBlockUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Local unit test of the pin block generation algorithm
 */
@ExperimentalCoroutinesApi
class PinBlockUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    fun generatePinBlock() {
        testDispatcher.runBlockingTest {
            val useCase = PinBlockUseCase(testDispatcher)

            var pinBlock = useCase(PIN_EVEN)
            assertTrue(pinBlock.startsWith("341226"))

            pinBlock = useCase(PIN_ODD)
            assertTrue(pinBlock.startsWith("3512267"))
        }

        testDispatcher.cleanupTestCoroutines()
    }

    companion object {
        private const val PIN_EVEN = "1234"
        private const val PIN_ODD = "12345"
    }
}