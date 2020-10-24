package com.HBKstudio.FoodDetector

import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.HBKstudio.FoodDetector.Utils.getRoundedCornerBitmap


class DescriptionActivity : AppCompatActivity() {


    private lateinit var foodName: String
    private lateinit var foodImage: ImageView
    private lateinit var foodLabel: TextView
    private lateinit var calories: TextView
    private lateinit var foodDescription: TextView
    lateinit var foodIngredients: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_description)

        foodImage = findViewById(R.id.image_food)
        foodLabel = findViewById(R.id.food_name)
        calories = findViewById(R.id.calories)
        foodDescription = findViewById(R.id.food_description)
        foodIngredients = findViewById(R.id.food_ingredients)


        foodName = getModuleAssetName()
        //showing the right result based on clicked button from previous activity
        when (foodName) {
            "molokhia" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.molokhia, this))
                foodLabel.setText(R.string.molokhia_name)
                calories.setText(R.string.molokhia_calories)
                foodDescription.setText(R.string.molokhia_description)
                foodIngredients.setText(R.string.molokhia_ingredients)
            }
            "kosksi" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.kosksi, this))
                foodLabel.setText(R.string.kosksi_name)
                calories.setText(R.string.kosksi_calories)
                foodDescription.setText(R.string.kosksi_description)
                foodIngredients.setText(R.string.kosksi_ingredients)
            }
            "fricassé" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.fricassee, this))
                foodLabel.setText(R.string.fricassee_name)
                calories.setText(R.string.fricassee_calories)
                foodDescription.setText(R.string.fricassee_description)
                foodIngredients.setText(R.string.fricassee_ingredients)
            }
            "lablabi" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.lablabi, this))
                foodLabel.setText(R.string.lablabi_name)
                calories.setText(R.string.lablabi_calories)
                foodDescription.setText(R.string.lablabi_description)
                foodIngredients.setText(R.string.lablabi_ingredients)
            }
            "brik" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.brik, this))
                foodLabel.setText(R.string.brik_name)
                calories.setText(R.string.brik_calories)
                foodDescription.setText(R.string.brik_description)
                foodIngredients.setText(R.string.brik_ingredients)
            }
            "chappati" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.chappati, this))
                foodLabel.setText(R.string.chappati_name)
                calories.setText(R.string.chappati_calories)
                foodDescription.setText(R.string.chappati_description)
                foodIngredients.setText(R.string.chappati_ingredients)
            }
            "zlabia" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.zalabia, this))
                foodLabel.setText(R.string.zalabia_name)
                calories.setText(R.string.zalabia_calories)
                foodDescription.setText(R.string.zalabia_description)
                foodIngredients.setText(R.string.zalabia_ingredients)
            }
            "baklawa" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.baklawa, this))
                foodLabel.setText(R.string.baklawa_name)
                calories.setText(R.string.baklawa_calories)
                foodDescription.setText(R.string.baklawa_description)
                foodIngredients.setText(R.string.baklawa_ingredients)
            }
            "makroudh" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.makroudh, this))
                foodLabel.setText(R.string.makroudh_name)
                calories.setText(R.string.makroudh_calories)
                foodDescription.setText(R.string.makroudh_description)
                foodIngredients.setText(R.string.makroudh_ingredients)
            }
            "kaak warka" -> {
                foodImage.setImageBitmap(getRoundedCornerBitmap(R.drawable.kaak_warka, this))
                foodLabel.setText(R.string.kaak_warka_name)
                calories.setText(R.string.kaak_warka_calories)
                foodDescription.setText(R.string.kaak_warka_description)
                foodIngredients.setText(R.string.kaak_warka_ingredients)
            }
            else -> foodImage.setImageResource(R.drawable.ic_error_icon)
        }
    }

    //getting selected food classe by user from the previous activity
    private fun getModuleAssetName(): String {
        val foodNameFromIntent = intent.getStringExtra("FOODNAME")
        foodName = if (!TextUtils.isEmpty(foodNameFromIntent)) foodNameFromIntent else "wrong"
        return foodName

    }
}

