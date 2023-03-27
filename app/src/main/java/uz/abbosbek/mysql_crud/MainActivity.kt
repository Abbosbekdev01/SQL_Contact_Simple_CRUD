package uz.abbosbek.mysql_crud

import android.app.Activity
import android.app.ActivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import uz.abbosbek.mysql_crud.adapters.MyContactAdapter
import uz.abbosbek.mysql_crud.adapters.RvAction
import uz.abbosbek.mysql_crud.databinding.ActivityMainBinding
import uz.abbosbek.mysql_crud.databinding.ItemDialogBinding
import uz.abbosbek.mysql_crud.db.MyDbHelper
import uz.abbosbek.mysql_crud.models.MyContact

class MainActivity : AppCompatActivity(), RvAction {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var myContactAdapter: MyContactAdapter
    private lateinit var myDbHelper: MyDbHelper

    /** ma'lumotlarni o'qib olish uchun list olsmiz (MyContact) tipida*/
    private lateinit var list: ArrayList<MyContact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()

        binding.apply {

            btnAdd.setOnClickListener {
                val dialog = AlertDialog.Builder(this@MainActivity).create()
                val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
                dialog.setView(itemDialogBinding.root)
                dialog.show()

                itemDialogBinding.btnSave.setOnClickListener {
                    /** bu yerda btnSave bosilganda kiritilgan ma'lumotlarni saqlashi kerak*/
                    val newContact = MyContact(
                        itemDialogBinding.addName.text.toString().trim(),
                        itemDialogBinding.addNumber.text.toString()
                    )
                    if (newContact.name!!.isNotEmpty() && newContact.number!!.isNotEmpty()) {
                        /** yangi kiritilgan ma'lumotni Database ga saqlab olamiz*/
                        myDbHelper.addContact(newContact)
                        /** qo'shilgan ma'lumotlarnimizni Database dan o'qib olishi uchun <loadData ni chaqirib qo'yamiz>*/
                        loadData()
//                    /** yangi qo'shilgan ma'lumot oynada ko'rinishi uchun <operativ hotiradagi ma'lumotlarni ham yangilab qo'yamiz>*/
//                    list.add(newContact)
//                    /** bu yerda yangi qo'shilgan ma'lumotni <adapterga qo'shib olamiz> va yangilaniz buyrugini beramiz, yangi ma'lumot qatorni ohiriga qo'shilishi haqida buyruq beramiz*/
//                    myContactAdapter.notifyItemInserted(list.size - 1)
                        dialog.cancel()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Ma'lumot to'liq kiritilmadi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

//            /** bu yerda <btnSave> bosilganda yangi qo'shilgan ma'lumotlarni Database ga qo'shib olamiz*/
//            btnSave.setOnClickListener {
//
//                val newContact = MyContact(
//                    addName.text.toString(),
//                    addNumber.text.toString()
//                )
//                if (newContact.name!!.isNotEmpty() && newContact.number!!.isNotEmpty()) {
//                    /** Qo'shilgan ma'lumotlarni Database ga saqlaymiz*/
//                    myDbHelper.addContact(newContact)
//                    /** yangi qo'shilgan ma'lumotlarni listga ham saqlab olamiz*/
//                    list.add(newContact)
//                    /** bu yerda yangi qo'shilgan ma'lumotlar ekranda qo'shgan zohoti ko'rinishi uchun Adapter ni yangi ma'lumot qoshilganligi haqida habar beramiz*/
//                    myContactAdapter.notifyItemInserted(list.size - 1)
//                    addName.text?.clear()
//                    addNumber.text?.clear()
//                    Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Ma'lumot to'liq kiritilmadi",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
        }
    }

    /** loadData ni vazifasi Database dan ma'lumotlarni o'qib RecycleView chiqarib beradi */
    private fun loadData() {
        myDbHelper = MyDbHelper(this)
        list = ArrayList()
        /** Database dagi ma'lumotlarni listga qo'shib oldi*/
        list.addAll(myDbHelper.getAllContact())

        myContactAdapter = MyContactAdapter(list, this)
        binding.rv.adapter = myContactAdapter

    }

    override fun deleteContact(deleteContact: MyContact, position: Int) {
        myDbHelper.deleteContact(deleteContact)
        list.remove(deleteContact)
        myContactAdapter.notifyItemRemoved(position)

        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun updateContact(contact: MyContact, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
        /** bu yerda oldin kiritilgan ma'lumotlarni o'qib oladi o'zgartirish uchun <setText> dan foydalangan holda*/
        itemDialogBinding.addName.setText(contact.name)
        itemDialogBinding.addNumber.setText(contact.number)

        itemDialogBinding.btnSave.setOnClickListener {
            /** endi oldin kiritilgan ma'lumotni o'zgartirildi va o'zgartirilgan ma'lumotni Database ga o'qib olyabti*/
            contact.name = itemDialogBinding.addName.text.toString()
            contact.number = itemDialogBinding.addNumber.text.toString()

            if (contact.name!!.isNotEmpty() && contact.number!!.isNotEmpty()) {
                /** o'zgartirilgan ma'lumotni Database ga yozib oldi*/
                myDbHelper.updateContact(contact)
                list[position] = contact
                /** kiritilgan ma'lumot RecycleView da xam o'zgarishi uchun Adapterni xam position ni bo'yicha yangilaymiz*/
                myContactAdapter.notifyItemChanged(position)
                dialog.cancel()
            } else {
                Toast.makeText(this, "Ma'lumot to'liq kiritilmadi", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.setView(itemDialogBinding.root)
        dialog.show()
    }
}