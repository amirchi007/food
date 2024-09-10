package com.amir.dunifood.mainScreen

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
import com.amir.dunifood.model.Food
import com.amir.dunifood.model.MyDatabase
import com.amir.dunifood.model.foodDao
import com.amir.dunifood.utils.BASE_URL_IMAGE
import com.amir.dunifood.utils.showToast
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class MainScreenActivity : AppCompatActivity(), FoodAdapter.FoodEvents, MainScreenContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: FoodAdapter
    private lateinit var mainScreenPresenter: MainScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainScreenPresenter = MainScreenPresenter(MyDatabase.getDatabase(this).FoodDao)

        val sharedPreferences = getSharedPreferences("dunifood", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            mainScreenPresenter.firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }
        mainScreenPresenter.onAttach(this)

        binding.btnRemoveAllFood.setOnClickListener {
            mainScreenPresenter.onDeleteAllClicked()
        }
        binding.btnAddNewFood.setOnClickListener {
            addNewFood()
        }
        binding.edtSearch.addTextChangedListener { editTextInput ->
            mainScreenPresenter.onSearchFood(editTextInput.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScreenPresenter.onDetach()
    }

    private fun addNewFood() {

        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogeAddNewItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.dialogEdtBtnDone.setOnClickListener {
            if (dialogBinding.dialogEdtFoodName.length() > 0 &&
                dialogBinding.dialogEdtFoodCity.length() > 0 &&
                dialogBinding.dialogEdtFoodPrice.length() > 0 &&
                dialogBinding.dialogEdtFoodDistance.length() > 0
            ) {
                val txtName = dialogBinding.dialogEdtFoodName.text.toString()
                val txtPrice = dialogBinding.dialogEdtFoodPrice.text.toString()
                val txtDistance = dialogBinding.dialogEdtFoodDistance.text.toString()
                val txtCity = dialogBinding.dialogEdtFoodCity.text.toString()
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

                mainScreenPresenter.onAddNewFoodClicked(newFood)
                dialog.dismiss()
            } else {
                showToast("لطفا همه مقادیر را وارد کنید")
            }
        }
        dialogBinding.dialogEdtBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
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
                mainScreenPresenter.onUpdateFood(newFood, position)
                dialog.dismiss()
            } else {
                showToast("لطفا همه مقادیر را وارد کنید")
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
            mainScreenPresenter.onDeleteFood(food, position)
            dialog.dismiss()
        }
    }

    override fun showFoods(data: List<Food>) {
        mainAdapter = FoodAdapter(ArrayList(data), this)
        binding.recyclerMain.adapter = mainAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun refreshFoods(data: List<Food>) {
        mainAdapter.setData(ArrayList(data))
    }

    override fun addNewFood(newFood: Food) {
        mainAdapter.addFood(newFood)
    }

    override fun deleteFood(oldFood: Food, pos: Int) {
        mainAdapter.removeFood(oldFood, pos)
    }

    override fun updateFood(editingFood: Food, pos: Int) {
        mainAdapter.updateFood(editingFood, pos)
    }
}