package com.ondevop.qrcodegenerator.ui.fragment.saved_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ondevop.qrcodegenerator.adapter.SavedAdapter
import com.ondevop.qrcodegenerator.databinding.FragmentSavedBinding
import com.ondevop.qrcodegenerator.ui.viewModel.MainViewModel
import com.ondevop.qrcodegenerator.utils.MainUiEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
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

        handlingOneTimeEvent()

        adapter = SavedAdapter(SavedAdapter.OnUserClickListener(
            {qrData->
               //handle item click click on item
                viewModel.setBitmapValue(qrData.bitmap!!)
                viewModel.setVisibility(false)
                findNavController().navigate(SavedFragmentDirections.actionSavedFragmentToSavedEditFragment(qrData.result!!))
            },
            {
                //handle the click on delete icon
                viewModel.onEvent(MainUiEvents.DeleteQr(it))
            }
        ))

        binding.savedRecycler.adapter = adapter
        binding.savedRecycler.layoutManager = LinearLayoutManager(requireContext())


        viewModel.savedQrData.observe(viewLifecycleOwner){result ->
            result?.let {
                val sortedList =it.reversed()
               adapter.differ.submitList(sortedList)
            }
        }

        return binding.root
    }

    private fun handlingOneTimeEvent() {
        lifecycleScope.launch {
            viewModel.eventFlow.collectLatest {event->
                when(event){
                    is MainViewModel.UiEvent.ShowSnackbar -> {
                        Snackbar.make(requireView(), event.message,Snackbar.LENGTH_SHORT).show()
                    }
                    else ->{}
                }
            }
        }
    }


}