package com.example.marijah.outflow.models


/*data class ExpenseItem(
    val key: String,
    val price: Int,
    val place: String,
    val category: String,
    val date: String,
    val comment: String,
    val whoDidThePurchase: String = "me"
    )*/


class ExpenseItem()
{
    var key : String = ""
    var price : Int = 0
    var place : String = ""
    var category : String = ""
    var date : String = ""
    var comment : String = ""
    var whoDidThePurchase : String = ""

    constructor(key: String, price:Int,category : String, place : String, date: String, comment: String, whoDidThePurchase : String):this()
    {
        this.key = key
        this.price = price
        this.category = category
        this.place = place
        this.date = date
        this.comment = comment
        this.whoDidThePurchase = whoDidThePurchase
    }
}
