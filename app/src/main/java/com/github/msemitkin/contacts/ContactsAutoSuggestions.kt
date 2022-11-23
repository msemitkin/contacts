package com.github.msemitkin.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

@Composable
fun ContactsAutoSuggestions(
    getContactsByQuery: GetContactsByQuery
) {
    var hideKeyboard by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var inputText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithNoIndication { hideKeyboard = true }
    ) {
        Box {
            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    expanded = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 30.dp, end = 30.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            expanded = true
                        }
                    },
                trailingIcon = {
                    IconButton(onClick = { inputText = "" }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
            DropdownMenu(
                properties = PopupProperties(focusable = false),
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(0.dp, 20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .fillMaxHeight(0.4f)
            ) {
                val contacts = getContactsByQuery.getContactsByQuery(inputText)
                contacts.forEach { contact ->
                    DropdownMenuItem(
                        onClick = { }
                    ) {
                        Column {
                            Text(text = contact.fullName)
                            contact.phoneNumbers.forEach { phoneNumber ->
                                Text(text = phoneNumber)
                            }
                        }
                    }
                }
            }
        }
        if (hideKeyboard) {
            focusManager.clearFocus()
            hideKeyboard = false
        }
    }
}

fun Modifier.clickableWithNoIndication(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier {
    return clickable(
        MutableInteractionSource(),
        null,
        enabled,
        onClickLabel,
        role,
        onClick
    )
}