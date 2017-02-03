package com.hannesdorfmann.mosby3.conductor.sample.model.contacts

import rx.Observable

/**
 * Responsible to load a list of all contacts from users phone
 *
 * @author Hannes Dorfmann
 */
interface ContactsLoader {

  /**
   * Load a list of all contacts
   */
  fun loadContacts(): Observable<List<Contact>>
}