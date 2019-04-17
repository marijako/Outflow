package com.example.marijah.outflow.models

class ExpenseItem()
{
    var key : String = ""
    var price : Int = 0
    var place : String = ""
    var category : String = ""
    var date : String = ""
    var comment : String = ""

    constructor(key: String, price:Int,category : String, place : String, date: String, comment: String):this()
    {
        this.key = key
        this.price = price
        this.category = category
        this.place = place
        this.date = date
        this.comment = comment
    }
}
