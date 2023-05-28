package com.rakib.qrcode

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rakib.qrcode.Fragments.GenerateFragment
import com.rakib.qrcode.Fragments.HistoryFragment
import com.rakib.qrcode.Fragments.ScanFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        window.statusBarColor = Color.parseColor("#ffffff");

        if (savedInstanceState == null) {
            startFragment(ScanFragment())
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.scan

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.generate -> {
                    startFragment(GenerateFragment())
                    true
                }
                R.id.scan -> {
                    startFragment(ScanFragment())
                    true
                }
                R.id.history -> {
                    startFragment(HistoryFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun startFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .commit()
    }

}