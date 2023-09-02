package www.ezriouil.hkclubapp.recyclerView

import www.ezriouil.hkclubapp.Client

interface Listener {
    fun addClient(client: Client)
    fun cardOfClient(client: Client,index:Int)
}