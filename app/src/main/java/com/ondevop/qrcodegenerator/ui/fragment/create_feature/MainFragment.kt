package com.ondevop.qrcodegenerator.ui.fragment.create_feature

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ondevop.qrcodegenerator.databinding.FragmentMainBinding
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel
import com.ondevop.qrcodegenerator.utils.QrUtility
import com.ondevop.qrcodegenerator.utils.QrUtility.generateQrCode
import dagger.hilt.android.AndroidEntryPoint


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

       val primaryColor = QrUtility.provideBackgrounColorPrimary(requireContext())
        binding.backgroundImage.setBackgroundColor(primaryColor)


        binding.createButton.setOnClickListener {
            val text = binding.dataEdittext.text.toString()
            if (text.isNotEmpty()) {

                //This will convert the text to bitmap
                qrBitmap = generateQrCode(text)

                qrBitmap?.let {
                    viewModel.setVisibility(false)
                    viewModel.setBitmapValue(qrBitmap!!)
                    viewModel.setScanResult(text)

                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToQrGeneratedFragment(text))
                } ?: Toast.makeText(requireContext(),"Unable to create",Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(requireContext(),"Please Enter the text",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


}
