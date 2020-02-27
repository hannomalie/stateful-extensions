package de.hanno.mixins

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class ExtensionStateTest {
    class Foo

    var Foo.bar: String by extensionState("defaultString")
    var Foo.baz: Int by extensionState(5)

    @Test
    fun testState() {
        val firstFoo = Foo()
        val secondFoo = Foo()
        assertThat(firstFoo.bar).isEqualTo("defaultString")
        assertThat(firstFoo.baz).isEqualTo(5)

        secondFoo.bar = "secondFooBar"
        secondFoo.baz = 10

        assertThat(firstFoo.bar).isEqualTo("defaultString")
        assertThat(secondFoo.bar).isEqualTo("secondFooBar")
        assertThat(firstFoo.baz).isEqualTo(5)
        assertThat(secondFoo.baz).isEqualTo(10)
    }

    var Foo.random: Float by extensionState(0f)
    @Test
    fun setUnderPressure() {

        val randomizedFoos = (0..20000).map {
            Foo().apply {
                random = Random.nextFloat()
            }
        }.map {
            it.random *= 2f
            assertThat(it.random).isNotNull()
        }


    }

}