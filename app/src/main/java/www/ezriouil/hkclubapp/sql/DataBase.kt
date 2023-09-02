package www.ezriouil.hkclubapp.sql

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import www.ezriouil.hkclubapp.Client

class DataBase(context: Context) : SQLiteOpenHelper(context, DB.NAME, null, DB.VERSION) {
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) = TODO()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(sql)
    }

    fun insertToDB(fullName: String, price: Int, gender: String, time: String) {
        val client = ContentValues().apply {
            put(DB.FULL_NAME, fullName)
            put(DB.PRICE, price.toString())
            put(DB.GENDER, gender)
            put(DB.TIME, time)
        }
        GlobalScope.launch {
            this@DataBase.writableDatabase.insert(DB.TABLE_NAME, null, client)
            this@DataBase.close()
        }
    }

    fun deleteFromDB(time: String) {
        val sql = this.writableDatabase
        GlobalScope.launch {
            sql.delete(DB.TABLE_NAME, "time=$time", null)
        }
    }

    @SuppressLint("Recycle")
    fun updateItemInDB(fullName: String, price: Int, clientName: String, newTime: Long) {
        val newPerson = ContentValues().apply {
            put(DB.FULL_NAME, fullName)
            put(DB.PRICE, price.toString())
            put(DB.TIME, newTime.toString())
        }
        GlobalScope.launch {
            this@DataBase.writableDatabase.update(
                DB.TABLE_NAME,
                newPerson,
                DB.TIME + "=?",
                arrayOf(clientName)
            )
        }

    }

    val sql = "CREATE TABLE ${DB.TABLE_NAME} (" +
            //"${DB.ID} INTEGER  PRIMARY KEY AUTOINCREMENT," +
            "${DB.FULL_NAME} TEXT," +
            "${DB.PRICE} INT," +
            "${DB.GENDER} TEXT," +
            "${DB.TIME} TEXT " +
            ")"

    fun readDB(): List<Client> {
        val data = ArrayList<Client>()
        GlobalScope.launch(Dispatchers.IO) {
            val cursor =
                this@DataBase.readableDatabase.rawQuery("SELECT * FROM ${DB.TABLE_NAME}", null)
            while (cursor.moveToNext()) {
                val fullName = cursor.getString(0)
                val price = cursor.getInt(1)
                val gender = cursor.getString(2)
                val time = cursor.getString(3)
                val client = Client(fullName, price, gender, time.toLong())
                data.add(client)
            }
            cursor.close()
        }

        return data
    }
}