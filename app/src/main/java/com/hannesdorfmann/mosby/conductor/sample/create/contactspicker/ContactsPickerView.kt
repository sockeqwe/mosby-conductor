package com.hannesdorfmann.mosby.conductor.sample.create.contactspicker

import com.hannesdorfmann.mosby.conductor.sample.model.contacts.Contact
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView

/**
 *
 * A [MvpLceView] that displays a list of contacts
 *
 * @author Hannes Dorfmann
 */
interface ContactsPickerView : MvpLceView<List<Contact>> {
  fun finish()
}