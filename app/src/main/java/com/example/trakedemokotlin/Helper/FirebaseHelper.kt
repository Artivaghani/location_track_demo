package com.example.trakedemokotlin.Helper

import android.util.Log
import com.example.trakedemokotlin.Model.Driver
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseHelper constructor(driverId: String) {
    companion object {
        private const val ONLINE_DRIVERS = "online_drivers"
    }
    private var firestoreDB: FirebaseFirestore? = null

//    firestoreDB = FirebaseFirestore.getInstance()

var onlineDriverDatabaseReference: FirebaseFirestore? = FirebaseFirestore.getInstance()

//    private val onlineDriverDatabaseReference: FirebaseFirestore = FirebaseFirestore
//        .getInstance()

    init {
        onlineDriverDatabaseReference
//            .onDisconnect()
//            .removeValue()
    }

    fun add(driver: Driver)
    {

        val user: MutableMap<String, Any> = HashMap()
        user["name"] = driver.driverId
        user["lang"] = driver.lng
        user["long"] = driver.lat

        onlineDriverDatabaseReference!!.collection("DATA")
                .add(user)
                .addOnSuccessListener(OnSuccessListener<DocumentReference> { Log.e("Success", " Updated") })
                .addOnFailureListener(OnFailureListener { Log.e("faild", " Updated") })

//        onlineDriverDatabaseReference!!.collection("users")
//                .add(driver)
//                .addOnSuccessListener(OnSuccessListener<DocumentReference> { Log.e("Success", " Updated") })
//                .addOnFailureListener(OnFailureListener { Log.e("Faild", " Updated") })
    }

    fun updateDriver(driver: Driver) {

        val user: MutableMap<String, Any> = HashMap()
        user["name"] = "vaghani Aru"
        user["lang"] = driver.lng
        user["long"] = driver.lat

        onlineDriverDatabaseReference!!.collection("ABC")
                .document(driver.driverId)
                .set(user).addOnSuccessListener {
                    Log.e("Success", " Updated")
                }
                .addOnFailureListener {
                    Log.e("Faild", " Updated")
                }
        Log.e("Driver Info", " Updated")
    }
    fun deleteDriver() {
        onlineDriverDatabaseReference!!.collection("ABC")
                .document("good")
                .delete()
    }

}