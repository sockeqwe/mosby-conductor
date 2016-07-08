package com.hannesdorfmann.mosby.conductor.sample.model.contacts

import android.content.Context
import android.provider.ContactsContract
import rx.Observable
import java.util.*

/**
 * Concrete implementation of [ContactsLoader].
 *
 * Assumes that permission has already been granted (Runtime permissions)
 *
 * @author Hannes Dorfmann
 */
class ContactsLoaderImpl(private val context: Context) : ContactsLoader {

  override fun loadContacts(): Observable<List<Contact>> = Observable.fromCallable {

    // TODO signal cancelation
    val cursor = context.contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
        arrayOf(ContactsContract.Data.DISPLAY_NAME_PRIMARY),
        null,
        null,
        null)

    val contacts = ArrayList<Contact>()
    try {
      if (cursor.count > 0) {
        val colIndex = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY)
        while (cursor.moveToNext()) {
          if (!cursor.isNull(colIndex)) {
            contacts.add(Contact(cursor.getString(colIndex)))
          }
        }
      }
    } finally {
      cursor.close()
    }

    // Return contacts
    contacts
  }
}