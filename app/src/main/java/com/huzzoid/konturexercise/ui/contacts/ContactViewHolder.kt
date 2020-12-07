package com.huzzoid.konturexercise.ui.contacts

import androidx.recyclerview.widget.RecyclerView
import com.huzzoid.konturexercise.databinding.ItemContactBinding
import com.huzzoid.konturexercise.domain.pojo.Contact

class ContactViewHolder(
    private val binding: ItemContactBinding,
    private val onClickListener: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            onClickListener.invoke(layoutPosition)
        }
    }

    fun bind(contact: Contact) {
        binding.name.text = contact.name
        binding.height.text = contact.height
        binding.phone.text = contact.phone
    }
}