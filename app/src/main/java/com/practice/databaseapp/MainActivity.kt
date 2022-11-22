package com.practice.databaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.practice.databaseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitID.setOnClickListener {
            val name = binding.nameInputID.text.toString()
            val password = binding.passwordInputID.text.toString()

            database = FirebaseDatabase.getInstance("https://database-app-dec41-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
            val user = User(name, password)
            database.child(name).setValue(user).addOnSuccessListener {
                binding.nameInputID.text.clear()
                binding.passwordInputID.text.clear()

                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
            }
        }

        binding.readBtnId.setOnClickListener {

            val name: String = binding.nameInputID.text.toString()
            if (name.isNotEmpty()) {

                readData(name)

            } else {

                Toast.makeText(this, "PLease enter the Username", Toast.LENGTH_SHORT).show()

            }

        }
    }

    private fun readData(userName: String) {

        database = FirebaseDatabase.getInstance("https://database-app-dec41-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
        database.child(userName).get().addOnSuccessListener {

            if (it.exists()){

//                val name = it.child("name").value
                val password = it.child("password").value
                Toast.makeText(this,"Successfully Read",Toast.LENGTH_SHORT).show()
//                binding.nameInputID.text.clear()
                binding.passwordInputID.setText(password.toString())
//                binding.tvLastName.text = lastName.toString()
//                binding.tvAge.text = age.toString()

            }else{

                Toast.makeText(this,"User Doesn't Exist",Toast.LENGTH_SHORT).show()


            }

        }.addOnFailureListener{

            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


        }



    }
}