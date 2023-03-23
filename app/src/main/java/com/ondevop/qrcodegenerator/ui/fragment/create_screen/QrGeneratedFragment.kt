package com.ondevop.qrcodegenerator.ui.fragment.create_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ondevop.qrcodegenerator.databinding.FragmentQrGeneratedBinding
import com.ondevop.qrcodegenerator.utils.MainUiEvents
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel
import com.ondevop.qrcodegenerator.utils.QrUtility
import com.ondevop.qrcodegenerator.utils.QrUtility.saveQrToExternalStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class QrGeneratedFragment : Fragment() {

    private lateinit var binding: FragmentQrGeneratedBinding
    private val args: QrGeneratedFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private var isVisible = false
    private var qrUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQrGeneratedBinding.inflate(layoutInflater)



        val generatedResultText = args.generatedResultText
        binding.generatedScanResult.text = generatedResultText

        viewModel.bitmap.observe(viewLifecycleOwner) { bitmap ->
            bitmap?.let {
                binding.qrImage.setImageBitmap(bitmap)
            }
        }

        viewModel.isVisible.observe(viewLifecycleOwner) { visible ->
            updateVisibility(visible)
        }

        binding.downloadImageView.setOnClickListener {
            val bitmap = viewModel.bitmap.value
            bitmap?.let {
                val qrUri = saveQrToExternalStorage(
                    context = requireContext(),UUID.randomUUID().toString(),
                    bitmap = bitmap
                )
                this.qrUri = qrUri
                Toast.makeText(requireContext(), "downloaded", Toast.LENGTH_SHORT).show()
                viewModel.setVisibility(true)
            } ?: Toast.makeText(requireContext(), "Qr code is not generated", Toast.LENGTH_SHORT)
                .show()
        }

        binding.addImageView.setOnClickListener {
            viewModel.onEvent(MainUiEvents.AddQr)
        }

        binding.shareImageView.setOnClickListener {
            val shareIntent = QrUtility.shareQr(qrUri)
            shareIntent?.let {
                startActivity(Intent.createChooser(
                    shareIntent,
                    "Share QR code"
                ))
            } ?: Toast.makeText(requireContext(), "Unable to share", Toast.LENGTH_SHORT).show()

        }

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }


        return binding.root
    }





    //This method set the visibility when the qr code is downloaded and makes share button visible
    private fun updateVisibility(isVisible: Boolean) {
        this.isVisible = isVisible
        if (isVisible) {
            binding.shareImageView.visibility = View.VISIBLE
            binding.downloadImageView.visibility = View.INVISIBLE
            binding.downloadText.text = "Share"
        }
    }




}