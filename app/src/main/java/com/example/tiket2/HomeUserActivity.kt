package com.example.tiket2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tiket2.databinding.ActivityHomeUserBinding
import com.example.tiket2.ProfileUserFragment

class HomeUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeUserBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi prefManager sebelum digunakan
        prefManager = PrefManager.getInstance(this@HomeUserActivity)

        replaceFragment(ListMovieFragment())

        with(binding){
            bottomNavBar.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.nav_list -> replaceFragment(ListMovieFragment())
                    R.id.nav_bookmark -> replaceFragment(BookmarkFragment())
                    R.id.nav_profile -> replaceFragment(ProfileUserFragment())

                    else -> {}
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    fun logOut() {
        prefManager.setLoggedIn(false)
        prefManager.clear()
        startActivity(Intent(this@HomeUserActivity, MainActivity::class.java))
        finish()
    }
}