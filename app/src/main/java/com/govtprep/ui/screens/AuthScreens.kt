package com.govtprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.govtprep.R

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit, onSignup: () -> Unit, error: String?) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(R.string.login_title))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text(stringResource(R.string.email)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text(stringResource(R.string.password)) }, modifier = Modifier.fillMaxWidth())
        error?.let { Text(it) }
        Button(onClick = { onLogin(email, password) }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.login)) }
        Button(onClick = onSignup, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.go_signup)) }
    }
}

@Composable
fun SignupScreen(onSignup: (String, String, String) -> Unit, onBack: () -> Unit, error: String?) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(R.string.signup_title))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.full_name)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text(stringResource(R.string.email)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text(stringResource(R.string.password)) }, modifier = Modifier.fillMaxWidth())
        error?.let { Text(it) }
        Button(onClick = { onSignup(name, email, password) }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.signup)) }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.back_login)) }
    }
}
