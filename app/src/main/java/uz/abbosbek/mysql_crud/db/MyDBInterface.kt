package uz.abbosbek.mysql_crud.db

import uz.abbosbek.mysql_crud.models.MyContact

interface MyDBInterface {

    fun addContact(myContact: MyContact)

    fun getAllContact():ArrayList<MyContact>

    fun deleteContact(myContact: MyContact)

    fun updateContact(myContact: MyContact)
}