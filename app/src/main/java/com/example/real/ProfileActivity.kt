package com.example.real

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.changeprofile.*
import kotlinx.android.synthetic.main.changeprofile.Cpassword
import kotlinx.android.synthetic.main.changeprofile.name

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.changeprofile)

        auth = FirebaseAuth.getInstance()

        btnConfirm.setOnClickListener {
            changePassword()
        }
        btnSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun signOut(){
        auth.signOut()
        Toast.makeText(this,"Logged out successfully.",Toast.LENGTH_SHORT)
            .show()
        startActivity(Intent(this,MainActivity::class.java))

        finish()
    }

//    private fun getUserProfile() {
//        // [START get_user_profile]
//        val user = FirebaseAuth.getInstance().currentUser
//        user?.let {
//            // Name, email address, and profile photo Url
//            val name = user.displayName
//            val email = user.email
//
//
//            // Check if user's email is verified
//            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user.uid
//
//        }
//        // [END get_user_profile]
//    }


    private fun changePassword(){
        if(oldPassword.text.isNotEmpty() && newPassword.text.isNotEmpty() && Cpassword.text.isNotEmpty()){

            if(newPassword.text.toString().equals(Cpassword.text.toString())){
                val user = auth.currentUser
                if(user != null && user.email != null ){
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, oldPassword.text.toString())

                    // Prompt the user to re-provide their sign-in credentials
                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(this,"Re-authentication successfully.",Toast.LENGTH_SHORT)
                                    .show()

                                user?.updatePassword(newPassword.text.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this,"Password change successfully.",Toast.LENGTH_SHORT)
                                                .show()

                                            startActivity(Intent(this,MenuActivity::class.java))

                                            finish()
                                        }
                                    }

                            }else{
                                Toast.makeText(this,"Re-authentication failed",Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }else{
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }

            }else{
                Toast.makeText(this,"Password Mismatching.",Toast.LENGTH_SHORT)
                    .show()
            }

        }else{
            Toast.makeText(this,"Please enter all the fields.",Toast.LENGTH_SHORT)
                .show()
        }
    }
}
