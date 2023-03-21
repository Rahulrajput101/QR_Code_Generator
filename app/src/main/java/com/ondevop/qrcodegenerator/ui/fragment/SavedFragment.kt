package com.ondevop.qrcodegenerator.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ondevop.qrcodegenerator.R
import com.ondevop.qrcodegenerator.adapter.SavedAdapter
import com.ondevop.qrcodegenerator.databinding.FragmentSavedBinding
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel


class SavedFragment : Fragment() {

    private lateinit var binding : FragmentSavedBinding
    private lateinit var adapter : SavedAdapter

    private val viewModel: MainViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(layoutInflater)

        //viewModel.getData()

        adapter = SavedAdapter()
        binding.savedRecycler.adapter = adapter
        binding.savedRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.savedQrData.observe(viewLifecycleOwner){result ->
            Toast.makeText(requireContext(),"under observer",Toast.LENGTH_SHORT).show()
            result?.let {
                val g =it[0]
                Toast.makeText(requireContext(),"${g.result}",Toast.LENGTH_SHORT).show()
            }




            //adapter.differ.submitList(it)
        }


        return binding.root
    }


}