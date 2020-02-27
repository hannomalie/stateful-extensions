This is a small experiment with Kotlin extension properties.
Those can normally not have any state, making them not much different than functions.
That also means, that any instance of a class has the same value for the extension property.
This limits extension properties a little bit.

[This documentation](https://discuss.kotlinlang.org/t/you-can-apparently-add-state-to-classes-with-extension-properties-with-almost-no-overhead-a-k-a-mixins-in-kotlin/15312/9)
lead me to the interesting idea to use the receiver reference as a key to fetch and safe desired state of
an extension property from and into a context. The discussion contains a more advanced implementation
with an abstraction over Java's weak references, whereas my implementation is a little bit simpler and
implements default values better iirc. My context is a simple weak hash map per property and the usage can
be seen in one of the tests, which looks like

```kotlin
class Foo

var Foo.bar: String by extensionState("defaultString")
var Foo.baz: Int by extensionState(5)

@Test
fun testState() {
    val firstFoo = Foo()
    val secondFoo = Foo()
    assertThat(firstFoo.bar).isEqualTo("defaultString") // <-- extension property get
    assertThat(firstFoo.baz).isEqualTo(5)

    secondFoo.bar = "secondFooBar" // <-- extension property set
    secondFoo.baz = 10

    assertThat(firstFoo.bar).isEqualTo("defaultString") // <-- extension property get still the same as before
    assertThat(secondFoo.bar).isEqualTo("secondFooBar") // <-- extension property get changed because of calling set
    assertThat(firstFoo.baz).isEqualTo(5)
    assertThat(secondFoo.baz).isEqualTo(10)
}
```