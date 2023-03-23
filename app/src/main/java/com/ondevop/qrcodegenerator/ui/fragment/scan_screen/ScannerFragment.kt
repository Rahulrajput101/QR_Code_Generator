package com.ondevop.qrcodegenerator.ui.fragment.scan_screen

import android.Manifest
import android.os.Bundle
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
    private val viewModel: MainViewModel by activityViewModels()
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

        binding.flashImageView.setOnClickListener {

            //This will turn on and turn off the torch
            toggleTorch()
        }






        return binding.root
    }


    fun requestPermissionAndStartCamera() {
        if (QrUtility.hasCameraPermission(requireContext())) {
            startCamera(binding.cameraPreview)

        } else {
            cameraPremissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val cameraPremissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        startCamera(binding.cameraPreview)
    }


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





}