package com.ondevop.qrcodegenerator.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.ondevop.qrcodegenerator.R
import com.ondevop.qrcodegenerator.databinding.FragmentScanResultBinding
import dagger.hilt.android.AndroidEntryPoint


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

        val result = args.scannedResult
        binding.textView.text =result

        return binding.root
    }


}