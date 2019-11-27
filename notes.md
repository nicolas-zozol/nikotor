Convariance / contravariance
====

in C# and Kotlin

* `in` : contravariant
* `out` : covariant



Good practices
----

* Read-only data types (sources) can be covariant (`out`);
* write-only data types (sinks) can be contravariant (`in`).
* Mutable data types which act as both sources and sinks **should be invariant**


Wildcard
---

The wildcard type argument ? extends E indicates that this method accepts a collection of objects of E or some subtype of E, not just E itself. This means that we can safely read E's from items (elements of this collection are instances of a subclass of E), but cannot write to it since we do not know what objects comply to that unknown subtype of E. 

PECS
---

Joshua Bloch calls those objects you only read from Producers, and those you only write to Consumers. He recommends: "For maximum flexibility, use wildcard types on input parameters that represent producers or consumers", and proposes the following mnemonic:

PECS stands for Producer-Extends, Consumer-Super.

En Kotlin: Bien plus logique

Consumer in, Producer out!



Examples
---

List<? extends Foo> list.
Cannot call `list.add(myFoo)` but can do `list.clear()`






Sources
----

https://kotlinlang.org/docs/reference/generics.html
https://en.wikipedia.org/wiki/Covariance_and_contravariance_(computer_science)
 


Kotlin
----

Declaration site variance

```java
interface Source<T> {
  T next();
}
class MainClient{
    void demo(Source<String> strs) {
      // Source<Object> objects = strs; // !!! Not allowed in Java
      Source<? extends Object> objects = strs; // Variance declared using the function
      // ...
    }
}
```

```kotlin
interface Source<out T> { // In  declaration, not using function
  fun next(): T;
}
class MainClient{
    fun demo(strs: Source<String> ):Void {
      // Source<Object> objects = strs; // !!! Not allowed in Java
      val objects: Source<Any>  = strs; // !!! OKin Java
      // ...
    }
}
```
