package www.ezriouil.hkclubapp.recyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import www.ezriouil.hkclubapp.*
import java.util.concurrent.TimeUnit

class MyAdapterForRecyclerView(val context: Context,private var myListOfClient: List<Client>,private val myListener: RecyclerListener) : RecyclerView.Adapter<MyViewHolderforRecyclerView>(){
    override fun getItemCount() = myListOfClient.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderforRecyclerView {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return  MyViewHolderforRecyclerView(inflater)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderforRecyclerView, position: Int) {
        val item = myListOfClient[holder.adapterPosition]
        holder.cardViewTime.setOnLongClickListener {
             myListener.cardOfClient(item,holder.adapterPosition)
            return@setOnLongClickListener true
        }
        holder.cardViewFullName.text = item.fullName.uppercase()
        holder.cardViewPrice.text = item.price.toString()
        holder.cardViewGender.text = item.gender
        val timeLeft = item.time - System.currentTimeMillis()
        val days = TimeUnit.MILLISECONDS.toDays(timeLeft)
        val hours = TimeUnit.MILLISECONDS.toHours(timeLeft) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60
        if (timeLeft <=0){
            if (!sale_notification.contains(item)){ sale_notification.add(item) }
            holder.cardViewTime.text = "FIN"
            //holder.cardViewTime.setTextColor(Color.RED)
        }
        else{
            holder.cardViewTime.text = String.format("%dD %dH %dM %dS", days, hours, minutes, seconds)
            //holder.cardViewTime.setTextColor(Color.GREEN)
        }
    }
    fun updateMyData(myNewListClient:List<Client>){
        val myDiffUtil = DiffUtil.calculateDiff(MyDiffUtil(myListOfClient, myNewListClient))
        myListOfClient = myNewListClient
        myDiffUtil.dispatchUpdatesTo(this)
    }


}