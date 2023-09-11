package www.ezriouil.gym.recyclerView

import www.ezriouil.gym.local.model.Client

interface Listener {
    fun addClient(client: Client)
    fun cardOfClient(client: Client, index: Int)
}