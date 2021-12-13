package com.azhar.banksampah.view.input

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.azhar.banksampah.R
import com.azhar.banksampah.utils.FunctionHelper.rupiahFormat
import com.azhar.banksampah.viewmodel.InputDataViewModel
import kotlinx.android.synthetic.main.activity_input_data.*
import java.text.SimpleDateFormat
import java.util.*

class InputDataActivity : AppCompatActivity() {

    lateinit var inputDataViewModel: InputDataViewModel
    lateinit var strNama: String
    lateinit var strTanggal: String
    lateinit var strAlamat: String
    lateinit var strCatatan: String
    lateinit var strKategoriSelected: String
    lateinit var strHargaSelected: String
    lateinit var strKategori: Array<String>
    lateinit var strHarga: Array<String>
    var countTotal = 0
    var countBerat = 0
    var countHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)
        setStatusBar()
        setToolbar()
        setInitLayout()
        setInputData()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun setInitLayout() {
        strKategori = resources.getStringArray(R.array.kategori_sampah)
        strHarga = resources.getStringArray(R.array.harga_perkilo)

        inputDataViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(InputDataViewModel::class.java)

        val arrayBahasa = ArrayAdapter(this@InputDataActivity, android.R.layout.simple_list_item_1, strKategori)
        arrayBahasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKategori.setAdapter(arrayBahasa)

        spKategori.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                strKategoriSelected = parent.getItemAtPosition(position).toString()
                strHargaSelected = strHarga[position]
                spKategori.setEnabled(true)
                countHarga = strHargaSelected.toInt()
                if (inputBerat.getText().toString() != "") {
                    countBerat = inputBerat.getText().toString().toInt()
                    setTotalPrice(countBerat)
                } else {
                    inputHarga.setText(rupiahFormat(countHarga))
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })

        inputBerat.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(editable: Editable) {
                inputBerat.removeTextChangedListener(this)
                if (editable.length > 0) {
                    countBerat = editable.toString()
                    setTotalPrice(countBerat)
                } else {
                    inputHarga.setText(rupiahFormat(countHarga))
                }
                inputBerat.addTextChangedListener(this)
            }
        })

        inputTanggal.setOnClickListener { view: View? ->
            val tanggalJemput = Calendar.getInstance()
            val date =
                OnDateSetListener { view1: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    tanggalJemput[Calendar.YEAR] = year
                    tanggalJemput[Calendar.MONTH] = monthOfYear
                    tanggalJemput[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val strFormatDefault = "d MMMM yyyy"
                    val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
                    inputTanggal.setText(simpleDateFormat.format(tanggalJemput.time))
                }
            DatePickerDialog(
                this@InputDataActivity, date,
                tanggalJemput[Calendar.YEAR],
                tanggalJemput[Calendar.MONTH],
                tanggalJemput[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    private fun setTotalPrice(berat: Int) {
        countTotal = countHarga * berat
        inputHarga.setText(rupiahFormat(countTotal))
    }

    private fun setInputData() {
        btnCheckout.setOnClickListener { v: View? ->
            strNama = inputNama.text.toString()
            strTanggal = inputTanggal.text.toString()
            strAlamat = inputAlamat.text.toString()
            strCatatan = inputTambahan.text.toString()
            if (strNama.isEmpty() or strTanggal.isEmpty() or strAlamat.isEmpty() or (strKategori.size == 0) or (countBerat == 0) or (countHarga == 0)) {
                Toast.makeText(
                    this@InputDataActivity,
                    "Data tidak boleh ada yang kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                inputDataViewModel.addDataSampah(
                    strNama,
                    strKategoriSelected,
                    countBerat,
                    countHarga,
                    strTanggal,
                    strAlamat,
                    strCatatan
                )
                Toast.makeText(
                    this@InputDataActivity,
                    "Pesanan Anda sedang diproses, cek di menu riwayat",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) {
                layoutParams.flags = layoutParams.flags or bits
            } else {
                layoutParams.flags = layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }

}