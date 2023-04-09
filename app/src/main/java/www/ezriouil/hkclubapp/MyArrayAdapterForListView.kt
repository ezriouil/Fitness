package www.ezriouil.hkclubapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyArrayAdapterForListView(context: Context,listOfClient: List<Client>): ArrayAdapter<Client>(context,0,listOfClient) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context).inflate(R.layout.item_view_for_notification,parent,false)
        inflater.findViewById<TextView>(R.id.cardViewFullNameOfNotification).text = getItem(position)!!.fullName.uppercase()
        return inflater
    }
}
