package www.ezriouil.hkclubapp.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import www.ezriouil.hkclubapp.R

class MyViewHolderforRecyclerView(itemView:View) : RecyclerView.ViewHolder(itemView) {
    var cardViewFullName: TextView
    var cardViewPrice: TextView
    var cardViewGender: TextView
    var cardViewTime: TextView
    init {
        cardViewFullName = itemView.findViewById(R.id.cardViewFullName) as TextView
        cardViewPrice = itemView.findViewById(R.id.cardViewPrice) as TextView
        cardViewGender = itemView.findViewById(R.id.cardViewGender) as TextView
        cardViewTime = itemView.findViewById(R.id.cardViewTime) as TextView
    }
}