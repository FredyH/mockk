package io.mockk.impl

import io.mockk.InternalPlatformDsl
import io.mockk.Ref
import io.mockk.StackElement
import io.mockk.impl.platform.CommonIdentityHashMapOf
import io.mockk.impl.platform.CommonRef
import io.mockk.impl.platform.NativeCounter
import io.mockk.impl.platform.NativeHexLongHelper
import kotlin.reflect.KClass

actual object InternalPlatform {
    internal val timeCounter = NativeCounter()

    actual fun time() = timeCounter.next()

    actual fun ref(obj: Any): Ref = CommonRef(obj)

    actual fun hkd(obj: Any): String = NativeHexLongHelper.toHexString(InternalPlatformDsl.identityHashCode(obj).toLong())

    actual fun isPassedByValue(cls: KClass<*>): Boolean {
        return when (cls) {
            Boolean::class -> true
            Byte::class -> true
            Short::class -> true
            Char::class -> true
            Int::class -> true
            Long::class -> true
            Float::class -> true
            Double::class -> true
            String::class -> true
            else -> false
        }
    }

    actual fun <K, V> MutableMap<K, V>.customComputeIfAbsent(key: K, valueFunc: (K) -> V): V {
        val value = get(key)
        return if (value == null) {
            val newValue = valueFunc(key)
            put(key, newValue)
            newValue
        } else {
            value
        }
    }

    actual fun counter(): () -> Long = NativeCounter()::next

    actual fun packRef(arg: Any?): Any? {
        return if (arg == null || isPassedByValue(arg::class))
            arg
        else
            ref(arg)
    }

    actual fun isSuspend(paramTypes: List<KClass<Any>>): Boolean {
        return false
    }

    actual fun prettifyRecordingException(ex: Throwable) = ex

    actual fun <K, V> weakMap(): MutableMap<K, V> = CommonIdentityHashMapOf()

    actual fun <K, V> identityMap(): MutableMap<K, V> = CommonIdentityHashMapOf()

    actual fun <T> synchronizedMutableList(): MutableList<T> = mutableListOf()

    actual fun <K, V> synchronizedMutableMap(): MutableMap<K, V> = hashMapOf()

    @Suppress("NAME_SHADOWING", "UNUSED_VARIABLE")
    actual fun <T : Any> copyFields(to: T, from: T) {
        TODO("copy fields")
    }

    actual fun captureStackTrace() = listOf<StackElement>()
}
