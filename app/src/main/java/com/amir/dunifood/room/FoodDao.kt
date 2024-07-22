package com.amir.dunifood.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface foodDao {

    // insert or update together
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(food: Food)

//    @Insert
//    fun insertFood(food: Food)

    @Insert
    fun insertAllFoods(data :List<Food>)

//    @Update
//    fun updateFood(food: Food)

    @Query("DELETE FROM table_food")
    fun deleteAllFoods()

    @Delete
    fun deleteFood(food: Food)

    // read all data before doing anything
    @Query("SELECT * FROM table_food")
    fun getAllFood(): List<Food>

    // for getting the char that user have searched we get it as val
    // for knowing val add :val in query
    // for using % as know it and get what ever in char example : '%'ham'% '
    @Query(
        "SELECT * FROM table_food" +
                " WHERE txtSubject LIKE'%'|| :searching ||'%'"
    )
    fun searchFood(searching: String): List<Food>


}