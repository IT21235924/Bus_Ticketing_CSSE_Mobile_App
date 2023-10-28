package com.example.csse_mobile

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.csse_mobile.databinding.ActivityQrscanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.util.UUID


class QRScanActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    private val requestPermissionlauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showCamera()
            } else {

            }
        }


    private val scanLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    setResult(result.contents)
                }
            }

        }

    private lateinit var binding: ActivityQrscanBinding

    private fun setResult(place: String){
        binding.textResult.text = place

        firebaseAuth = FirebaseAuth.getInstance()

        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        val ref = db.collection("Trip")

        ref.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                val tripID = UUID.randomUUID().toString()
                val userID = FirebaseAuth.getInstance().currentUser!!.uid
                val tripMap = hashMapOf(
                    "User_ID" to userID,
                    "Trip_ID" to tripID,
                    "startDestination" to place,
                    "startTime" to FieldValue.serverTimestamp(),
                    "endDestination" to null,
                    "endTime" to null,
                )

                db.collection("Trip").document(userID).set(tripMap).addOnSuccessListener {
                    Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                }
            }
            else{
                db.collection("Trip").orderBy("startTime", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener { querySnapshot ->
                    val tripDoc = querySnapshot.first()

                    if(tripDoc["endTime"] == null) {

                        db.collection("Trip").document(userID).update("endDestination",place)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {exception ->
                                Log.d("EditProfile", "update failed with ", exception)
                            }

                        db.collection("Trip").document(userID).update("endTime",FieldValue.serverTimestamp())
                            .addOnSuccessListener {
                                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {exception ->
                                Log.d("EditProfile", "update failed with ", exception)
                            }

                    }
                    else{
                        val tripID = UUID.randomUUID().toString()
                        val userID = FirebaseAuth.getInstance().currentUser!!.uid
                        val tripMap = hashMapOf(
                            "User_ID" to userID,
                            "Trip_ID" to tripID,
                            "startDestination" to place,
                            "startTime" to FieldValue.serverTimestamp(),
                            "endDestination" to null,
                            "endTime" to null,
                        )

                        db.collection("Trip").document(userID).set(tripMap).addOnSuccessListener {
                            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)

                        }
                    }

                }
            }
        }


    }

    private fun showCamera() {

        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan QR code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViews()
    }

    private fun initViews() {
        binding.fab.setOnClickListener {
            checkPermissionCamera(this)
        }
    }

    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "CAMERA permission required", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissionlauncher.launch(android.Manifest.permission.CAMERA)
        }

    }

    private fun initBinding() {
        binding = ActivityQrscanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}