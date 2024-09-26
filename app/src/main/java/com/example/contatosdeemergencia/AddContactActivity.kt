package com.example.contatosdeemergencia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddContactActivity : AppCompatActivity() {
    private lateinit var contactNameInput: EditText
    private lateinit var contactPhoneInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        contactNameInput = findViewById(R.id.contactNameInput)
        contactPhoneInput = findViewById(R.id.contactPhoneInput)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val name = contactNameInput.text.toString()
            val phone = contactPhoneInput.text.toString()

            // Verificar se os campos não estão vazios
            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("name", name)
                intent.putExtra("phone", phone)
                setResult(RESULT_OK, intent)  // Retorna resultado positivo
                finish()  // Finaliza a activity e retorna os dados
            } else {
                // Pode adicionar uma mensagem de erro se os campos estiverem vazios
                // Exemplo: Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
