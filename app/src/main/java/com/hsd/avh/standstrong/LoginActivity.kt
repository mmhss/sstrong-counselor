package com.hsd.avh.standstrong

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.content.Intent
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsd.avh.standstrong.databinding.ActivityLoginBinding
import com.hsd.avh.standstrong.utilities.FirebaseUtils
import com.hsd.avh.standstrong.utilities.UserAuthentication
import android.view.inputmethod.InputMethodManager


class LoginActivity : AppCompatActivity() {

    lateinit var bindings : ActivityLoginBinding
    var user: UserAuthentication = UserAuthentication("","","","")
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = StandStrong.firebaseInstance()
        bindings = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindings.userAuthentication = user

        bindings.verifyBtn.startAnimation(AnimationUtils.loadAnimation(this.applicationContext,R.anim.grow))
        bindings.verifyBtn.setOnClickListener {
            user.setCode()
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, FirebaseUtils.loginAttempt(user.code))
            if(user.isValidUser()){
                setFirebaseUser()
                val mainIntent = Intent(this.applicationContext, MainActivity::class.java)
                mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(mainIntent)
                finish()
            } else {
                bindings.verifyBtn.startAnimation(AnimationUtils.loadAnimation(this.applicationContext,R.anim.shake));
                user.clearCode()
                bindings.otp1.setText("")
                bindings.otp2.setText("")
                bindings.otp3.setText("")
                bindings.otp4.setText("")
                bindings.otp1.requestFocus();
            }

        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(bindings.otp1, InputMethodManager.SHOW_IMPLICIT)


        //To change focus as soon as 1 digit is entered in edit text, "shiftRequest" method is created.
        shiftRequest(bindings.otp1, bindings.otp2)
        shiftRequest(bindings.otp2, bindings.otp3)
        shiftRequest(bindings.otp3, bindings.otp4)

    }


    private fun setFirebaseUser(){
        //Sets the user ID property.
        firebaseAnalytics.setUserId(user.userType()?.userGroupCode);
        //Sets a user property to a given value.
        firebaseAnalytics.setUserProperty("user_type", user.userType()?.userGroupName);

    }
    private fun shiftRequest(from: EditText, to: EditText) {
        from.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val otp1 = from.text.toString()
                if (otp1.isNotEmpty()) {
                    from.clearFocus()
                    to.requestFocus()
                    to.isCursorVisible = true
                }
            }
            override fun afterTextChanged(editable: Editable) {

            }
        })


    }
}
