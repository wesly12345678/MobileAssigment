package com.example.real

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize FireBase Auth
        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        btnLogin.setOnClickListener {
            doLogin()
        }

        btnForget.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.forgot_password, null)
            val username = view.findViewById<EditText>(R.id.username)
            builder.setView(view)
            builder.setPositiveButton("Reset",DialogInterface.OnClickListener { _, _ ->
                forgotPassword(username)
            })
            builder.setNegativeButton("Close",DialogInterface.OnClickListener { _, _ ->  })
            builder.show()
        }

    }

    private fun forgotPassword(username : EditText){
        if(username.text.toString().isEmpty()){
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            return
        }

        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Verification Email Sent.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun doLogin(){
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

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    val user = auth.currentUser
                    updateUI(user)
                    //updateUI(null)
                }
            }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?){
                //val user = auth.currentUser
                if(currentUser != null ){
                    if(currentUser.isEmailVerified){
                        startActivity(Intent(this,MenuActivity::class.java))
                        Toast.makeText(baseContext, "Login successfully.\n" +
                                "Hi, " + currentUser!!.email,
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(baseContext, "Please verify your email address.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                else{
//                    Toast.makeText(baseContext, "Login failed.",
//                        Toast.LENGTH_SHORT).show()
                }

    }
}