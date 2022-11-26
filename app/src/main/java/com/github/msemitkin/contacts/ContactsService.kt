package com.github.msemitkin.contacts

import android.content.ContentResolver
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone

interface GetContactsByQuery {
    fun getContactsByQuery(query: String): List<Contact>
}

class ContactsService(private val contentResolver: ContentResolver) : GetContactsByQuery {

    override fun getContactsByQuery(query: String): List<Contact> {
        val contacts = mutableListOf<Contact>()
        contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )!!
            .use { cursor ->
                while (cursor.moveToNext()) {
                    val idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    val displayNameColumnIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val contactId = cursor.getString(idColumnIndex)
                    val contactDisplayName = cursor.getString(displayNameColumnIndex)
                    contacts.add(
                        Contact(
                            contactDisplayName,
                            getPhoneNumbers(contactId)
                        )
                    )
                }
            }
        return contacts.filter {
            it.fullName.contains(query, true)
                    || it.phoneNumbers.any { number -> number.contains(query, true) }
        }
    }

    private fun getPhoneNumbers(contactId: String): List<String> {
        val numbers = mutableListOf<String>()
        contentResolver.query(
            Phone.CONTENT_URI,
            null,
            Phone.CONTACT_ID + " = ?",
            arrayOf(contactId),
            null
        )!!.use { cursor ->
            while (cursor.moveToNext()) {
                val phoneNumberColumnIndex = cursor.getColumnIndex(Phone.NUMBER)
                numbers.add(cursor.getString(phoneNumberColumnIndex))
            }
        }
        return numbers.map { it.replace(" ", "") }
    }
}