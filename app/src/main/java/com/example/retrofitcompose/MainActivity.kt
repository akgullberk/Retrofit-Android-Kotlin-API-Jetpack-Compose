package com.example.retrofitcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrofitcompose.model.CryptoModel
import com.example.retrofitcompose.service.CryptoAPI
import com.example.retrofitcompose.ui.theme.RetrofitComposeTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitComposeTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val BASE_URL = "https://raw.githubusercontent.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)

    val call = retrofit.getData()

    call.enqueue(object : Callback<List<CryptoModel>>{
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if(response.isSuccessful){
                response.body()?.let {
                    it.forEach {
                        println(it.currency)
                    }
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Retrofit Compose",Modifier.padding(10.dp), fontSize = 26.sp) },



                )

        },
        content = { paddingValues ->
            // Ekrandaki diğer içerik burada olacak
            // Padding değerleri kullanılarak içerik üst çubuk altında başlatılır.
            Text(
                text = "Ana İçerik",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    )
}

@Composable
fun CryptoList(cryptos : List<CryptoModel>){
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(cryptos){ crypto ->

            CryptoRow(crypto = crypto)
        }

    }
}

@Composable
fun CryptoRow(crypto : CryptoModel){
    Column(modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.surface)) {
        Text(text = crypto.currency,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold

            )
        Text(text = crypto.price,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(2.dp),

            )
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RetrofitComposeTheme {
        CryptoRow(crypto = CryptoModel("BTC","12311"))
    }
}