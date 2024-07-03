package com.amir.dunifood

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.amir.dunifood.databinding.ActivityMainBinding
import com.amir.dunifood.databinding.DialogDeleteNewItemsBinding
import com.amir.dunifood.databinding.DialogeAddNewItemBinding
import com.amir.dunifood.databinding.DialogeUpdateItemBinding
import java.util.Random
import kotlin.math.log

const val ACTIVITY_MAIN = "activityMain"

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvents {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.v(ACTIVITY_MAIN,"oncreate call shod")


        //how to use recycle view:
        //1. create a view pff recycle view in activity_main.xml *
        //2. create item for recycle view *
        //3. create adapter recycle view *
        //4. set adapter to recyclerView and set layout manager

        val foodList = arrayListOf(
            Food(
                "Hamburger",
                "15",
                "3",
                "Isfahan, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food1.jpg",
                20,
                4.5f
            ),
            Food(
                "Grilled fish",
                "20",
                "2.1",
                "Tehran, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food2.jpg",
                10,
                4f
            ),
            Food(
                "Lasania",
                "40",
                "1.4",
                "Isfahan, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food3.jpg",
                30,
                2f
            ),
            Food(
                "pizza",
                "10",
                "2.5",
                "Zahedan, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food4.jpg",
                80,
                1.5f
            ),
            Food(
                "Sushi",
                "20",
                "3.2",
                "Mashhad, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food5.jpg",
                200,
                3f
            ),
            Food(
                "Roasted Fish",
                "40",
                "3.7",
                "Jolfa, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food6.jpg",
                50,
                3.5f
            ),
            Food(
                "Fried chicken",
                "70",
                "3.5",
                "NewYork, USA",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food7.jpg",
                70,
                2.5f
            ),
            Food(
                "Vegetable salad",
                "12",
                "3.6",
                "Berlin, Germany",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food8.jpg",
                40,
                4.5f
            ),
            Food(
                "Grilled chicken",
                "10",
                "3.7",
                "Beijing, China",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food9.jpg",
                15,
                5f
            ),
            Food(
                "Baryooni",
                "16",
                "10",
                "Ilam, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food10.jpg",
                28,
                4.5f
            ),
            Food(
                "Ghorme Sabzi",
                "11.5",
                "7.5",
                "Karaj, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food11.jpg",
                27,
                5f
            ),
            Food(
                "Rice",
                "12.5",
                "2.4",
                "Shiraz, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food12.jpg",
                35,
                2.5f
            ),
        )

        Log.v(ACTIVITY_MAIN,"tedad : ${foodList.size}")

        myAdapter = FoodAdapter(foodList.clone() as ArrayList<Food>, this)

        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        binding.btnAddNewFood.setOnClickListener {
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

                    val randomForUrl = (0..11).random()
                    val urlPick = foodList[randomForUrl].urlImage

                    val newFood = Food(
                        txtName,
                        txtPrice,
                        txtDistance,
                        txtCity,
                        urlPick,
                        txtRatingNumber,
                        ratingBarStar
                    )

                    myAdapter.addFood(newFood)
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



        binding.edtSearch.addTextChangedListener { editTextInput ->
            if (editTextInput!!.isNotEmpty()) {

                // filter Data
                val cloneList = foodList.clone() as ArrayList<Food>
                val filteredList = cloneList.filter { foodItem ->
                    foodItem.txtSubject.contains(editTextInput)

                }
                myAdapter.setData(ArrayList(filteredList))


            } else {
                // show all data
                myAdapter.setData(foodList.clone() as ArrayList<Food>)
            }
        }
    }

    override fun onFoodClicked(food: Food, position: Int) {

        Log.v(ACTIVITY_MAIN,"On Food Clicked${food.txtSubject}")

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
                val newFood = Food(
                    txtName,
                    txtPrice,
                    txtDistance,
                    txtCity,
                    food.urlImage,
                    food.numOfRating,
                    food.rating
                )

                // update item :
                myAdapter.updateFood(newFood, position)

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
        }
    }


}