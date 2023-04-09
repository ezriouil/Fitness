package www.ezriouil.hkclubapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import www.ezriouil.hkclubapp.*
import www.ezriouil.hkclubapp.databinding.HomeBinding
import www.ezriouil.hkclubapp.recyclerView.MyAdapterForRecyclerView
import www.ezriouil.hkclubapp.recyclerView.RecyclerListener
import www.ezriouil.hkclubapp.sql.DB
import www.ezriouil.hkclubapp.sql.DataBase
import www.ezriouil.hkclubapp.sql.DataBase.Companion.readDB
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class Home : Fragment() , RecyclerListener {
    private lateinit var myAdapterForRecyclerView: MyAdapterForRecyclerView
    private lateinit var binding: HomeBinding
    private lateinit var dataBase: DataBase
    @SuppressLint("Recycle")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeBinding.inflate(inflater)
        dataBase = DataBase(requireContext())
        sale = readDB(dataBase)
        myAdapterForRecyclerView = MyAdapterForRecyclerView(requireContext(), sale,this)
        myAdapterForRecyclerView.updateMyData(sale)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myAdapterForRecyclerView = MyAdapterForRecyclerView(requireContext(),sale,this)
        binding.myRecyclerView.adapter  = myAdapterForRecyclerView
        binding.mySearchView.setOnQueryTextListener(object  : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val newListOfClient = mutableListOf<Client>()
                for (item in sale)   if (item.fullName.lowercase().contains(newText!!.lowercase()))   newListOfClient.add(item)
                if(newListOfClient.isEmpty()) Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show()
                else myAdapterForRecyclerView.updateMyData(newListOfClient)
                return true
            }
        })
    }

    override fun addClient(client: Client) = TODO()
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged", "SimpleDateFormat")
    override fun cardOfClient(client: Client,index:Int) {
        val sheetDialogChoice = BottomSheetDialog(requireContext(),R.style.bottomSheetStyle)
        val viewOfChoice = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_choix,view?.findViewById(R.id.bottom_sheet_choix))
        //CLOSE
        viewOfChoice.findViewById<ImageView>(R.id.close_bottom_sheet_choix).setOnClickListener {
            sheetDialogChoice.dismiss()
        }
        // EDIT
        viewOfChoice.findViewById<Button>(R.id.edit_bottom_sheet_choix).setOnClickListener {
            val sheetDialog = BottomSheetDialog(requireContext(),R.style.bottomSheetStyle)
            val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_edit,view?.findViewById(R.id.bottom_sheet_design))
            view.findViewById<ImageView>(R.id.close_bottom_sheet).setOnClickListener{
                sheetDialog.dismiss()
            }
            val name = view.findViewById<TextInputEditText>(R.id.newUser)
            val price = view.findViewById<TextInputEditText>(R.id.newPrice)
            val yearOfBottomSheet = view.findViewById<NumberPicker>(R.id.newYear)
            val monthOfBottomSheet = view.findViewById<NumberPicker>(R.id.newMonth)
            val dayOfBottomSheet = view.findViewById<NumberPicker>(R.id.newDay)
            val hourOfBottomSheet = view.findViewById<NumberPicker>(R.id.newHour)
            name.setText(client.fullName)
            price.setText(client.price.toString())
            yearOfBottomSheet.apply {
                minValue = 2022
                maxValue = 2032
                value = LocalDateTime.now().year
            }
            monthOfBottomSheet.apply {
                minValue = 1
                maxValue = 12
                value = LocalDateTime.now().monthValue+1
            }
            dayOfBottomSheet.apply {
                minValue = 1
                maxValue = 31
                value = LocalDateTime.now().dayOfMonth
            }
            hourOfBottomSheet.apply {
                minValue = 1
                maxValue = 24
                value = LocalDateTime.now().hour + 1
            }
            view.findViewById<Button>(R.id.btn_ok_edit).setOnClickListener {
                if (name.text!!.isNotBlank() && price.text!!.isNotBlank()){
                    val myDate = "${dayOfBottomSheet.value}/${monthOfBottomSheet.value}/${yearOfBottomSheet.value} ${hourOfBottomSheet.value}:${LocalDateTime.now().minute}:${LocalDateTime.now().second}"
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    val date = sdf.parse(myDate)
                    val millis = date!!.time
                    dataBase.updateItemInDB(name.text.toString(),price.text.toString().toInt(),client.time.toString(),millis)
                    sale[index].fullName = name.text.toString()
                    sale[index].price = price.text.toString().toInt()
                    sale[index].time = millis
                    Toast.makeText(requireContext(),"has been modifed successfully ${client.fullName.uppercase()}",Toast.LENGTH_SHORT).show()
                    myAdapterForRecyclerView.notifyDataSetChanged()
                    if (sale_notification.contains(client)){sale_notification.remove(client)}
                    sheetDialog.dismiss()
                }
                else Toast.makeText(requireContext(),"error in modifcation ${client.fullName.uppercase()} ",Toast.LENGTH_SHORT).show()
            }
            sheetDialog.setCancelable(false)
            sheetDialog.setContentView(view)
            sheetDialog.show()
            sheetDialogChoice.dismiss()
        }
        //REMOVE
        viewOfChoice.findViewById<Button>(R.id.remove_bottom_sheet_choix).setOnClickListener {
            val sheetDialogRemove = BottomSheetDialog(requireContext(),R.style.bottomSheetStyle)
            val viewOfRemove = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_remove,view?.findViewById(R.id.bottom_sheet_remove))
            viewOfRemove.findViewById<ImageView>(R.id.close_bottom_sheet_remove).setOnClickListener{
                sheetDialogRemove.dismiss()
            }
            viewOfRemove.findViewById<Button>(R.id.remove_bottom_sheet_remove_Yes).setOnClickListener{
                delete(index)
                if (sale_notification.contains(client)){ sale_notification.remove(client) }
                DataBase(requireContext()).deleteFromDB(client.time.toString())
                myAdapterForRecyclerView.updateMyData(sale)
                Toast.makeText(requireContext(),"${client.fullName.uppercase()} is deleted",Toast.LENGTH_SHORT).show()
                sheetDialogRemove.dismiss()
            }
            viewOfRemove.findViewById<Button>(R.id.edit_bottom_sheet_remove_No).setOnClickListener{
                Toast.makeText(requireContext(),"${client.fullName.uppercase()} is not deleted",Toast.LENGTH_SHORT).show()
                sheetDialogRemove.dismiss()
            }
            sheetDialogRemove.setCancelable(false)
            sheetDialogRemove.setContentView(viewOfRemove)
            sheetDialogRemove.show()
            sheetDialogChoice.dismiss()
        }
        sheetDialogChoice.setCancelable(false)
        sheetDialogChoice.setContentView(viewOfChoice)
        sheetDialogChoice.show()
    }
}