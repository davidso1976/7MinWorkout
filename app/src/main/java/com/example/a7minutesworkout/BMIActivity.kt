package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }

    private var binding : ActivityBmiBinding? = null
    private var currentVisibleView : String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBMIActivity)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate BMI"
        }
        binding?.toolbarBMIActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.btnCalculateUnits?.setOnClickListener {
            calculateUnits()
        }
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if(checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnits()
            } else {
                makeVisibleUSUnits()
            }
        }

    }

    private fun calculateBMI(height : Float, weight : Float) : Float {
        return weight / (height*height)
    }

    private fun makeVisibleMetricUnits(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.llMetricsContainer?.visibility = View.VISIBLE
        binding?.llUSContainer?.visibility = View.GONE
        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE


        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()
    }

    private fun makeVisibleUSUnits(){
        currentVisibleView = US_UNITS_VIEW
        binding?.llMetricsContainer?.visibility = View.GONE
        binding?.llUSContainer?.visibility = View.VISIBLE
        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

        binding?.etUsUnitWeight?.text!!.clear()
        binding?.etUSUnitFeet?.text!!.clear()
        binding?.etUSUnitInch?.text!!.clear()

    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel : String
        val bmiDescription : String
        val bmiString = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Eat more"
        } else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Eat More"
        } else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Eat More"
        } else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "You are in good shape"
        } else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Eat less"
        } else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Eat less"
        } else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "Danger! Danger! Danger"
        } else {
            bmiLabel = "Obese Class ||| (Very severely obese)"
            bmiDescription = "You will die soon"
        }

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIDescription?.text = bmiDescription
        binding?.tvBMIValue?.text = bmiString
        binding?.tvBMIType?.text = bmiLabel

    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if(binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        } else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits() {
        if(currentVisibleView == METRIC_UNITS_VIEW) {
            if (validateMetricUnits()) {
                val heightValue : Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                val bimValue = calculateBMI(heightValue, weightValue)
                displayBMIResult(bimValue)
            } else {
                Toast.makeText(this, "Please enter a valid height and weight values", Toast.LENGTH_SHORT).show()
            }
        } else {
            if(validateUSUnits()) {
                val usUnitHeightValueFeet: String = binding?.etUSUnitFeet?.text.toString()
                val usUnitHeightValueInch: String = binding?.etUSUnitFeet?.text.toString()

                val weightValue : Float = binding?.etUsUnitWeight?.text.toString().toFloat()
                val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val bmiValue = 703 * (weightValue / (heightValue * heightValue))

                displayBMIResult(bmiValue)
            } else {
                Toast.makeText(this, "Please enter a valid height and weight values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUSUnits() : Boolean{
        var isValid = true
        when{
            binding?.etUsUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUSUnitFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUSUnitInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }
}