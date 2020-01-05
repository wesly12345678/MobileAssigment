package com.example.real

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class AdminSignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sign_up)

        // Initialize FireBase Auth
        auth = FirebaseAuth.getInstance()

        btnSU.setOnClickListener {
            signUpUser()
        }

        btnToLogin.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser(){

        if(name.text.toString().isEmpty()){
            name.error = "Please enter username"
            name.requestFocus()
            return
        }

        if(email.text.toString().isEmpty()){
            email.error = "Please enter email"
            email.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if(password.text.toString().isEmpty()){
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        if(password.text.toString().length < 8){
            password.error = "Please enter 8 characters/digits password"
            password.requestFocus()
            return
        }

        if(Cpassword.text.toString().isEmpty()){
            Cpassword.error = "Please enter confirm password"
            Cpassword.requestFocus()
        }

        if(!Cpassword.text.toString().equals(password.text.toString())){
            Cpassword.error = "Both password fields must be identical"
            Cpassword.requestFocus()
        }

        if(role.text.toString().isEmpty()){
            role.error = "Please enter your role"
            role.requestFocus()
        }

//        if(role.checkedRadioButtonId == -1){
//            Toast.makeText(baseContext, "Please select your role",
//              Toast.LENGTH_SHORT).show()
//            role.requestFocus()
//       }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                //saveUserToDB()
                                startActivity(Intent(this,MainActivity::class.java))
                                Toast.makeText(baseContext, "Sign Up successfully.",
                                    Toast.LENGTH_SHORT).show()
                                finish()

                            }
                        }
                } else {
                    Toast.makeText(baseContext, "Sign Up failed. Try Again Later...",
                        Toast.LENGTH_SHORT).show()

                }
                //
            }
    }
}
