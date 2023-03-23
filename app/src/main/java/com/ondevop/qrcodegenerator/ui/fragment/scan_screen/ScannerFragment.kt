package com.ondevop.qrcodegenerator.ui.fragment.scan_screen

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ondevop.qrcodegenerator.databinding.FragmentScannerBinding
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel
import com.ondevop.qrcodegenerator.utils.QrCodeImageAnalyzer
import com.ondevop.qrcodegenerator.utils.QrUtility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private var isTorchOn = false
    private var camera: Camera? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScannerBinding.inflate(layoutInflater)

        /*This function will check the user have
           the camera permission and handle that
           after it will start scanning the Qr code
         */
        requestPermissionAndStartCamera()

        if(QrUtility.hasCameraPermission(requireContext())){
            startCamera(binding.cameraPreview)
        }

        binding.flashImageView.setOnClickListener {

            //This will turn on and turn off the torch
            toggleTorch()
        }

        return binding.root
    }


    fun requestPermissionAndStartCamera() {
        if (!QrUtility.hasCameraPermission(requireContext())) {
            cameraPremissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val cameraPremissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted){
            Log.d("accepted", " success")
        }else{
           askForFinalPermission()
        }

    }


    /*This is a function that initializes the camera and set preview using a PreviewView.
     and launches a QrCodeImageAnalyzer to  extract QR codes.*/
    private fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(requireContext()),
                QrCodeImageAnalyzer { result ->
                    findNavController().navigate(
                        ScannerFragmentDirections.actionScannerFragmentToScanResultFragment(
                            result
                        )
                    )
                }
            )

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            ).apply {
                camera = this
            }

        }, ContextCompat.getMainExecutor(requireContext()))

    }


    private fun toggleTorch() {
        isTorchOn = !isTorchOn
        val camera = camera ?: return
        val cameraControl = camera.cameraControl

        cameraControl.enableTorch(isTorchOn)
    }


    override fun onResume() {
        super.onResume()
        startCamera(binding.cameraPreview)
    }

    private fun askForFinalPermission() {
        val message = "Camera permission is required to use this feature"
        val intent = Intent()
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("Go to ->") { _, _ ->
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val packageName = requireContext().packageName
                //Toast.makeText(requireContext()," package:$packageName", Toast.LENGTH_LONG).show()
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)

            }
            .setNegativeButton("Deny") { dialogInterface, _ ->
                dialogInterface.dismiss()

            }.create()

        dialog.show()
    }




}