package com.example.blegarden.scanFragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.blegarden.R
import com.example.blegarden.RecyclerAdapter
import com.example.blegarden.databinding.ScanFragmentBinding
import kotlinx.android.synthetic.main.scan_fragment.*

class ScanFragment : Fragment() {
    private lateinit var viewModel: ScanFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ScanFragmentViewModel::class.java)
        val binding = DataBindingUtil
            .inflate<ScanFragmentBinding>(
                inflater,
                R.layout.scan_fragment,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rv.layoutManager = GridLayoutManager(context, 1)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        viewModel.scanData.observe(viewLifecycleOwner, Observer { btList ->
            rv.adapter = RecyclerAdapter(btList, RecyclerAdapter.DeviceClickListener { device ->
                AlertDialog.Builder(activity)
                    .setMessage("Connect to ${device.device.name ?: device.address}?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.connectDevice(device.device)
                        findNavController().navigate(R.id.action_scanFragment_to_blankFragment)
                    }
                    .setNegativeButton(
                        "No"
                    ) { dialog, _ -> dialog.cancel() }
                    .create().show()
            })
        })
    }
}