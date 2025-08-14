package lib.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException

typealias ViewAdjustment = (view: View, viewType: Int, parent: ViewGroup) -> View

abstract class GenericRecyclerViewAdapter<T>(
    var items: List<T> = emptyList(),
    diffCallback: DiffUtil.ItemCallback<T>? = null
) : RecyclerView.Adapter<GenericRecyclerViewAdapter.Binder<T>>() {

    var recyclerView: RecyclerView? = null
    val differ: AsyncListDiffer<T>? = diffCallback?.let {
        AsyncListDiffer(this, it).apply {
            submitList(items)
        }
    }

    val viewAdjustment = mutableListOf<ViewAdjustment>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<T>) {
        differ?.let {
            differ.submitList(items)
        } ?: run {
            this.items = items
            notifyDataSetChanged()
        }
    }

    fun currentList(): List<T> = differ?.currentList ?: items

    open fun deleteItem(position: Int) {
        updateItems(
            currentList().filterIndexed { index, t -> position != index }
        )
    }

    override fun getItemCount(): Int = currentList().size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Binder<T> = getBinder(parent, viewType, inflate(viewType, parent)) ?: throw IllegalStateException("binder was not found for view type $viewType")

    override fun onBindViewHolder(holder: Binder<T>, position: Int) {
        currentList().getOrNull(position)?.let {
            holder.bind(it, position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    abstract override fun getItemViewType(position: Int): Int

    abstract fun getBinder(parent: ViewGroup, viewType: Int, view: View): Binder<T>?

    @LayoutRes abstract fun getLayoutId(viewType: Int): Int

    abstract class Binder<in T>(view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(item: T, position: Int)
    }

    open class NoOpBinder<in T>(view: View) : Binder<T>(view) {
        override fun bind(item: T, position: Int) {
            /* no-op */
        }
    }

    private fun inflate(viewType: Int, parent: ViewGroup, attachToRoot: Boolean = false): View {
        var view = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, attachToRoot)

        viewAdjustment.forEach { adjustView ->
            view = adjustView(view, viewType, parent)
        }
        return view
    }

}