package com.ondevop.qrcodegenerator.ui.fragment.saved_screen

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
import com.ondevop.qrcodegenerator.R
import com.ondevop.qrcodegenerator.databinding.FragmentSavedEditBinding
import com.ondevop.qrcodegenerator.ui.fragment.create_screen.QrGeneratedFragmentArgs
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel
import com.ondevop.qrcodegenerator.utils.MainUiEvents
import com.ondevop.qrcodegenerator.utils.QrUtility
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class SavedEditFragment : Fragment() {

    private lateinit var binding : FragmentSavedEditBinding
    private val args: SavedEditFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private var isVisible = false
    private var qrUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentSavedEditBinding.inflate(layoutInflater)

        val savedResultText = args.savedResultText
        binding.editScanResult.text = savedResultText

        viewModel.bitmap.observe(viewLifecycleOwner) { bitmap ->
            bitmap?.let {
                binding.editQrImage.setImageBitmap(bitmap)
            }
        }

        viewModel.isVisible.observe(viewLifecycleOwner) { visible ->
            updateVisibility(visible)
        }

        binding.downloadEditImageView.setOnClickListener {
            val bitmap = viewModel.bitmap.value
            bitmap?.let {
                val qrUri = QrUtility.saveQrToExternalStorage(
                    context = requireContext(),
                    displayText = UUID.randomUUID().toString(),
                    bitmap = bitmap
                )
                this.qrUri = qrUri
                Toast.makeText(requireContext(), "downloaded", Toast.LENGTH_SHORT).show()
                viewModel.setVisibility(true)
            } ?: Toast.makeText(requireContext(), "Qr code is not generated", Toast.LENGTH_SHORT)
                .show()
        }

        binding.shareEditImageView.setOnClickListener {
            val shareIntent = QrUtility.shareQr(qrUri)
            shareIntent?.let {
                startActivity(Intent.createChooser(
                    shareIntent,
                    "Share QR code"
                ))
            } ?: Toast.makeText(requireContext(), "Unable to share", Toast.LENGTH_SHORT).show()
        }

        binding.editBack.setOnClickListener {
            findNavController().navigateUp()
        }


        return binding.root
    }


    private fun updateVisibility(isVisible: Boolean) {
        this.isVisible = isVisible
        if (isVisible) {
            binding.shareEditImageView.visibility = View.VISIBLE
            binding.downloadEditImageView.visibility = View.INVISIBLE
            binding.editDownloadText.text = "Share"
        }
    }



}