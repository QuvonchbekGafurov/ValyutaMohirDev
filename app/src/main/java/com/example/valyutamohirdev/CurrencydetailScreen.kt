package com.example.valyutamohirdev

import android.os.Build
import android.provider.Telephony.Mms.Rate
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrencyDetailScreen(rateJson: String? = "", navController: NavController) {
    val rate = rateJson?.let { Gson().fromJson(it, CurrencyRate::class.java) }
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    val countryCode = rate?.Ccy?.take(2)?.lowercase()
    val Ccy = rate?.Ccy
    val Rate = rate?.Rate?.toDouble()  // Valyuta kursi (agar mavjud bo'lsa)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "",
                modifier = Modifier
                    .padding(5.dp)
                    .size(30.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Spacer(modifier = Modifier.weight(1f)) // Image va Text orasiga bo'sh joy qo'shish
            Text(
                text = "Konvertatsiya",
                modifier = Modifier,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f)) // Image va Text orasiga bo'sh joy qo'shish
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Jonli tariflarni tekshiring, miqdorni kiriting va konvertatsiya natijasiga ega bo`ling.",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center, // Matnni gorizontal markazlash uchun
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .shadow(1.dp)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Miqdor",
                modifier = Modifier,
                color = Color(0xFF808080),
                fontSize = 15.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FlagImage(countryCode = "uz")
                    Spacer(modifier = Modifier.padding(start = 10.dp))
                    Text(
                        text = "UZS",
                        modifier = Modifier,
                        fontSize = 25.sp,
                    )
                }

                OutlinedTextField(
                    value = text, // Matnning hozirgi qiymati
                    onValueChange = { newText ->
                        // Faqat raqamlar va nuqtalarni kirishiga ruxsat berish (Double uchun)
                        if (newText.all { it.isDigit() || it == '.' } &&
                            newText.count { it == '.' } <= 1) { // Faqat bir marta nuqta kirishiga ruxsat beriladi
                            text = newText
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // TextFieldni kengaytirish
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEFEFEF)), // Foni
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number // Raqamlar va nuqta (decimal) kirishi uchun
                    ),
                    maxLines = 2, // Maksimal 2 qator
                    singleLine = false, // Bir qator bilan cheklamaslik
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp), // Matn o'lchamini 20sp qilib belgilash
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent, // Qirra rangini yo'q qilish
                        unfocusedBorderColor = Color.Transparent // Unfocussed holatdagi qirra rangini yo'q qilish
                    )
                )

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp), // Ekran kengligiga mos qilib, balandlik ham berildi
                contentAlignment = Alignment.Center // Elementlarni markazda joylashtirish uchun
            ) {
                Divider(
                    color = Color(0xFFEFEFEF), // Chiziqning rangi
                    thickness = 1.dp,   // Chiziqning qalinligi
                    modifier = Modifier.fillMaxWidth() // Chiziqni butun kenglikka uzaytirish
                )

                Box(
                    modifier = Modifier
                        .size(50.dp) // Aylanani o'lchami 30 dp bo'lishi uchun
                        .clip(CircleShape) // Aylana shaklida qilish
                        .background(Color.Blue) // Foni uchun rang
                )
            }

            Text(
                text = "Konvertatsiya qilingan Miqdor",
                modifier = Modifier,
                color = Color.LightGray,
                fontSize = 15.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (countryCode != null) {
                        FlagImage(countryCode = countryCode)
                    } else {
                        FlagImage(countryCode = "uz")
                    }
                    Spacer(modifier = Modifier.padding(start = 10.dp))
                    Text(
                        text = if (Ccy != null) "$Ccy" else "UZS",
                        modifier = Modifier,
                        fontSize = 25.sp,
                    )
                }
                var result = if (text.isNotEmpty() && rate != null) {
                    // Formatlangan natija (masalan, 2 o'nlik raqam bilan)
                    "%.4f".format(text.toDouble() / Rate!!)
                } else {
                    "" // Agar malumotlar mavjud bo'lmasa
                }

                Text(
                    text = result,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // TextFieldni kengaytirish
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEFEFEF)) // Foni
                        .padding(horizontal = 15.dp, vertical = 20.dp), // Padding qo'shish
                    maxLines = 2, // Maksimal 2 qator
                    fontSize = 20.sp
                )

            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Indikativ valyuta kursi",
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF808080)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "1 $Ccy = ${Rate} UZS",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}