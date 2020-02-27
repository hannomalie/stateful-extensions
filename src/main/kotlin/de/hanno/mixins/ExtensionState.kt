package de.hanno.mixins

import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SimpleProperty<R, T>(var value: T): ReadWriteProperty<R, T> {
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        this.value = value
    }
}

class ExtensionState<R, T>(val defaultValue: T): ReadWriteProperty<R, T> {
    internal val receiverToProperty = WeakHashMap<R, SimpleProperty<*, *>>()

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        val readWriteProperty = receiverToProperty[thisRef] as? SimpleProperty<R, T>
        return readWriteProperty?.getValue(thisRef, property) ?: defaultValue
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        receiverToProperty.putIfAbsent(thisRef, SimpleProperty<R, T>(value))
    }
}

fun <R, T> extensionState(defaultValue: T): ExtensionState<R, T> {
    return ExtensionState(defaultValue)
}
