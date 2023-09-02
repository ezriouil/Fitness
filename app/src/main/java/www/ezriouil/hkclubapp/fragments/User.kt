package www.ezriouil.hkclubapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import www.ezriouil.hkclubapp.Client
import www.ezriouil.hkclubapp.add
import www.ezriouil.hkclubapp.databinding.UserBinding
import www.ezriouil.hkclubapp.recyclerView.Adapter
import www.ezriouil.hkclubapp.recyclerView.Listener
import www.ezriouil.hkclubapp.sale
import www.ezriouil.hkclubapp.sql.DataBase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime

class User : Fragment() , Listener{

    private lateinit var binding: UserBinding
    private lateinit var adapter: Adapter
    private lateinit var dataBase: DataBase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = UserBinding.inflate(inflater)
        return binding.root
    }
    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = Adapter(requireContext(),sale,this)
        dataBase = DataBase(requireContext())
        //Menu Gender
        binding.EditTextGender.setAdapter(ArrayAdapter(requireContext(),android.R.layout.select_dialog_item,listOf("MEN","WOMEN")))
        //Number Picker
        numberPicker()
        //Onclick In Button Ok
        binding.btnOk.setOnClickListener {
            if (binding.EditTextFullName.text.toString().isNotBlank() && binding.EditTextPrice.text.toString().isNotBlank()){
                val myDate = "${binding.numberPickerDay.value}/${binding.numberPickerMonth.value}/${binding.numberPickerYear.value} ${binding.numberPickerHour.value}:${LocalDateTime.now().minute}:${LocalDateTime.now().second}"
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val date = sdf.parse(myDate)
                val millis = date!!.time
                Log.d("a",LocalDateTime.now().second.toString())
                dataBase.insertToDB(binding.EditTextFullName.text.toString(),binding.EditTextPrice.text.toString().toInt(),binding.EditTextGender.text.toString(),millis.toString())
                Toast.makeText(requireContext(),"${binding.EditTextFullName.text.toString()} added successfully",Toast.LENGTH_SHORT).show()
                binding.EditTextFullName.text?.clear()
                binding.EditTextPrice.text?.clear()
            }else Toast.makeText(requireContext(),"error adding ${binding.EditTextFullName.text.toString()}",Toast.LENGTH_SHORT).show()
        }
    }
    private fun numberPicker() {
        binding.numberPickerYear.apply {
            minValue = LocalDate.now().year
            maxValue = LocalDate.now().year + 10
            value = LocalDate.now().year }
        binding.numberPickerMonth.apply {
            minValue = 1
            maxValue = 12
            value = LocalDate.now().monthValue }
        binding.numberPickerDay.apply {
            minValue = 1
            maxValue = 31
            value = LocalDate.now().dayOfMonth }
        binding.numberPickerHour.apply {
            minValue = 1
            maxValue = 24
            value = LocalDateTime.now().hour + 1
        }
    }
    override fun addClient(client: Client) {
        add(client)
        adapter.updateMyData(sale)
    }
    override fun cardOfClient(client: Client,index:Int){TODO()}
}