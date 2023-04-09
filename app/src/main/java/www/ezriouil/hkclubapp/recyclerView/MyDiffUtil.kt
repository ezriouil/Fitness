package www.ezriouil.hkclubapp.recyclerView

import androidx.recyclerview.widget.DiffUtil
import www.ezriouil.hkclubapp.Client

class MyDiffUtil(private val myOldListClient:List<Client>,private val myNewListClient:List<Client>) : DiffUtil.Callback() {

    override fun getOldListSize() = myOldListClient.size

    override fun getNewListSize() = myNewListClient.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return myOldListClient[oldItemPosition].fullName == myNewListClient[newItemPosition].fullName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            myOldListClient[oldItemPosition].fullName != myNewListClient[newItemPosition].fullName -> false
            myOldListClient[oldItemPosition].price != myNewListClient[newItemPosition].price -> false
            myOldListClient[oldItemPosition].gender != myNewListClient[newItemPosition].gender -> false
            else -> {true}
        }
    }
}