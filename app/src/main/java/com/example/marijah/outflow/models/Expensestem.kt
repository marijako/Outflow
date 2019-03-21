package com.example.marijah.outflow.models

class ExpenseItem()
{
    var price : Int = 0
    var place : String = ""
    var category : String = ""
    var date : String = ""
    var comment : String = ""

    constructor(price:Int,category : String, place : String, date: String, comment: String):this()
    {
        this.price = price
        this.category = category
        this.place = place
        this.date = date
        this.comment = comment
    }
}
