package com.example.contatosdeemergencia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter
    private var contactList: MutableList<Contact> = ArrayList()
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var addContactButton: Button
    private lateinit var noContactsTextView: TextView
    private lateinit var deleteAllButton: Button  // Botão para deletar todos os contatos

    // Registrar o ActivityResultLauncher para adicionar contato
    private val addContactLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    val name = it.getStringExtra("name") ?: ""
                    val phone = it.getStringExtra("phone") ?: ""
                    if (name.isNotEmpty() && phone.isNotEmpty()) {
                        addContact(name, phone)  // Adiciona o novo contato
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        addContactButton = findViewById(R.id.addContactButton)
        deleteAllButton = findViewById(R.id.deleteAllButton)  // Encontrar o botão de deletar
        noContactsTextView = findViewById(R.id.noContactsTextView)

        // Configurar o RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        contactsAdapter = ContactsAdapter(contactList, this)
        recyclerView.adapter = contactsAdapter

        // Carregar os contatos do banco de dados
        loadContacts()

        // Configurar o botão para adicionar contato
        addContactButton.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            addContactLauncher.launch(intent)  // Usar o ActivityResultLauncher
        }

        // Configurar o botão para deletar todos os contatos
        deleteAllButton.setOnClickListener {
            deleteAllContacts()  // Chamar a função de deletar todos os contatos
        }
    }

    // Método para carregar contatos do banco de dados
    private fun loadContacts() {
        contactList.clear()
        contactList.addAll(dbHelper.getAllContacts())
        contactsAdapter.notifyDataSetChanged()

        // Verifica se a lista de contatos está vazia
        if (contactList.isEmpty()) {
            noContactsTextView.visibility = View.VISIBLE  // Exibe a mensagem
            recyclerView.visibility = View.GONE  // Oculta o RecyclerView
        } else {
            noContactsTextView.visibility = View.GONE  // Oculta a mensagem
            recyclerView.visibility = View.VISIBLE  // Exibe o RecyclerView
        }
    }

    // Método para adicionar contatos
    private fun addContact(name: String, phone: String) {
        val newContact = Contact(name, phone)
        dbHelper.addContact(newContact)
        loadContacts()  // Recarrega os contatos
    }

    // Método para deletar todos os contatos
    private fun deleteAllContacts() {
        dbHelper.deleteAllContacts()  // Deleta todos os contatos no banco de dados
        loadContacts()  // Recarrega os contatos (irá mostrar a mensagem se estiver vazio)
    }
}
