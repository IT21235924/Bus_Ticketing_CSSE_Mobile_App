package com.example.csse_mobile//package com.example.csse_mobile

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidatorTest{

    @Test
    fun whenInputIsValid(){

        val name = "kasun"
        val email = "kasun@gmail.com"
        val address = "colombo"
        val password = "Kasun123"
        val result = Validator.validateInput(name, email, address, password)


    }

}