package com.hannesdorfmann.mosby3.conductor.sample.create.contactspicker

import com.hannesdorfmann.mosby3.conductor.sample.model.contacts.Contact
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView

/**
 *
 * A [MvpLceView] that displays a list of contacts
 *
 * @author Hannes Dorfmann
 */
interface ContactsPickerView : MvpLceView<List<Contact>> {
  fun finish()
}