package com.huzzoid.konturexercise.ui.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.huzzoid.konturexercise.ui.details.DetailsFragment
import com.huzzoid.konturexercise.ui.contacts.ContactsFragment

object Screen {
    fun contactsScreen() = FragmentScreen { ContactsFragment.newInstance() }
    fun detailsScreen() = FragmentScreen { DetailsFragment.newInstance() }
}