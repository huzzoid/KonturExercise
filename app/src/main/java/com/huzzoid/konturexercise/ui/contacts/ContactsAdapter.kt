package com.huzzoid.konturexercise.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.huzzoid.konturexercise.databinding.ItemContactBinding
import com.huzzoid.konturexercise.domain.pojo.Contact

class ContactsAdapter(var onItemClick: ((Contact) -> Unit)) :
    RecyclerView.Adapter<ContactViewHolder>() {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
                oldItem.id == newItem.id
        }
    }

    private val differ: AsyncListDiffer<Contact> = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { ContactViewHolder(this) { onItemClick.invoke(differ.currentList[it]) } }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    fun submitList(list: List<Contact>) {
        differ.submitList(list)
    }
}
