package com.example.blegarden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blegarden.databinding.ItemLayoutBinding

class RecyclerAdapter(
    private val items: List<BTDevice>,
    private val clickListener: DeviceClickListener
) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], clickListener)

    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BTDevice, clickListener: DeviceClickListener) {
            binding.clickListener = clickListener
            binding.item = item
            binding.executePendingBindings()
        }
    }

    class DeviceClickListener(val clickListener: (device: BTDevice) -> Unit) {
        fun onClick(device: BTDevice) = clickListener(device)
    }
}