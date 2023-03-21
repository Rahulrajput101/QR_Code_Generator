package com.ondevop.qrcodegenerator.ui.fragment

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Camera
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.ondevop.qrcodegenerator.databinding.FragmentMainBinding
import com.ondevop.qrcodegenerator.utils.QrUtility.generateQrCode
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private var qrBitmap: Bitmap? = null



   private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(layoutInflater)




        binding.createButton.setOnClickListener {
            val text = binding.dataEdittext.text.toString()
            if (text.isNotEmpty()) {
                qrBitmap = generateQrCode(text)

                qrBitmap?.let {

                    viewModel.setBitmapValue(qrBitmap!!)
                    viewModel.setScanResult(text)

                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToQrGeneratedFragment(text))
                } ?: Toast.makeText(requireContext(),"Unable to create",Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(requireContext(),"Please Enter the text",Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(requireContext(), "$text", Toast.LENGTH_LONG).show()
        }





        return binding.root
    }






}