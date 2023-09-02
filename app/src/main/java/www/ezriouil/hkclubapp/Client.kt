package www.ezriouil.hkclubapp

data class Client(var fullName:String,var price:Int,var gender:String,var time:Long)

private val sale_private = mutableListOf<Client>()

var sale : List<Client> = sale_private.toList()

val sale_notification = mutableListOf<Client>()

fun add(client:Client) = sale_private.add(client)
fun delete(client: Client) = sale_private.remove(client)

