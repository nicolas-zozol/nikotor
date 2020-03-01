package io.robusta.nikotor.data

import java.util.*

interface HasId : Comparable<String> {

    val id: String

    override fun compareTo(other: String): Int {
        return id.compareTo(other)
    }
}

open class SimpleEntity : HasId {
    override val id = UUID.randomUUID().toString()
}

class DataSet<Entity> where Entity : HasId {

    private val map = TreeMap<String, Entity>()

    val size: Int
        get() = map.size


    fun add(element: Entity) {
        if (this.map[element.id] != null) {
            throw IllegalStateException("Element with id " + element.id + " is already existing")
        }
        this.map[element.id] = element
    }

    fun remove(element: Entity) {
        this.map.remove(element.id)
    }

    fun remove(id: String) {
        this.map.remove(id)
    }

    fun find(id: String): Entity? {
        return map[id]
    }

    fun clear() {
        this.map.clear();
    }

    fun isEmpty(): Boolean {
        return this.map.isEmpty()
    }

    fun getMap(): TreeMap<String, Entity> {
        return this.map
    }


}

