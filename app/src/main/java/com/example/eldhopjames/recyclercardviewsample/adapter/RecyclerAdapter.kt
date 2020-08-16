package com.example.eldhopjames.recyclercardviewsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eldhopjames.recyclercardviewsample.R
import com.example.eldhopjames.recyclercardviewsample.modelClass.ModelClass
import com.example.eldhopjames.recyclercardviewsample.viewHolders.EmptyViewHolder
import com.example.eldhopjames.recyclercardviewsample.viewHolders.EvenViewHolder
import com.example.eldhopjames.recyclercardviewsample.viewHolders.OddViewHolder

/**
 * needs RecyclerAdapter and View Holder
 * View Holder : used to caches the data and no need to write findViewById for each items
 * RecyclerAdapter : populates the item  and data about it , data comes from n/w or from DB
 * We create an interface to pass the values of the item clicked into activity
 *
 */
class RecyclerAdapter(private val mContext: Context) : RecyclerView.Adapter<ViewHolder>() {
    private var mListItems: MutableList<ModelClass> = ArrayList()
    private var mListener: ((ModelClass, Int) -> Unit)? = null

    /**
     * lambda will forward our click from adapter to our activity/fragment
     * NOTE : we can pass lambda via constructor of the adapter also
     */
    fun setOnContentClickListener(listener: (ModelClass, Int) -> Unit) {
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder { // this method calls when ever our view method is created , ie; the instance of ViewHolder class is created
        val inflater = LayoutInflater.from(parent.context)
        val view: View
        return when (viewType) {
            0 -> {
                view = inflater
                    .inflate(R.layout.even_list_item, parent, false)
                OddViewHolder(view, mListener, mListItems)
            }
            1 -> {
                view = inflater
                    .inflate(R.layout.odd_list_item, parent, false)
                EvenViewHolder(view, mListener, mListItems)
            }
            else -> {
                view = inflater
                    .inflate(R.layout.item_empty, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mListItems[position].type == 0) {
            return 0
        } else if (mListItems[position].type == 1) {
            return 1
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) { //populate the data into the list_item (View Holder), as we scroll
        (holder as? EvenViewHolder)?.bindData()
        (holder as? OddViewHolder)?.bindData()
    }

    override fun getItemCount(): Int {
        return mListItems.size
    }

    //-------------------------------------Manipulating RecyclerView--------------------------------//

    fun submitList(newItems: MutableList<ModelClass>?) {
        if (!newItems.isNullOrEmpty()) {
            newItems.addAll(0, mListItems)
            updateListItems(newItems)
        }
    }

    fun clearData() {
        if (mListItems.isNotEmpty()) {
            val newItems = ArrayList<ModelClass>()
            updateListItems(newItems)

        }
    }

    fun addOrReplaceItem(newItem: ModelClass?, position: Int = itemCount) {
        if (newItem != null) {
            val newItems = ArrayList<ModelClass>()
            newItems.addAll(mListItems)
            if (itemCount >= position) {
                newItems.add(position, newItem)
            } else {
                newItems.add(newItem)
            }
            updateListItems(newItems)
        }
    }

    fun removeItem(position: Int = itemCount - 1) {
        val newItems = ArrayList<ModelClass>()
        newItems.addAll(mListItems)
        if (itemCount > position) {
            newItems.removeAt(position)
        } else {
            newItems.removeAt(mListItems.size - 1)
        }
        updateListItems(newItems)
    }

    private fun updateListItems(newItems: MutableList<ModelClass>) {
        val diffResult = DiffUtil.calculateDiff(
            RecyclerDiffUtilCallback(
                newItems
            )
        )
        mListItems.clear()
        mListItems.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    private inner class RecyclerDiffUtilCallback(var newItems: List<ModelClass>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return itemCount
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        //Checks both items are same by checking its primary key
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (newItems[newItemPosition].primaryKey == mListItems[oldItemPosition].primaryKey)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newItems[newItemPosition] == mListItems[oldItemPosition]
        }

    }


//    fun clearData() {
//        if (mListItems.isNotEmpty()) {
//            mListItems.clear()
//            notifyDataSetChanged()
//        }
//    }
//
//    fun addItemRange(items: List<ModelClass>?) {
//        if (!items.isNullOrEmpty()) {
//            val position = mListItems.size
//            mListItems.addAll(position, items)
//            notifyItemRangeInserted(position, items.size)
//        }
//    }
//
//    fun addItemRangeInPosition(items: List<ModelClass>?, position: Int) {
//        if (!items.isNullOrEmpty()) {
//            if (mListItems.size >= position) {
//                mListItems.addAll(position, items)
//                notifyItemRangeInserted(position, items.size)
//            } else {
//                mListItems.addAll(items)
//                notifyItemRangeInserted(mListItems.size - 1, items.size)
//            }
//        }
//    }

//    fun addItem(item: ModelClass?, position: Int) {
//        if (item != null) {
//            if (mListItems.size >= position) {
//                mListItems.add(position, item)
//                notifyItemInserted(position)
//            } else {
//                mListItems.add(item)
//                notifyItemInserted(mListItems.size - 1)
//            }
//        }
//    }

//
//    fun removeItem(position: Int) {
//        if (mListItems.size > position) {
//            mListItems.removeAt(position)
//            notifyItemRemoved(position)
//        } else {
//            val newPosition = mListItems.size - 1
//            mListItems.removeAt(newPosition)
//            notifyItemRemoved(newPosition)
//        }
//    }

}