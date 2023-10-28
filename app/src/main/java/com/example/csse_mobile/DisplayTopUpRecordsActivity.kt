package com.example.cssenew

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class DisplayTopUpRecordsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var topUpRecordAdapter: TopUpRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_top_up_records)

        recyclerView = findViewById(R.id.recyclerViewTopUpRecords)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch top-up records from Firebase Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("topups").get()
            .addOnSuccessListener { documents ->
                val records = mutableListOf<TopUpRecord>()
                for (document in documents) {
                    val record = document.toObject(TopUpRecord::class.java)
                    records.add(record)
                }
                topUpRecordAdapter = TopUpRecordAdapter(records)
                recyclerView.adapter = topUpRecordAdapter
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }
}
