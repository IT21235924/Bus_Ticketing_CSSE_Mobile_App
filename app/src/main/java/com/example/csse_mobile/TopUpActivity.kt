package com.example.cssenew

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.csse_mobile.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TopUpActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextTopUpAmount: EditText
    private lateinit var spinnerPaymentMethod: Spinner
    private lateinit var buttonTopUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val app = FirebaseApp.getInstance()
        if (app != null) {
            Log.d("Firebase", "Firebase App is initialized.")
        } else {
            Log.e("Firebase", "Firebase App is not initialized.")
        }

// Check Firebase Firestore instance
        val firestore = FirebaseFirestore.getInstance()
        if (firestore != null) {
            Log.d("Firebase", "Firebase Firestore is accessible.")
        } else {
            Log.e("Firebase", "Firebase Firestore is not accessible.")
        }

        editTextTopUpAmount = findViewById(R.id.editTextTopUpAmount)
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod)
        buttonTopUp = findViewById(R.id.buttonTopUp)

        // Set up payment method spinner with dummy data
        val paymentMethods = arrayOf("Credit Card", "Debit Card", "Prepaid Card")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paymentMethods)
        spinnerPaymentMethod.adapter = adapter

        // Handle top-up button click
        buttonTopUp.setOnClickListener {
            Log.d("ButtonTest", "Button clicked") // Check Logcat for "Button clicked" message
            handleTopUp()

        }
    }

    private fun handleTopUp() {
        val topUpAmount = editTextTopUpAmount.text.toString().toDoubleOrNull() ?: 0.0
        val selectedPaymentMethod = spinnerPaymentMethod.selectedItem as String
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val topUpData = hashMapOf(
            "topUpAmount" to topUpAmount,
            "paymentMethod" to selectedPaymentMethod,
            "dateTime" to currentDateTime
        )

        val userDocRef = db.collection("topups").document()

        userDocRef.set(topUpData)
            .addOnSuccessListener {
                val successMessage = "Top-up Amount: $topUpAmount\nPayment Method: $selectedPaymentMethod\nDate and Time: $currentDateTime"
                println(successMessage)

                // Show a toast message for successful top-up
                runOnUiThread {
                    showToast("Top-up successful")
                }
            }
            .addOnFailureListener { e ->
                println("Transaction failed: $e")
                e.printStackTrace()
                // Handle the error or show an error message
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
