package com.example.wecom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wecom.R
import com.example.wecom.db.Exercise
import com.example.wecom.others.AppUtility
import kotlinx.android.synthetic.main.main_recycler_card_s.view.*
import java.text.SimpleDateFormat
import java.util.*

// adapter for the home fragment recycler 
class  ExerciseRecycAdapter : RecyclerView.Adapter<ExerciseRecycAdapter.RunViewHolder>() {

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val diffCallback = object : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Exercise>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.main_recycler_card_s,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {

  val exercise = differ.currentList[position]
        holder.itemView.apply {

            val calendar = Calendar.getInstance().apply {
                timeInMillis = exercise.exerciseDate
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            date_recycler_card.text = dateFormat.format(calendar.time)

            val avgSpeed = "${exercise.averageSpeed}km/h"
            speed_recycler_card.text = avgSpeed

            val distanceInKm = "${exercise.distanceInMeters / 1000f}km"
            totalKm_recycler_card.text = distanceInKm

            time_taken_recycler_card.text = AppUtility.createFormattedTime(exercise.exerciseTimeInMillis)

            val caloriesBurned = "${exercise.caloriesBurned}kcal"
            calori_recycler_card.text = caloriesBurned
        }

    }
}

