package uz.abbosbek.mysql_crud.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.abbosbek.mysql_crud.db.MyConstant.DB_NAME
import uz.abbosbek.mysql_crud.db.MyConstant.ID
import uz.abbosbek.mysql_crud.db.MyConstant.NAME
import uz.abbosbek.mysql_crud.db.MyConstant.NUMBER
import uz.abbosbek.mysql_crud.db.MyConstant.TABLE_NAME
import uz.abbosbek.mysql_crud.db.MyConstant.VERSION
import uz.abbosbek.mysql_crud.models.MyContact

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION), MyDBInterface {
    /** Database ni yaratib berish ushun ishlatiladi*/
    override fun onCreate(p0: SQLiteDatabase?) {
        val query =
            "create table $TABLE_NAME (id integer not null primary key autoincrement unique, $NAME text not null, $NUMBER text not null)"
        p0?.execSQL(query)
    }
    /** Database ni yangilab uchun ishlatiladi */
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    /** Database ga yangi ma'lumot qo'shish uchun ishlatiladi <bu funksiyalarni o'zimiz yaratamiz yaratayotgan dasturimizni bajaradigan ishlariga qarab>*/
    override fun addContact(myContact: MyContact) {

        /** writableDatabase bu funksiya database ga malumotlarni yozib beradi */
        val database = this.writableDatabase
        /** ContentValues ma'lumotlarni qo'shishimiz uchun kerak bo'ladi */
        val contentValues = ContentValues()
        contentValues.put(NAME, myContact.name)
        contentValues.put(NUMBER, myContact.number)
        database.insert(TABLE_NAME, null, contentValues)
        database.close()
    }

    /** Database dagi ma'lumotlarni o'qib oladi */
    override fun getAllContact(): ArrayList<MyContact> {
        val list=ArrayList<MyContact>()

        val query = "select * from $TABLE_NAME"
        /** readableDatabase ma'lumotlarni o'qib beradi*/
        val database = this.readableDatabase
        /** cursor database dagi ma'lumotlarni qatorma qator o'qib beradi*/
        val cursor = database.rawQuery(query,null)
        /** <moveToFirst> cursor ni qatorni birinchisiga ko'chiradi*/
        if (cursor.moveToFirst()){
            do {
                /** bu yerda <do while> dan foydalanib ma'lumotlarni qatorma qator o'qib oladi... */
                val myContact = MyContact(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                /** bu yerda o'qilgan ma'lumotlarni list ga qo'shib oladi*/
                list.add(myContact)
                /** qachonki ohirgi qatorga kelgandagina yani <moveToFirst> qator tugaganda bu sikl operatoridan chiqadi */
            }while (cursor.moveToNext())
        }
        return list
    }

    override fun deleteContact(myContact: MyContact) {
        val database = this.writableDatabase
        database.delete(TABLE_NAME,"id=?", arrayOf(myContact.id.toString()))
        database.close()
    }

    override fun updateContact(myContact: MyContact) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, myContact.id)
        contentValues.put(NAME, myContact.name)
        contentValues.put(NUMBER, myContact.number)
    }

}