package com.ondevop.qrcodegenerator.ui

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ondevop.qrcodegenerator.R
import com.ondevop.qrcodegenerator.databinding.ActivityMainBinding
import com.ondevop.qrcodegenerator.utils.QrUtility
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavigation.setupWithNavController(navController)



        navHost.findNavController()
            .addOnDestinationChangedListener{_,destination,_ ->
                when(destination.id){
                    R.id.mainFragment, R.id.scannerFragment, R.id.savedFragment ->
                        binding.bottomNavigation.visibility = View.VISIBLE
                    else ->  binding.bottomNavigation.visibility = View.GONE
                }
            }







        setContentView(binding.root)
    }


}