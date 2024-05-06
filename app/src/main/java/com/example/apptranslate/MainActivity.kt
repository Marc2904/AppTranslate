package com.example.apptranslate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.apptranslate.API.retrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var btnDetecte:Button
    private lateinit var etDesc:EditText

    var allLanguages = emptyList<Language>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
        getLanguages()
    }

    private fun initListener() {
        btnDetecte.setOnClickListener{
            val text : String = etDesc.text.toString()
            if(text.isNotEmpty()){
                getTextLanguage(text)
            }
        }
    }

    private fun getTextLanguage(text: String) {
        CoroutineScope(Dispatchers.IO).launch{
            val result : Response<DetectionResponse> = retrofitService.getTextLanguage(text)
            if(result.isSuccessful){
                checkResult(result.body())
            }else{
                showError()
            }
        }
    }

    private fun checkResult(detectionResponse: DetectionResponse?) {
        if(detectionResponse != null && !detectionResponse.data.detections.isNullOrEmpty()){
            val correctLanguages :List<Detection> = detectionResponse.data.detections.filter { it.isReliable }
            if (correctLanguages.isNotEmpty()){

                val languageName = allLanguages.find { it.code == correctLanguages.first().language }

                if(languageName != null){
                    runOnUiThread{
                        Toast.makeText(this,"El idioma es ${languageName.name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getLanguages() {
        CoroutineScope(Dispatchers.IO).launch {
            val language : Response<List<Language>> = retrofitService.getLanguage()
            if(language.isSuccessful){
                allLanguages = language.body() ?: emptyList()
                showSuccess()
            }else{
                showError()
            }
        }
    }

    private fun showSuccess() {
        runOnUiThread{
            Toast.makeText(this,"Peticion correcta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError() {
        runOnUiThread{
            Toast.makeText(this,"Error al hacer la llamada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView(){
        btnDetecte = findViewById(R.id.btnDetecte)
        etDesc = findViewById(R.id.etDesc)
    }
}