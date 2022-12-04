package com.example.firebase1

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail:EditText
    private lateinit var etPass:EditText
    private lateinit var btnRegistrar:Button
    private lateinit var btnLogin:Button

    private lateinit var llAutentication:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById(R.id.et_email)
        etPass = findViewById(R.id.et_pass)
        btnRegistrar = findViewById(R.id.btn_registrar)
        btnLogin = findViewById(R.id.btn_login)

        //llAutentication = findViewById(R.id.ll_autenticar)

        ejecutarAnalitica()
        setup()
    }
    fun ejecutarAnalitica(){
        val analisis:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("mensaje", "Integracion de firebaseAnalytics")
        analisis.logEvent("initScreen", bundle)

    }

    fun setup(){
        title = "Autentificacion"
        btnRegistrar.setOnClickListener{
            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    etEmail.text.toString(), etPass.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            mostrarPrincipal(it?.result?.user?.email?:"", TiposProveedor.BASICO)
                        }
                }.addOnFailureListener {
                    println(it)
                }
            }else{
                mostrarAlerta()
            }
        }
        btnLogin.setOnClickListener{
            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    etEmail.text.toString(), etPass.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        mostrarPrincipal(it?.result?.user?.email?:"", TiposProveedor.BASICO)
                    }
                }
            }else{
                mostrarAlerta()
            }
        }
    }
    fun mostrarAlerta(){
        val builder  = AlertDialog.Builder(this)
        builder.setTitle("Error conexion")
        builder.setMessage("Se ha producido un error de autenticacion del usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog = builder.create()
        dialog.show()
    }

    fun mostrarPrincipal(email:String, proveedor:TiposProveedor){
        val ventana:Intent = Intent(this, ActivityPrincipal::class.java).apply{
            putExtra("email", email)
            putExtra("proveedor", proveedor)
        }
        startActivity(ventana)
    }
}

enum class TiposProveedor{
    BASICO
}