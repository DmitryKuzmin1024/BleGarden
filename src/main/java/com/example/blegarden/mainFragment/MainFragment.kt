package com.example.blegarden.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.blegarden.GattClient
import com.example.blegarden.R
import com.example.blegarden.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        val binding = DataBindingUtil
            .inflate<MainFragmentBinding>(
                inflater,
                R.layout.main_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.scanButton.setOnClickListener {
            GattClient.disconnectGatt()
            findNavController().navigate(R.id.action_blankFragment_to_scanFragment)
        }
        return binding.root
    }
}