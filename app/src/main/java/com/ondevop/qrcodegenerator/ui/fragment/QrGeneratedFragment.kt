package com.ondevop.qrcodegenerator.ui.fragment

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ondevop.qrcodegenerator.databinding.FragmentQrGeneratedBinding
import com.ondevop.qrcodegenerator.utils.MainUiEvents
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel
import com.ondevop.qrcodegenerator.utils.QrUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID

@AndroidEntryPoint
class QrGeneratedFragment : Fragment() {

    private lateinit var binding: FragmentQrGeneratedBinding
    private val args: QrGeneratedFragmentArgs by navArgs()
    private val viewModel: MainViewModel by activityViewModels()
    private var isVisible = false
    private var qrUri : Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQrGeneratedBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is MainViewModel.UiEvent.AddQr -> {
                        findNavController().navigateUp()
                    }
                    is MainViewModel.UiEvent.ShowSnackbar -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

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

                val qrUri = saveQrToExternalStorage(UUID.randomUUID().toString(), bitmap)
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
            shareQr(qrUri)
        }


        return binding.root
    }


  //This method used to store the Qr code in external memory
    fun saveQrToExternalStorage(displayText: String, bitmap: Bitmap): Uri?{

        val imageCollection = QrUtility.sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayText.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, "${bitmap.width}")
            put(MediaStore.Images.Media.HEIGHT, "${bitmap.height}")

        }

        return try {
            val contentResolver = requireContext().contentResolver
            contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outPutstream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outPutstream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
        } catch (e: IOException) {
            e.printStackTrace()
           null
        }
    }

    //This method set the visibility when the qr code is downloaded and makes share button visible
    fun updateVisibility(isVisible : Boolean){
        this.isVisible = isVisible
        if(isVisible){
             binding.shareImageView.visibility = View.VISIBLE
             binding.downloadImageView.visibility = View.INVISIBLE
             binding.downloadText.text = "Share"
        }
    }


    fun shareQr(qrUri: Uri? ){
        qrUri?.let {
            // Create a share intent with the ACTION_SEND action
            val shareIntent = Intent(Intent.ACTION_SEND).also {
                 it.type ="image/jpeg"
                // Attach the QR code image to the share intent
                 it.putExtra(Intent.EXTRA_STREAM, qrUri)
                it.putExtra(Intent.EXTRA_TEXT,"Hey, I have created this amazing QR code")
            }

            startActivity(Intent.createChooser(shareIntent, "Share QR code"))
        } ?: Toast.makeText(requireContext(),"Unable to share",Toast.LENGTH_SHORT).show()


    }




}