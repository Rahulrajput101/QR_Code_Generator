package com.ondevop.qrcodegenerator.ui.fragment.scan_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ondevop.qrcodegenerator.databinding.FragmentScanResultBinding
import com.ondevop.qrcodegenerator.utils.QrUtility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ScanResultFragment : Fragment() {

    private lateinit var binding : FragmentScanResultBinding
    private val args : ScanResultFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanResultBinding.inflate(layoutInflater)

        val primaryColor = QrUtility.provideBackgrounColorPrimary(requireContext())
        binding.backgroundImage.setBackgroundColor(primaryColor)

        val result = args.scannedResult
        binding.scannedResult.text =result

        binding.editBack.setOnClickListener {
            findNavController().navigateUp()
        }

        //Sharing the scan result to another apps
        binding.shareImageView.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).also {
                it.type = "text/plain"
                it.putExtra(Intent.EXTRA_TEXT, result)
            }
            startActivity(Intent.createChooser(shareIntent,"share text with:"))
        }

        return binding.root
    }


}