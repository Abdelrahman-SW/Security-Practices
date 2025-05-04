package com.example.securitypractices.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.securitypractices.encrypted_db.PersonDbSqlCipher
import com.example.securitypractices.encrypted_db.PersonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun DbWithSqlCipher(modifier: Modifier = Modifier, db: PersonDbSqlCipher) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var enteredName by remember {
        mutableStateOf("")
    }

    var persons by remember { mutableStateOf(emptyList<PersonEntity>()) }

    LaunchedEffect(true) {
        // get all persons for encrypted db
        db.personDao().getAllPersons().onEach {
            persons = it
        }.launchIn(scope)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 32.dp),
            text = "DB With SqlCipher",
            fontSize = 30.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            modifier.weight(1f)
        ) {
            items(persons) {
                Text(text = "Person With Id = ${it.id} And Name =  ${it.name}" , fontSize = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        TextField(
            value = enteredName,
            onValueChange = { enteredName = it },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                if (enteredName.isNotBlank()) {
                    db.personDao().insertPerson(PersonEntity(name = enteredName))
                    enteredName = ""
                }
                else {
                    Toast.makeText(context, "Name can't be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text(text = "Insert New Person")
        }
    }
}