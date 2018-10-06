package com.park254.app.park254.utils

import java.util.*

internal class MySet<A> {
    var contents: ArrayList<A> = ArrayList()
    var indices = HashMap<A, Int>()
    var R = Random()

    //selects random element in constant time
    fun randomKey(): A {
        return contents[R.nextInt(contents.size)]
    }

    //adds new element in constant time
    fun add(a: A) {
        indices[a] = contents.size
        contents.add(a)
    }

    //removes element in constant time
    fun remove(a: A) {
        val index = indices[a]
        if (index != null) {
            contents[index] = contents[contents.size - 1]
        }
        contents.removeAt(contents.size - 1)
     //   indices.set(contents[contents.size - 1], index)
        indices.remove(a)
    }
}