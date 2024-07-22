package com.amir.dunifood

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.amir.dunifood.databinding.ActivityMainBinding
import com.amir.dunifood.databinding.DialogDeleteNewItemsBinding
import com.amir.dunifood.databinding.DialogeAddNewItemBinding
import com.amir.dunifood.databinding.DialogeUpdateItemBinding
import com.amir.dunifood.room.Food
import com.amir.dunifood.room.MyDatabase
import com.amir.dunifood.room.foodDao
import java.util.ArrayList

// how to use room in our project
// 1. implement room
// 2. make Entity (table)
// 3. create Dao (Accessing Data Using) interface
// 4. create database

//how to use recycle view:
//1. create a view pff recycle view in activity_main.xml *
//2. create item for recycle view *
//3. create adapter recycle view *
//4. set adapter to recyclerView and set layout manager

const val BASE_URL_IMAGE =  "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food"
const val ACTIVITY_MAIN = "activityMain"

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvents {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: FoodAdapter
    private lateinit var foodDao: foodDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // it is object of data base
        foodDao = MyDatabase.getDatabase(this).FoodDao

        val sharedPreferences = getSharedPreferences("dunifood", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }
        showAllData()

        binding.btnRemoveAllFood.setOnClickListener {
            removeAllData()
        }

        binding.btnAddNewFood.setOnClickListener {
            addNewFood()


        }

        binding.edtSearch.addTextChangedListener { editTextInput ->
            searchOnDatabase(editTextInput!!.toString())

        }
    }

    // crud operations
    private fun searchOnDatabase(editTextInput: String) {

        if (editTextInput.isNotEmpty()) {

            // filter Data 'h'
            val searchData = foodDao.searchFood(editTextInput)
            myAdapter.setData(ArrayList(searchData))


        } else {
            // show all data
            val data = foodDao.getAllFood()
            myAdapter.setData(ArrayList(data))
        }
    }
    private fun addNewFood() {

        val dialog = AlertDialog.Builder(this).create()
        val dialogbinding = DialogeAddNewItemBinding.inflate(layoutInflater)
        dialog.setView(dialogbinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogbinding.dialogEdtBtnDone.setOnClickListener {

            if (dialogbinding.dialogEdtFoodName.length() > 0 && dialogbinding.dialogEdtFoodCity.length() > 0 && dialogbinding.dialogEdtFoodPrice.length() > 0 && dialogbinding.dialogEdtFoodDistance.length() > 0
            ) {

                val txtName = dialogbinding.dialogEdtFoodName.text.toString()
                val txtPrice = dialogbinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = dialogbinding.dialogEdtFoodDistance.text.toString()
                val txtCity = dialogbinding.dialogEdtFoodCity.text.toString()
                val txtRatingNumber: Int = (1..150).random()
                val ratingBarStar: Float = (1..5).random().toFloat()

                /*val min = 0f
                val max = 5f
                val random:Float = min + Random().nextFloat() * (max - min)
                */

                val randomForUrl = (1..12).random()
               // val urlPick = BASE_URL_IMAGE + randomForUrl.toString() + ".jpg"
                val urlPick = "$BASE_URL_IMAGE$randomForUrl.jpg"


                val newFood = Food(
                    txtSubject = txtName,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    txtCity = txtCity,
                    urlImage = urlPick,
                    numOfRating = txtRatingNumber,
                    rating = ratingBarStar
                )

                myAdapter.addFood(newFood)
                foodDao.insertOrUpdate(newFood)
                this.closeOptionsMenu()

                binding.recyclerMain.scrollToPosition(0)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "لطفا مقادیر را وارد کنید", Toast.LENGTH_SHORT).show()
            }
        }
        dialogbinding.dialogEdtBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
    private fun removeAllData() {
        foodDao.deleteAllFoods()
        showAllData()
    }
    private fun showAllData() {
        // return list of food from database
        val foodData = foodDao.getAllFood()

        myAdapter = FoodAdapter(ArrayList(foodData), this)

        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        Log.v("amir", foodData.toString())
    }

    private fun firstRun() {
        val foodList = arrayListOf(
            Food(
                txtSubject = "Hamburger",
                txtPrice = "15",
                txtDistance = "3",
                txtCity = "Isfahan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food1.jpg",
                numOfRating = 20,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Grilled fish",
                txtPrice = "20",
                txtDistance = "2.1",
                txtCity = "Tehran, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food2.jpg",
                numOfRating = 10,
                rating = 4f
            ),
            Food(
                txtSubject = "Lasania",
                txtPrice = "40",
                txtDistance = "1.4",
                txtCity = "Isfahan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food3.jpg",
                numOfRating = 30,
                rating = 2f
            ),
            Food(
                txtSubject = "pizza",
                txtPrice = "10",
                txtDistance = "2.5",
                txtCity = "Zahedan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food4.jpg",
                numOfRating = 80,
                rating = 1.5f
            ),
            Food(
                txtSubject = "Sushi",
                txtPrice = "20",
                txtDistance = "3.2",
                txtCity = "Mashhad, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food5.jpg",
                numOfRating = 200,
                rating = 3f
            ),
            Food(
                txtSubject = "Roasted Fish",
                txtPrice = "40",
                txtDistance = "3.7",
                txtCity = "Jolfa, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food6.jpg",
                numOfRating = 50,
                rating = 3.5f
            ),
            Food(
                txtSubject = "Fried chicken",
                txtPrice = "70",
                txtDistance = "3.5",
                txtCity = "NewYork, USA",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food7.jpg",
                numOfRating = 70,
                rating = 2.5f
            ),
            Food(
                txtSubject = "Vegetable salad",
                txtPrice = "12",
                txtDistance = "3.6",
                txtCity = "Berlin, Germany",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food8.jpg",
                numOfRating = 40,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Grilled chicken",
                txtPrice = "10",
                txtDistance = "3.7",
                txtCity = "Beijing, China",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food9.jpg",
                numOfRating = 15,
                rating = 5f
            ),
            Food(
                txtSubject = "Baryooni",
                txtPrice = "16",
                txtDistance = "10",
                txtCity = "Ilam, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food10.jpg",
                numOfRating = 28,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Ghorme Sabzi",
                txtPrice = "11.5",
                txtDistance = "7.5",
                txtCity = "Karaj, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food11.jpg",
                numOfRating = 27,
                rating = 5f
            ),
            Food(
                txtSubject = "Rice",
                txtPrice = "12.5",
                txtDistance = "2.4",
                txtCity = "Shiraz, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food12.jpg",
                numOfRating = 35,
                rating = 2.5f
            )
        )
        foodDao.insertAllFoods(foodList)
        Toast.makeText(this, "welcome", Toast.LENGTH_SHORT).show()
    }

    override fun onFoodClicked(food: Food, position: Int) {

        val dialog = AlertDialog.Builder(this).create()
        val updateDialogBinding = DialogeUpdateItemBinding.inflate(layoutInflater)

        dialog.setView(updateDialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        updateDialogBinding.dialogEdtNameFood.setText(food.txtSubject)
        updateDialogBinding.dialogEdtFoodCity.setText(food.txtCity)
        updateDialogBinding.dialogEdtFoodPrice.setText(food.txtPrice)
        updateDialogBinding.dialogEdtFoodDistance.setText(food.txtDistance)

        updateDialogBinding.dialogUpdateBtnCancel.setOnClickListener {
            dialog.dismiss()
        }

        updateDialogBinding.dialogUpdateBtnDone.setOnClickListener {

            if (updateDialogBinding.dialogEdtNameFood.length() > 0 && updateDialogBinding.dialogEdtFoodCity.length() > 0 && updateDialogBinding.dialogEdtFoodPrice.length() > 0 && updateDialogBinding.dialogEdtFoodDistance.length() > 0) {
                val txtName = updateDialogBinding.dialogEdtNameFood.text.toString()
                val txtPrice = updateDialogBinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = updateDialogBinding.dialogEdtFoodDistance.text.toString()
                val txtCity = updateDialogBinding.dialogEdtFoodCity.text.toString()

                // create new food to add to recycler view
                // give id in order to update
                val newFood = Food(
                    id = food.id,
                    txtSubject = txtName,
                    txtPrice = txtPrice,
                    txtDistance = txtDistance,
                    txtCity = txtCity,
                    urlImage = food.urlImage,
                    numOfRating = food.numOfRating,
                    rating = food.rating
                )

                // update item in adapter
                myAdapter.updateFood(newFood, position)
                // update item in database
                foodDao.insertOrUpdate(newFood)

                dialog.dismiss()
            } else {
                Toast.makeText(this, "لطفا همه مقادیر را وارد کن :)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFoodLongClicked(food: Food, position: Int) {
        var dialog = AlertDialog.Builder(this).create()
        val dialogDeletBinding = DialogDeleteNewItemsBinding.inflate(layoutInflater)
        dialog.setView(dialogDeletBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogDeletBinding.dialogBtnDeleteCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogDeletBinding.dialogBtnDeleteSure.setOnClickListener {
            dialog.dismiss()
            myAdapter.removeFood(food, position)
            foodDao.deleteFood(food )
        }
    }
}