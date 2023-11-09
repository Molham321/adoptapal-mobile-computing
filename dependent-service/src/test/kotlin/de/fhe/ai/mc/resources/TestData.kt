package de.fhe.ai.mc.resources

import de.fhe.ai.mc.model.Contact

/**
 * Some static test data used during tests.
 */

class TestData {

    companion object {
        fun newContact(): Contact {
            return Contact("Mustermann", "Max", "m.muster@mail.com")
        }
    }



}