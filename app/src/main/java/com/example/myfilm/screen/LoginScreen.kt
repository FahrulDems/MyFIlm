package com.example.myfilm.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.myfilm.appcomponent.ButtonComponent
import com.example.myfilm.appcomponent.HeadingTextComponent
import com.example.myfilm.database.MyApplication
import com.example.myfilm.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class LoginInput(
    val email: String,
    val password: String
)

var userLogin: User? = null

@Composable
fun PreLoginScreen(navController: NavController){
    val context = LocalContext.current
    val userDao = (context.applicationContext as MyApplication).database.userDao()

    LaunchedEffect(key1 = true ){
        val user = withContext(Dispatchers.IO){
            userDao.getUserByLogin(true)
        }
        if (user != null){
            userLogin = user
            navController.navigate("home"){
                popUpTo("home"){inclusive = true}
            }
        } else{
            navController.navigate("login"){
                popUpTo("login"){inclusive = true}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onRegisterButtonClicked: () -> Unit, onLoginButtonClicked: () -> Unit) {
    val context = LocalContext.current
    val loginInput = remember { mutableStateOf(LoginInput("", "")) }
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
            HeadingTextComponent(value = "Welcome Back")
            Spacer(modifier = Modifier.heightIn(min = 50.dp))
            // Email Field
            OutlinedTextField(
                value = loginInput.value.email,
                onValueChange = { newValue ->
                    loginInput.value = loginInput.value.copy(email = newValue)
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
                value = loginInput.value.password,
                onValueChange = { newValue ->
                    loginInput.value = loginInput.value.copy(password = newValue)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonComponent(
                    value = "Register Here",
                    onButtonClicked = {
                        onRegisterButtonClicked()
                    },
                )
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    modifier = Modifier
                        .width(135.dp)
                        .heightIn(48.dp),
                    onClick = {
                        val email = loginInput.value.email
                        val password = loginInput.value.password

                        val userDao = (context.applicationContext as MyApplication).database.userDao()

                        CoroutineScope(Dispatchers.IO).launch{
                            val user = userDao.getUserByEmail(email)
                            withContext(Dispatchers.Main) {
                                if (user != null && user.password == password) {
                                    userDao.updateUser(user.id, true)
                                    onLoginButtonClicked()
                                    Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Email or password Is not valid", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    shape = RoundedCornerShape(50.dp),
                    enabled = true
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
                            text = "Login",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
