package com.example.food_app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_app.R
import com.example.food_app.data.Role

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: (Role) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var identifier by remember { mutableStateOf(AuthViewModel.lastRegisteredIdentifier ?: "admin") }
    var password by remember { mutableStateOf(AuthViewModel.lastRegisteredPassword ?: "admin") }
    val authResult by viewModel.authResult.collectAsState()
    val savedCredentials by viewModel.savedCredentials.collectAsState(initial = null)
    var hasPreFilled by remember { mutableStateOf(false) }

    // Logic tự động điền thông tin
    LaunchedEffect(savedCredentials) {
        if (!hasPreFilled && savedCredentials != null) {
            identifier = savedCredentials!!.identifier
            password = savedCredentials!!.password
            hasPreFilled = true
        }
    }

    LaunchedEffect(authResult) {
        val result = authResult
        if (result is AuthResult.Success) {
            onLoginSuccess(result.role)
            viewModel.resetResult()
        }
    }

    val orangeColor = Color(0xFFFFA726)
    val lightTextColor = Color.White.copy(alpha = 0.8f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(orangeColor)
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.my_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "FoodGo",
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = Color.White
            )
            Text(
                text = "Booking Fast Food App",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it; viewModel.resetResult() },
            label = { Text("Tên người dùng hoặc SĐT") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.White,
                unfocusedLabelColor = lightTextColor,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = lightTextColor,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; viewModel.resetResult() },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.White,
                unfocusedLabelColor = lightTextColor,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = lightTextColor,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        val result = authResult
        when (result) {
            is AuthResult.Error -> {
                Text(
                    text = result.message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            is AuthResult.PasswordReset -> {
                Text(
                    text = result.message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.login(identifier, password) },
            enabled = authResult !is AuthResult.Loading,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            if (authResult is AuthResult.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = orangeColor
                )
            } else {
                Text("Đăng nhập", color = orangeColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
             TextButton(onClick = onNavigateToForgotPassword) {
                Text("Quên mật khẩu?", color = Color.White.copy(alpha = 0.9f))
            }
            TextButton(onClick = onNavigateToRegister) {
                Text("Đăng ký ngay", color = Color.White)
            }
        }
    }
}
