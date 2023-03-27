package uz.abbosbek.mysql_crud.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abbosbek.mysql_crud.databinding.ItemRvBinding
import uz.abbosbek.mysql_crud.models.MyContact

class MyContactAdapter(val list: List<MyContact>, val rvAction: RvAction): RecyclerView.Adapter<MyContactAdapter.VH>() {

    inner class VH( val itemRvBinding: ItemRvBinding):RecyclerView.ViewHolder(itemRvBinding.root){
        fun onBind(myContact: MyContact, position: Int){
            itemRvBinding.tvName.text = myContact.name
            itemRvBinding.tvNumber.text = myContact.number

            itemRvBinding.root.setOnLongClickListener {
                rvAction.deleteContact(myContact, position)
                true
            }

            itemRvBinding.root.setOnClickListener {
                rvAction.updateContact(myContact, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface RvAction{
    fun deleteContact(deleteContact: MyContact, position: Int)
    fun updateContact(contact: MyContact, position: Int)
}