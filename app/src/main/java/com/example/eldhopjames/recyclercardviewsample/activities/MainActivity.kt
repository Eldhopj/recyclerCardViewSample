package com.example.eldhopjames.recyclercardviewsample.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eldhopjames.recyclercardviewsample.adapter.RecyclerAdapter
import com.example.eldhopjames.recyclercardviewsample.databinding.ActivityMainBinding
import com.example.eldhopjames.recyclercardviewsample.modelClass.ModelClass
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**1.Add dependencies
 * 2. Add recycler view  in the layout where it needed
 * 3.Create an layout for the items (list_item.xml)
 * 4.Model Class
 * 5.Bind the data with recycler view using an mRecyclerAdapter
 * 6. By using setOnItemClickListener handle the mRecyclerView Item click then pass the value to the DetailedActivity
 *
 * Parcelable
 * 1.Implement Parcelable in ModelClass and import all methods
 * 2.Send parcel position into DetailedActivity from MainActivity
 */

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        dummyData()
        gesture()
    }

    /**Initializing recyclerView */
    private fun initRecyclerView() {
        recyclerAdapter =
            RecyclerAdapter(this) //set the mRecyclerAdapter to the recycler view

        with(binding.recyclerView) {
            setHasFixedSize(true) // setting it to true if all the items are have a fixed height and width, this allows some optimization to our view , avoiding validations when mRecyclerAdapter content changes
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            ) //it can be GridLayoutManager or StaggeredGridLayoutManager
            isNestedScrollingEnabled = false
            //        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)); // Divider decorations
            adapter = recyclerAdapter
        }

        recyclerAdapter.setOnContentClickListener { modelClass, index ->
            Log.d(TAG, index.toString())
            onItemClick(modelClass)
        }
    }

    /**
     * Handles the recycler view item clicks
     */
    private fun onItemClick(modelClass: ModelClass) {
        // Here we start our detailed activity and pass the values of the clicked item into it
        val detailedActivityIntent =
            Intent(applicationContext, DetailedActivity::class.java)
        detailedActivityIntent.putExtra(
            ITEM,
            modelClass
        ) // Get the position of the clicked item
        startActivity(detailedActivityIntent)
    }

    /**
     * Function to put dummy data into the recyclerView
     */
    private fun dummyData() { //dummy data
        val mListItems: MutableList<ModelClass> = ArrayList()
        for (i in 0..9) {
            val listItem: ModelClass = if (i % 2 == 0) {
                ModelClass(
                    "Heading $i", "Dummy description", 0, i
                )
            } else {
                ModelClass(
                    "Heading $i", "Dummy description", 1, i
                )
            }
            mListItems.add(listItem)
            /** adding dummy data into the List */
        }
        //dummy data ends here
        recyclerAdapter.submitListItem(mListItems)
    }
    /**
     * For Pagination check : https://medium.com/@etiennelawlor/pagination-with-recyclerview-1cb7e66a502b
     */
    /**Adding item into a position of RecyclerView */
    fun add(view: View?) {
        Log.d(TAG, "add: ")
        val position = binding.editText.text.toString().toInt()

        /**@params -> position to where we need to add the item, NOTE : It is optional
         * @params -> passing the values into the model class
         */
        val typeValue: Int //for to determine which recycler layoutOut view has to populate
        typeValue = if (position % 2 == 0) {
            0
        } else {
            1
        }
        val modelClass = ModelClass("Heading$position", " New Item", typeValue, position)
        recyclerAdapter.addOrReplaceItem(modelClass, position)
    }

    /**Removing an item from a position of RecyclerView */
    fun remove(view: View?) {
        Log.d(TAG, "remove: ")
        val position = binding.editText.text.toString().toInt()
        /** @params -> position to where we need to add the item
         */
        recyclerAdapter.removeItem(position)
    }

    /**
     * Swipe Right gesture to remove the item from the recycler view
     */
    private fun gesture() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                recyclerAdapter.removeItem(viewHolder.adapterPosition) // Passing the position into the remove function
            }
        }).attachToRecyclerView(recyclerView) // Attach it to the recycler view
    }

    companion object {
        private const val TAG = "MainActivity"

        //Key constants for intentExtra
        const val ITEM = "item"
    }
}