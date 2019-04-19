package com.hsd.avh.standstrong

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsd.avh.standstrong.databinding.ActivityLoginBinding
import com.hsd.avh.standstrong.utilities.FirebaseUtils
import com.hsd.avh.standstrong.utilities.UserAuthentication
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.hsd.avh.standstrong.data.SignInResponse
import com.hsd.avh.standstrong.managers.AnalyticsManager
import com.hsd.avh.standstrong.utilities.Const
import com.hsd.avh.standstrong.utilities.SSUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class LoginActivity : BaseActivity() {

    lateinit var bindings : ActivityLoginBinding
    private var user: UserAuthentication = UserAuthentication("","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindings = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindings.userAuthentication = user

        if (PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext()).getString(Const.ARG_TOKEN, "") != "")
            moveToMain()

        bindings.verifyBtn.startAnimation(AnimationUtils.loadAnimation(this.applicationContext,R.anim.grow))
        bindings.verifyBtn.setOnClickListener {
            user.setCode()
            analyticsManager.trackEvent(FirebaseAnalytics.Event.LOGIN, FirebaseUtils.loginAttempt(user.code))
            if(user.isValidUser()) {

                SSUtils.login(user.code, "111111", object : Callback<SignInResponse> {
                    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {

                        Log.e(SSUtils.TAG, "error while sign in: " + Log.getStackTraceString(t))

                        analyticsManager.trackEvent("Login failed")

                        Toast.makeText(this@LoginActivity, R.string.login_failed, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {

                        Log.d(SSUtils.TAG, "success login " + response.body())

                        Toast.makeText(this@LoginActivity, R.string.login_success, Toast.LENGTH_SHORT).show()

                        PreferenceManager.getDefaultSharedPreferences(StandStrong.applicationContext()).edit().putString(Const.ARG_TOKEN, response.body()!!.token).apply()

                        setFirebaseUser()
                        StandStrong.setUser(user)
                        moveToMain()
                    }
                })
            } else {

                bindings.verifyBtn.startAnimation(AnimationUtils.loadAnimation(this.applicationContext,R.anim.shake))
                user.clearCode()
                bindings.otp1.setText("")
                bindings.otp2.setText("")
                bindings.otp3.setText("")
                bindings.otp4.setText("")
                bindings.otp1.requestFocus()

                analyticsManager.trackEvent("User typed wrong code")
            }

        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(bindings.otp1, InputMethodManager.SHOW_IMPLICIT)


        //To change focus as soon as 1 digit is entered in edit text, "shiftRequest" method is created.
        shiftRequest(bindings.otp1, bindings.otp2)
        shiftRequest(bindings.otp2, bindings.otp3)
        shiftRequest(bindings.otp3, bindings.otp4)

    }

    private fun moveToMain() {
        val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(mainIntent)
        finish()
    }


    private fun setFirebaseUser(){
        //Sets the user ID property.
        analyticsManager.setUserId(user.userType()?.userGroupCode)
        //Sets a user property to a given value.
        analyticsManager.setUserProperty("user_type", user.userType()?.userGroupName)

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
