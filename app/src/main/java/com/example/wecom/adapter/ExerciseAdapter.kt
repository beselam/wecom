import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wecom.R
import com.example.wecom.firestore.ExerciseFstore
import kotlinx.android.synthetic.main.main_recycler_card_s.view.*
import kotlinx.android.synthetic.main.main_recycler_card_winner.view.*
// an adapter for the group class
class ExerciseAdapter: RecyclerView.Adapter<ExerciseAdapter.MyViewHolder>() {
    //inner view holder class
    var list = mutableListOf<ExerciseFstore>()
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  inflater = LayoutInflater.from(parent.context).inflate(R.layout.main_recycler_card_winner,parent,false)
        return MyViewHolder(inflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.apply {
            distance_winner_recycler_card.text = ((list[position].distanceInMeters)/1000f).toString() + "km"
            calorie_winner_recycler_card.text = list[position].caloriesBurned.toString()+"kcal"
        }

    }

    override fun getItemCount(): Int {
        return  list.size
    }

}