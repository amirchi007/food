package com.amir.dunifood.mainScreen

import com.amir.dunifood.model.Food
import com.amir.dunifood.utils.BasePresenter

interface MainScreenContract {

    interface Presenter: BasePresenter<View> {

        fun firstRun()

        fun onSearchFood(filter: String)
        fun onAddNewFoodClicked(food: Food)
        fun onDeleteAllClicked()

        fun onUpdateFood(food: Food, pos :Int)
        fun onDeleteFood(food: Food, pos: Int)

    }

    interface View {

        fun showFoods(data: List<Food>)
        fun refreshFoods(data: List<Food>)
        fun addNewFood(newFood: Food)
        fun deleteFood(oldFood: Food, pos: Int)
        fun updateFood(editingFood: Food, pos: Int)

    }

}