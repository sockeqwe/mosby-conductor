package com.hannesdorfmann.mosby.conductor.sample.model.contacts

import android.os.Parcel
import android.os.Parcelable

/**
 * Represents a Contact from phones contact book
 *
 * @author Hannes Dorfmann
 */
data class Contact(val name: String) : Parcelable {
  constructor(source: Parcel): this(source.readString())

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeString(name)
  }

  companion object {
    @JvmField final val CREATOR: Parcelable.Creator<Contact> = object : Parcelable.Creator<Contact> {
      override fun createFromParcel(source: Parcel): Contact {
        return Contact(source)
      }

      override fun newArray(size: Int): Array<Contact?> {
        return arrayOfNulls(size)
      }
    }
  }
}