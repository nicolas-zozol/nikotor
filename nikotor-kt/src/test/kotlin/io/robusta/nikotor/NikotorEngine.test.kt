package io.robusta.nikotor

import org.junit.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NikotorEngineTest {


    val store:EventStore;
    val engine : NikotorEngine;

    init {
        store = EventStore()
        engine = NikotorEngine()
    }


    @Test
    fun testBasic(){
        assert(1 == 2);
    }


}

