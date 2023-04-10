package com.practice.attendanceusingfacerecognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ViewAttendanceActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    var count: Any? = "as"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance)
        var names: ArrayList<String> = ArrayList()
        names.add("Preetham")
        names.add("Sanjith")
        names.add("Rahul")
        names.add("Shubham")
        names.add("Shashanka")
        names.add("Sunil")



        readCount(names)

//        writeCount("Preetham")
//        writeCount("Sanjith")
//        writeCount("Rahul")
//        writeCount("Shashanka")
//        writeCount("Sunil")
//        writeCount("Shubham")



    }

    private fun readCount(names: ArrayList<String>) {

        for (name in names){
            val stat: TextView
            if(name=="Preetham") {
                stat = findViewById<TextView>(R.id.Preetham)
            }
            else if(name=="Sanjith"){
                stat = findViewById<TextView>(R.id.Sanjith)
            }
            else if(name=="Rahul"){
                stat = findViewById<TextView>(R.id.Rahul)
            }
            else if(name=="Shubham"){
                stat = findViewById<TextView>(R.id.Shubham)
            }
            else if(name=="Shashanka"){
                stat = findViewById<TextView>(R.id.Shashanka)
            }
            else{
                stat = findViewById<TextView>(R.id.Sunil)
            }

            database =
                FirebaseDatabase.getInstance("https://attendanceusingfacerecog-52ca1-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Status")
            database.child(name).get().addOnSuccessListener {
                if (it.exists()) {
//                Toast.makeText(this,"${it.child("daysCount").value}", Toast.LENGTH_SHORT).show()

                    count = it.child("daysCount").value
//                    Toast.makeText(this,"${count.toString()}", Toast.LENGTH_SHORT).show()
                    stat.text = count as CharSequence?

//                val name = it.child("name").value
//                val fetchedpassword = it.child("password").value
//                val mail = it.child("mail").value
//                val dept = it.child("dept").value

                }
            }.addOnFailureListener{
                Toast.makeText(this,"Failed to validate the user.", Toast.LENGTH_SHORT).show()
            }
//            Toast.makeText(this,"${count.toString()}", Toast.LENGTH_SHORT).show()

        }
    }

    private fun writeCount(Username: String) {
        database = FirebaseDatabase.getInstance("https://attendanceusingfacerecog-52ca1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Status")
        val s: String = "20"
        val status = Status(s)

//        if (student != null) {
//            database.child(date).child(name).setValue(student).addOnSuccessListener {
////                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
//            }
//        }

        database.child(Username).setValue(status).addOnSuccessListener {
                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
        }
    }

}