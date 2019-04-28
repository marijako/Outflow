package com.example.marijah.outflow.models

class Group() {

    var key: String = ""
    var groupName: String = ""
    var groupMembers: ArrayList<String> = ArrayList()


    constructor(key: String, groupName: String) : this() {
        this.key = key
        this.groupName = groupName

    }

    constructor(key: String, groupName: String, groupMembers: ArrayList<String>) : this() {
        this.key = key
        this.groupName = groupName
        this.groupMembers = groupMembers
    }


}