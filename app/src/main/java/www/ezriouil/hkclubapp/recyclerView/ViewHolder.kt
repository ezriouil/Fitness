package www.ezriouil.hkclubapp.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import www.ezriouil.hkclubapp.R

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardViewFullName = itemView.findViewById(R.id.cardViewFullName) as TextView
    val cardViewPrice = itemView.findViewById(R.id.cardViewPrice) as TextView
    val cardViewGender = itemView.findViewById(R.id.cardViewGender) as TextView
    val cardViewTime = itemView.findViewById(R.id.cardViewTime) as TextView

}