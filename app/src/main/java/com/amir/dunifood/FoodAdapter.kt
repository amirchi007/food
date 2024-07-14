package com.amir.dunifood

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.motion.widget.KeyPosition
import androidx.recyclerview.widget.RecyclerView
import com.amir.dunifood.databinding.ActivityMainBinding
import com.amir.dunifood.databinding.ItemFoodBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class FoodAdapter(private val data: ArrayList<Food>, private val foodEvents: FoodEvents) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {


    inner class FoodViewHolder(val binding: ItemFoodBinding ) : RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("setTextI18n")
        fun bindData(position: Int) {

                binding.itemTxtSubject.text = data[position].txtSubject
                binding.itemTxtCity.text = data[position].txtCity
                binding.itemTxtPrice.text = "$" + data[position].txtPrice + " vip"
                binding.itemTxtDistance.text = data[position].txtDistance + " miles from you"
                binding.itemRatingMain.rating = data[position].rating
                binding.itemTxtRating.text = "(" + data[position].numOfRating.toString() + " Ratings)"

            Glide
                .with(binding.root.context)
                .load(data[position].urlImage)
                .transform(RoundedCorners(16))
                .into(binding.itemImgMain)

            itemView.setOnClickListener {

                foodEvents.onFoodClicked(data[adapterPosition], adapterPosition)

            }
            itemView.setOnLongClickListener {
                foodEvents.onFoodLongClicked(data[adapterPosition], adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        // ویو مارو هلد  میکنه به اصطلاح ذخیره میکنه و   داره (داره تعداد رو از اینیر کلاس بالا میگیره)
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        // تعداد آیتم ها رو برگردونیم
        return data.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        //اطلاعات رو داخل ویو قرار بدیم(وقتی کاربر اسکرول میکنه)
        holder.bindData(position)

    }

    fun addFood(newfood: Food) {
        //add food to list :
        data.add(0, newfood)
        notifyItemInserted(0)
    }

    fun removeFood(oldFood: Food, oldPosition: Int) {
        //remove item from list
        data.remove(oldFood)
        notifyItemRemoved(oldPosition)
    }

    fun updateFood(newfood: Food, position: Int) {
        // update item from list
        data.set(position, newfood)
        notifyItemChanged(position)
    }

    fun setData(newList: ArrayList<Food>) {

        // set new data to list
        data.clear()
        data.addAll(newList)

        notifyDataSetChanged()
    }

    interface FoodEvents {
        // 1.create interface in adapter
        // 2. get an object of interface  in args of adapter
        // 3.fill (call) object of interface with your data
        // 4.implementation in MainActivity

        fun onFoodClicked(food: Food, position: Int)

        fun onFoodLongClicked(food: Food, position: Int)

    }

}