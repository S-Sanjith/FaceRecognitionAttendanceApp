package com.practice.attendanceusingfacerecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.practice.attendanceusingfacerecognition.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.loginButton.setOnClickListener {

            val uname = binding.usernameInput.text.toString()
            //Toast.makeText(this,"${UName.isNotEmpty()}",Toast.LENGTH_SHORT).show()
            val pass = binding.passwordInput.text.toString()
            if (uname.isNotEmpty()) {

                readData(uname, pass)

            } else {

                Toast.makeText(this, "USERNAME OR PASSWORD CANNOT BE EMPTY!!", Toast.LENGTH_SHORT)
                    .show()

            }


//            Intent(this, ProfileActivity::class.java).also {
//                startActivity(it)
//            }
        }

    }

    private fun readData(Username: String, Password: String) {
        database =
            FirebaseDatabase.getInstance("https://attendanceusingfacerecog-52ca1-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users")
        database.child(Username).get().addOnSuccessListener {
            if (it.exists()) {

                val fetchedUsername = it.child("name").value
//                val name = it.child("name").value
                val fetchedpassword = it.child("password").value
                val mail = it.child("mail").value
                val dept = it.child("dept").value
                if (fetchedUsername == Username) {
                    if (fetchedpassword == Password) {
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.putExtra("username", Username)
                        intent.putExtra("mail", mail.toString())
                        intent.putExtra("dept", dept.toString())
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(
                            this,
                            "The password is invalid",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else{
                    Toast.makeText(this,"This account does not exist. Please create a new account.",Toast.LENGTH_SHORT).show()
                }
//                    Toast.makeText(this, "Successfully Read", Toast.LENGTH_SHORT).show()
//                binding.nameInputID.text.clear()
                //               binding.passwordInputID.setText(password.toString())
//                binding.tvLastName.text = lastName.toString()
//                binding.tvAge.text = age.toString()

            }
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to validate the user.",Toast.LENGTH_SHORT).show()
        }
    }


}