package www.ezriouil.gym.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import www.ezriouil.gym.R
import www.ezriouil.gym.databinding.HomeBinding
import www.ezriouil.gym.local.model.Client
import www.ezriouil.gym.local.model.sale
import www.ezriouil.gym.local.model.sale_notification
import www.ezriouil.gym.local.sql.DataBase
import www.ezriouil.gym.recyclerView.Adapter
import www.ezriouil.gym.recyclerView.Listener
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class Home : Fragment(), Listener {

    private lateinit var adapter: Adapter
    private lateinit var binding: HomeBinding
    private lateinit var dataBase: DataBase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeBinding.inflate(inflater)
        dataBase = DataBase(requireContext())
        sale.clear()
        sale_notification.clear()
        GlobalScope.launch {
            dataBase.readDB_ByFlow().collect { clients ->
                sale.addAll(clients)
                for (item in clients){
                    if (item.time - System.currentTimeMillis()<0){
                        sale_notification.add(item)
                    }
                }
            }
        }
        adapter = Adapter(requireContext(), sale, this)
        adapter.updateMyData(sale)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = Adapter(requireContext(), sale, this)
        binding.myRecyclerView.adapter = adapter
    }

    override fun addClient(client: Client) = TODO()

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged", "SimpleDateFormat")
    override fun cardOfClient(client: Client, index: Int) {
        val sheetDialogChoice = BottomSheetDialog(requireContext(), R.style.bottomSheetStyle)
        val viewOfChoice = LayoutInflater.from(context)
            .inflate(R.layout.bottom_sheet_choix, view?.findViewById(R.id.bottom_sheet_choix))
        //CLOSE
        viewOfChoice.findViewById<ImageView>(R.id.close_bottom_sheet_choix).setOnClickListener {
            sheetDialogChoice.dismiss()
        }
        // EDIT
        viewOfChoice.findViewById<Button>(R.id.edit_bottom_sheet_choix).setOnClickListener {
            val sheetDialog = BottomSheetDialog(requireContext(), R.style.bottomSheetStyle)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_edit, view?.findViewById(R.id.bottom_sheet_design))
            view.findViewById<ImageView>(R.id.close_bottom_sheet).setOnClickListener {
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
                value = LocalDateTime.now().monthValue + 1
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
                if (name.text!!.isNotBlank() && price.text!!.isNotBlank()) {
                    val myDate =
                        "${dayOfBottomSheet.value}/${monthOfBottomSheet.value}/${yearOfBottomSheet.value} ${hourOfBottomSheet.value}:${LocalDateTime.now().minute}:${LocalDateTime.now().second}"
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                    val date = sdf.parse(myDate)
                    val millis = date!!.time
                    dataBase.updateItemInDB(
                        name.text.toString(),
                        price.text.toString().toInt(),
                        client.time.toString(),
                        millis
                    )
                    adapter.notifyDataSetChanged()
                    Toast.makeText(
                        requireContext(),
                        "has been modified successfully ${client.fullName.uppercase()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (sale_notification.contains(client)) {
                        sale_notification.remove(client)
                    }
                    sheetDialog.dismiss()
                } else Toast.makeText(
                    requireContext(),
                    "error in modifcation ${client.fullName.uppercase()} ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            sheetDialog.setCancelable(false)
            sheetDialog.setContentView(view)
            sheetDialog.show()
            sheetDialogChoice.dismiss()
        }
        //REMOVE
        viewOfChoice.findViewById<Button>(R.id.remove_bottom_sheet_choix).setOnClickListener {
            val sheetDialogRemove = BottomSheetDialog(requireContext(), R.style.bottomSheetStyle)
            val viewOfRemove = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_remove, view?.findViewById(R.id.bottom_sheet_remove))
            viewOfRemove.findViewById<ImageView>(R.id.close_bottom_sheet_remove)
                .setOnClickListener {
                    sheetDialogRemove.dismiss()
                }
            viewOfRemove.findViewById<Button>(R.id.remove_bottom_sheet_remove_Yes)
                .setOnClickListener {
                    if (sale_notification.contains(client)) {
                        sale_notification.remove(client)
                    }
                    GlobalScope.launch {
                        DataBase(requireContext()).deleteFromDB(client.time.toString())
                    }
                    adapter.updateData(sale)
                    Toast.makeText(
                        requireContext(),
                        "${client.fullName.uppercase()} is deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    sheetDialogRemove.dismiss()
                }
            viewOfRemove.findViewById<Button>(R.id.edit_bottom_sheet_remove_No).setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "${client.fullName.uppercase()} is not deleted",
                    Toast.LENGTH_SHORT
                ).show()
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