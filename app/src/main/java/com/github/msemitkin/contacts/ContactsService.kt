package com.github.msemitkin.contacts

import android.content.Context
import android.provider.ContactsContract

interface GetContactsByQuery {
    fun getContactsByQuery(query: String): List<Contact>
}

private const val PHONE_STUB = "+380986666666"

class ContactsService(private val appContext: Context) : GetContactsByQuery {

    override fun getContactsByQuery(query: String): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver = appContext.contentResolver
        contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)!!
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
                            getPhoneNumber(contactId)
                        )
                    )
                }
            }
        return contacts.filter { it.fullName.contains(query, true) }
    }

    private fun getPhoneNumber(contactId: String?): String {
        return PHONE_STUB
    }
}