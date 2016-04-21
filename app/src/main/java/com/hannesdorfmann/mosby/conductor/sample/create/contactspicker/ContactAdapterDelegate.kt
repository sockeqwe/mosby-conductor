package com.hannesdorfmann.mosby.conductor.sample.create.contactspicker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates2.AbsListItemAdapterDelegate
import com.hannesdorfmann.mosby.conductor.sample.R
import com.hannesdorfmann.mosby.conductor.sample.model.contacts.Contact

/**
 * Displays an item for [Contact]
 *
 * @author Hannes Dorfmann
 */
class ContactAdapterDelegate(private val inflater: LayoutInflater, private val selectionListener: (Contact) -> Unit) : AbsListItemAdapterDelegate<Contact, Contact, ContactAdapterDelegate.ContactViewHolder>() {

  override fun isForViewType(item: Contact, items: MutableList<Contact>?, position: Int) = true

  override fun onCreateViewHolder(parent: ViewGroup) = ContactViewHolder (
      inflater.inflate(R.layout.item_contact, parent, false))

  override fun onBindViewHolder(item: Contact, viewHolder: ContactViewHolder) {
    viewHolder.contact = item
    viewHolder.name.text = item.name
  }

  inner class ContactViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    lateinit var contact: Contact
    val name: TextView

    init {
      v.setOnClickListener {
        selectionListener(contact)
      }

      name = v.findViewById(R.id.name) as TextView
    }

  }
}