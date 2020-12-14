package com.huzzoid.konturexercise.ui.screens

import android.content.Intent
import android.net.Uri
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.huzzoid.konturexercise.ui.contacts.ContactsFragment
import com.huzzoid.konturexercise.ui.details.DetailsFragment

object Screen {
    fun contactsScreen() = FragmentScreen { ContactsFragment.newInstance() }
    fun detailsScreen(id: String) = FragmentScreen { DetailsFragment.newInstance(id) }
    fun callToPhone(phone: String) = ActivityScreen {
        Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    }
}