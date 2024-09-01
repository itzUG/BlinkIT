package com.example.dontblink

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dontblink.activity.UserMainActivity
import com.example.dontblink.databinding.FragmentOtpBinding
import com.example.dontblink.models.Users
import com.example.dontblink.viewModel.authViewModel
import kotlinx.coroutines.launch

class OtpFragment : Fragment() {

    private val viewModel : authViewModel by viewModels()
    private lateinit var binding: FragmentOtpBinding
    private lateinit var userNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        getUserNumber()
        backtosigninActivity()
        customisingEbtryOtp()
        sendOtp()
        onLoginButtonClicked()
        return binding.root
    }

    private fun onLoginButtonClicked() {
        binding.btnVerifyOtp.setOnClickListener {
            Utils.showDialog(requireContext(),"Signing You")
            val editText = arrayOf(binding.etOtpDigit1,binding.etOtpDigit2,binding.etOtpDigit3,binding.etOtpDigit4,binding.etOtpDigit5,binding.etOtpDigit6)
            val otp = editText.joinToString(""){it.text.toString() }
            if(otp.length<editText.size){
                Utils.showToast(requireContext(),"Please Enter Valid Otp")
            }else{
                editText.forEach {
                    it.text.clear();
                    it.clearFocus()
                }
                verifyOtp(otp)
            }
        }
    }

    private fun verifyOtp(otp:String) {
        val user = Users(uid = Utils.getCurrentUserId() , userPhoneNumber = userNumber , userAddress = null)
        viewModel.signInWithPhoneAuthCredential(otp,userNumber,user)
        lifecycleScope.launch {
            viewModel.iSignedSuccesfully.collect{
                if(it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(),"Logged IN sucessfully")
                    startActivity(Intent(requireActivity(),UserMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun sendOtp() {

        Utils.showDialog(requireContext(),"Otp Sent")
        viewModel.apply {
            sendOtp(userNumber,requireActivity())
            lifecycleScope.launch {
                  otpSend.collect{
                      if(it){
                          Utils.hideDialog()
                          Utils.showToast(requireContext(),"Otp Sent")
                      }
                }
            }
        }
    }

    private fun customisingEbtryOtp() {
        val editText = arrayOf(binding.etOtpDigit1,binding.etOtpDigit2,binding.etOtpDigit3,binding.etOtpDigit4,binding.etOtpDigit5,binding.etOtpDigit6)
        for(i in editText.indices){
            editText[i].addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if(s?.length==1){
                        if(i<editText.size-1){
                            editText[i+1].requestFocus()
                        }
                    }else if(s?.length==0){
                        if(i>0){
                            editText[i-1].requestFocus()
                        }
                    }
                }

            })
        }
    }

    private fun backtosigninActivity() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_signInFragment)
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()
        binding.tvPhoneNumber.text = "We've sent an OTP to $userNumber"
    }
}
