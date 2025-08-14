package com.example.a7minutesworkout.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.a7minutesworkout.databinding.ItemExerciseStatusBinding
import androidx.core.graphics.toColorInt
import androidx.databinding.DataBindingUtil
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.model.ExerciseModel
import lib.view.GenericRecyclerViewAdapter

class ExerciseStatusAdapter() : GenericRecyclerViewAdapter<ExerciseModel>(){

    override fun getItemViewType(position: Int): Int = 0

    override fun getBinder(parent: ViewGroup, viewType: Int, view: View): Binder<ExerciseModel> = ExerciseStatusAdapterBinder(view)

    override fun getLayoutId(viewType: Int): Int = R.layout.item_exercise_status

    private class ExerciseStatusAdapterBinder(private val view: View) : Binder<ExerciseModel>(view){
        override fun bind(item: ExerciseModel, position: Int) {
            DataBindingUtil.bind<ItemExerciseStatusBinding>(view)?.let { binding ->
                with(binding) {
                    tvItem.text = item.id.toString()
                    when{
                        item.isCompleted == true -> {
                            tvItem.background = ContextCompat.getDrawable(
                                itemView.context, R.drawable.item_circular_color_accent_background
                            )
                            tvItem.setTextColor("#FFFFFF".toColorInt())
                        }
                        item.isSelected == true -> {
                            tvItem.background = ContextCompat.getDrawable(
                                itemView.context, R.drawable.item_circular_thin_color_accent_border
                            )
                            tvItem.setTextColor("#212121".toColorInt())
                        }
                        else -> {
                            tvItem.background = ContextCompat.getDrawable(
                                itemView.context, R.drawable.item_color_gray_background
                            )
                            tvItem.setTextColor("#212121".toColorInt())
                        }
                    }
                }
            }
        }
    }

}