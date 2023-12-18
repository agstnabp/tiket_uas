package com.example.tiket2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.tiket2.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    //Mendeklarasikan variabel binding yang digunakan untuk melakukan View Binding terhadap layout ActivityMainBinding.
    //Mendeklarasikan variabel prefManager yang digunakan untuk mengelola preferensi pengguna.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi prefManager sebelum digunakan
        prefManager = PrefManager.getInstance(this@MainActivity)
        intentToHomeActivity(prefManager.getRole())

        with(binding){
            viewPager.adapter = TabAdapter(this@MainActivity)
            TabLayoutMediator(tabLayout,viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Login"
                    1 -> "Register"
                    else -> ""
                }
            }.attach()
        }
    }
    fun intentToHomeActivity(role: String) {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            if (role == "user") {
                startActivity(Intent(this@MainActivity, HomeUserActivity::class.java))
                finish()
            } else if (role == "admin") {
                startActivity(Intent(this@MainActivity, HomeAdminActivity::class.java))
                finish()
            }
        }
    }
    //Menerima peran (role) pengguna sebagai parameter dan memeriksa apakah pengguna sudah login.
    //Jika pengguna sudah login, akan diarahkan ke HomeUserActivity jika rolenya "user" atau ke HomeAdminActivity
    // jika rolenya "admin".

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }
    //Menginflasi menu dari menu_options.xml ke dalam objek menu

}