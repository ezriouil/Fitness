package www.ezriouil.gym.local.model

data class Client(var fullName: String, var price: Int, var gender: String, var time: Long)

var sale = mutableListOf<Client>()

val sale_notification = mutableListOf<Client>()

