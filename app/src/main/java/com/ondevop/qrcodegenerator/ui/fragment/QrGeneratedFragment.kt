package com.ondevop.qrcodegenerator.ui.fragment

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
import com.ondevop.qrcodegenerator.databinding.FragmentQrGeneratedBinding
import com.ondevop.qrcodegenerator.utils.MainUiEvents
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QrGeneratedFragment : Fragment() {

     private lateinit var binding : FragmentQrGeneratedBinding
     private val args : QrGeneratedFragmentArgs by navArgs()


    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQrGeneratedBinding.inflate(layoutInflater)


        lifecycleScope.launch {
            viewModel.eventFlow.collectLatest { event ->
                when(event){
                    is MainViewModel.UiEvent.saveNote -> findNavController().navigateUp()
                    is MainViewModel.UiEvent.ShowSnackbar ->{ }
                }
            }
        }

        val generatedResultText =args.generatedResultText
        binding.generatedScanResult.text = generatedResultText



        viewModel.bitmap.observe(viewLifecycleOwner){
            binding.qrImage.setImageBitmap(it)
        }

        binding.saveImageView.setOnClickListener {
            viewModel.onEvent(MainUiEvents.SaveQr)
        }

        binding.shareImageView.setOnClickListener {
            Toast.makeText(requireContext(),"clicked",Toast.LENGTH_SHORT).show()
        }









        return binding.root
    }


}