package com.example.myfilm.screen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myfilm.R
import com.example.myfilm.appcomponent.HeadingTextComponent
import com.example.myfilm.database.MyApplication
import com.example.myfilm.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class RegistrationInput(
    val name: String,
    val email: String,
    val password: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val registrationInput = remember { mutableStateOf(RegistrationInput("", "", "")) }

    Surface(
        color = Color(0xFFE9F1EF),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.heightIn(min = 60.dp))
            Spacer(modifier = Modifier.heightIn(min = 50.dp))
            Image(
                modifier = Modifier
                    .height(137.dp)
                    .width(359.dp),
                painter = painterResource(id = R.drawable.logocineplay),
                contentDescription = ""
            )
            HeadingTextComponent(value = "Register Here")
            Spacer(modifier = Modifier.heightIn(min = 50.dp))

            // Name Field
            OutlinedTextField(
                value = registrationInput.value.name,
                onValueChange = { newValue ->
                    registrationInput.value = registrationInput.value.copy(name = newValue)
                },
                modifier = Modifier
                    .width(350.dp),
                label = { Text("Name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF59CCBC),
                    focusedLabelColor = Color(0xFF59CCBC),
                    cursorColor = Color(0xFF59CCBC),
                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                maxLines = 1,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.heightIn(min = 25.dp))

            // Email Field
            OutlinedTextField(
                value = registrationInput.value.email,
                onValueChange = { newValue ->
                    registrationInput.value = registrationInput.value.copy(email = newValue)
                },
                modifier = Modifier
                    .width(350.dp),
                label = { Text("Email") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF59CCBC),
                    focusedLabelColor = Color(0xFF59CCBC),
                    cursorColor = Color(0xFF59CCBC),
                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                maxLines = 1,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.heightIn(min = 25.dp))

            // Password Field
            val passwordVisible = remember {
                mutableStateOf(false)
            }
            OutlinedTextField(
                value = registrationInput.value.password,
                onValueChange = { newValue ->
                    registrationInput.value = registrationInput.value.copy(password = newValue)
                },
                modifier = Modifier
                    .width(350.dp),
                label = { Text("Password") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF59ccbc),
                    focusedLabelColor = Color(0xFF59ccbc),
                    cursorColor = Color(0xFF59ccbc),
                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                maxLines = 1,
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.lock), contentDescription = "")
                },
                trailingIcon = {
                    val iconImage = if (passwordVisible.value) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    val description = if (passwordVisible.value) {
                        "Hide Password"
                    } else {
                        "Show Password"
                    }
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(imageVector = iconImage, contentDescription = description)
                    }
                },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else
                    PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.heightIn(min = 50.dp))
            Button(
                modifier = Modifier
                    .width(135.dp)
                    .heightIn(48.dp),
                onClick = {
                    val application = context.applicationContext as Application
                    val database = (application as MyApplication).database
                    val userDao = database.userDao()

                    val input = registrationInput.value

                    if (input.name.isNotEmpty() &&
                        input.email.isNotEmpty() &&
                        input.password.isNotEmpty()) {

                        val entity = User(name = input.name,email = input.email, password = input.password)
                        CoroutineScope(Dispatchers.IO).launch {
                            userDao.insert(entity)
                            Log.d("DatabaseLogging", "Data inserted: $entity")
                        }
                        navController.navigate("login")
                    } else {
                        Toast.makeText(context, "Fields must not be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                shape = RoundedCornerShape(50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(135.dp)
                        .heightIn(48.dp)
                        .background(
                            color = Color(0xFF34806e),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Register",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/*@Preview
@Composable
fun DefaultPreviewRegisterScreen() {
    MyFIlmTheme {
        val dummyNavController = rememberNavController()
        RegisterScreen(navController = dummyNavController)
    }
}*/
