package com.example.valyutamohirdev

import android.util.Log
import androidx.annotation.ColorInt
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.gson.Gson

@Composable
fun painterfun(countryCode: String) = rememberImagePainter(
    "https://flagcdn.com/w320/$countryCode.png"
)
@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel = viewModel(),navController: NavController) {
    val currencyRates by viewModel.currencyRates.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCurrencyRates()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Text(text = "Valyuta kurslari", fontWeight = FontWeight.Bold, fontSize = 25.sp)
            Spacer(modifier = Modifier.weight(1f)) // Bo'sh joy beradi
            if (currencyRates.isNotEmpty()) {
                Text(text = currencyRates[0].Date, fontWeight = FontWeight.Bold)
            } else {
                Text(text = "", fontWeight = FontWeight.Bold, color = Color.Red)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        when {
            isLoading -> {
                // Yuklanayotgan holat
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center) // Markazda joylashishi uchun
                ) {
                    CircularProgressIndicator()
                }            }
            error != null -> {
                // Xatolik mavjud bo'lsa
                Text(text = error!!, color = Color.Red)
            }
            else -> {
                // Muvaffaqiyatli yuklangan holat
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(currencyRates) { rate ->
                        CurrencyRateItem(rate = rate,navController=navController)
                    }
                }
            }
        }
    }
}
@Composable
fun CurrencyRateItem(rate: CurrencyRate,navController: NavController) {
    val imageUrl = "https://countryflags.io/us/flat/64.png"  // Manzilni to'g'ri tekshirib chiqing
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            error(R.drawable.sell)  // Rasm yuklanmasa default rasmni ko'rsatish
            placeholder(R.drawable.ic_launcher_background)  // Yuklanayotgan rasm uchun placeholder
        }
    )
    Column(modifier = Modifier.clickable {
        val rateJson = Gson().toJson(rate)
        // JSON String ko'rinishida yuborish
        navController.navigate("currencyDetail/$rateJson")
    }) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val countryCode=rate.Ccy.take(2).lowercase()
            Log.e("TAG", "CurrencyRateItem: $countryCode", )
            FlagImage(countryCode = countryCode)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = rate.Ccy, fontWeight = FontWeight.Bold)
                Text(
                    text = rate.CcyNm_UZ,
                    modifier = Modifier,
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            }
            Spacer(modifier = Modifier.weight(1f)) // Bo'sh joy beradi
            Column (modifier = Modifier, horizontalAlignment = Alignment.End){
                Text(text = rate.Rate+" UZS", fontWeight = FontWeight.Bold)
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = rate.Diff+"%",
                        modifier = Modifier,
                        fontSize = 15.sp,
                        color = Color.LightGray
                    )
                    Image(
                        painter = painterResource(
                            id = if (rate.Diff?.toDoubleOrNull() ?: 0.0 > 0) R.drawable.top else R.drawable.sell
                        ),
                        contentDescription = "",
                        modifier = Modifier.size(
                            if (rate.Diff?.toDoubleOrNull() ?: 0.0 > 0) 20.dp else 16.dp
                        )
                    )
                }
            }
        }
        Divider(
            color = Color.LightGray,       // Chiziqning rangi
            thickness = 1.dp,         // Chiziqning qalinligi
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)  // Chiziqni ekran bo'ylab kengaytirish
        )
        Spacer(modifier = Modifier.height(10.dp))

    }

}


// Composable funksiya
@Composable
fun FlagImage(countryCode: String) {

    Image(
        painter = painterfun(countryCode),
        contentDescription = "Flag of $countryCode",
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

