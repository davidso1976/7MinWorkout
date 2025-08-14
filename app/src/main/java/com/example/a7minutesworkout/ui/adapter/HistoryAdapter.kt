package com.example.a7minutesworkout.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.databinding.ItemHistoryRowBinding
import com.example.a7minutesworkout.model.HistoryEntity
import lib.view.GenericRecyclerViewAdapter

class HistoryAdapter(): GenericRecyclerViewAdapter<HistoryEntity>() {

    override fun getItemViewType(position: Int): Int = 0

    override fun getBinder(parent: ViewGroup, viewType: Int, view: View): Binder<HistoryEntity> = HistoryAdapterBinder(view)

    override fun getLayoutId(viewType: Int): Int = R.layout.item_history_row

    private class HistoryAdapterBinder(private val view: View) : Binder<HistoryEntity>(view) {
        override fun bind(item: HistoryEntity, position: Int) {
            DataBindingUtil.bind<ItemHistoryRowBinding>(view)?.let { binding ->
                binding.apply {
                    tvPosition.text = (position + 1).toString()
                    tvItem.text = item.date

                    if(position % 2 == 0) {
                        llHistoryItemMain.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.lightGray)
                        )
                    } else {
                        llHistoryItemMain.setBackgroundColor(
                            ContextCompat.getColor(itemView.context, R.color.white)
                        )
                    }
                }

            }
        }
    }


}