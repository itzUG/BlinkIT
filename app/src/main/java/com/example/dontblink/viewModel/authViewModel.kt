package com.example.dontblink.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.dontblink.Utils
import com.example.dontblink.models.Users
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class authViewModel : ViewModel() {


    private val _verifiactionId = MutableStateFlow<String?>(null)
    private val _otpsend = MutableStateFlow(false)
    val otpSend = _otpsend

    private val _isSignedInSuccesfully = MutableStateFlow(false)
    val iSignedSuccesfully = _isSignedInSuccesfully

    private val _isACurrentUser = MutableStateFlow(false)
    val isCurrentUser = _isACurrentUser

    init {

        Utils.getAuthInstance().currentUser?.let {
            _isACurrentUser.value = true
        }

    }

    fun sendOtp(userNumber: String,activity: Activity){

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken)
            {
                _verifiactionId.value = verificationId
                _otpsend.value = true

            }
        }
        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber("+91$userNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(otp: String, userNumber: String, user: Users) {
        val credential = PhoneAuthProvider.getCredential(_verifiactionId.value.toString(), otp )
        Utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(user.uid!!).setValue(user)
                    _isSignedInSuccesfully.value = true
                } else {

                }
            }
    }
}