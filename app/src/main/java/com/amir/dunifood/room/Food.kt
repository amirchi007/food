package com.amir.dunifood.room

import androidx.room.Entity
import androidx.room.PrimaryKey

// entity make it (data class) table
@Entity(tableName = "table_food")
data class Food(
    // make primary  key and you can give it id or not
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null ,

    val txtSubject: String,
    val txtPrice: String,
    val txtDistance: String,
    val txtCity: String,

    //change name in column
//    @ColumnInfo("url")
    val urlImage: String,

    val numOfRating: Int,
    val rating: Float

)