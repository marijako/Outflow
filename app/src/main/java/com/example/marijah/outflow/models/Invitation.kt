package com.example.marijah.outflow.models

class Invitation() {

    var key: String = ""
    var email: String = ""

    constructor(key: String, email: String) : this() {
        this.key = key
        this.email = email
    }

}