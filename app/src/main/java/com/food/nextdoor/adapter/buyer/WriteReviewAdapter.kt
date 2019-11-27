package com.food.nextdoor.adapter.buyer

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.food.nextdoor.R
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.model.*
import kotlinx.android.synthetic.main.activity_launch.*
import kotlinx.android.synthetic.main.write_review_row.view.*
import system.Preference


class WriteReviewAdapter(val Dishes: List<Dishe>,val chefId:Int) :RecyclerView.Adapter<WriteReviewViewHolder>() {
    lateinit var  tagList: ArrayList<ReviewTag>
    lateinit var reviews:Array<WriteReviewPost?>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteReviewViewHolder {
        tagList=NextDoorDB.invoke(parent.context).daoAccess.getReviewTagList() as ArrayList<ReviewTag>
        val inflater=LayoutInflater.from(parent.context)
        val rootView= inflater.inflate(R.layout.write_review_row,parent,false)
        reviews= arrayOfNulls(Dishes.size)
        return WriteReviewViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return Dishes.size
    }

    override fun onBindViewHolder(holder: WriteReviewViewHolder, position: Int) {
        holder.writeReviewView.tv_dish_name_write_review_row_slr.text=Dishes[position].DishName
        val reviewPost=WriteReviewPost()
        reviewPost.DishId=Dishes[position].DishId
        reviewPost.ChefId=chefId
        reviewPost.BuyerId= Preference(holder.writeReviewView.context).getUserId()
        holder.writeReviewView.tv_discription_write_review_row_slr.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                reviewPost.ReviewNote=p0.toString()
                reviews[position]=reviewPost
            }

        })
        val citiAdapter = ArrayAdapter<ReviewTag>(holder.writeReviewView.context, R.layout.time_slot_row, tagList)
        holder.writeReviewView.spinner_tags__write_review_row_slr.adapter=citiAdapter
        holder.writeReviewView.spinner_tags__write_review_row_slr.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                reviewPost.TagId=(p0?.getItemAtPosition(p2) as ReviewTag).reviewTagId
                reviews[position]=reviewPost
            }


        }
        reviewPost.TagId=(holder.writeReviewView.spinner_tags__write_review_row_slr.selectedItem as ReviewTag).reviewTagId
        reviews[position]=reviewPost

    }
    fun retriveReviews():Array<WriteReviewPost?>{
        return reviews
    }
}

class WriteReviewViewHolder(val writeReviewView: View):RecyclerView.ViewHolder (writeReviewView)
