package com.ondevop.qrcodegenerator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ondevop.qrcodegenerator.databinding.ItemSavedBinding
import com.ondevop.qrcodegenerator.db.QrData

class SavedAdapter(val onUserClickListener: OnUserClickListener) : RecyclerView.Adapter<SavedAdapter.MySavedViewHolder>()  {


    val differCallBack = object : DiffUtil.ItemCallback<QrData>(){
        override fun areItemsTheSame(oldItem: QrData, newItem: QrData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QrData, newItem: QrData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this,differCallBack)

    fun submitList(list: List<QrData>) = differ.submitList(list)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySavedViewHolder {
      return MySavedViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MySavedViewHolder, position: Int) {
        val qr = differ.currentList[position]
        holder.itemView.setOnClickListener {
            onUserClickListener.onClick(qr)
        }
        holder.bind(qr,onUserClickListener)
    }

    class MySavedViewHolder(private val binding : ItemSavedBinding) : RecyclerView.ViewHolder(binding.root) {


        companion object{
            fun from(parent: ViewGroup) : MySavedViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding =ItemSavedBinding.inflate(inflater,parent,false)
                return MySavedViewHolder(binding)
            }
        }



        fun bind(qrData: QrData,onUserClickListener: OnUserClickListener){
            Glide.with(binding.root).load(qrData.bitmap).into(binding.itemImageView)

            binding.itemTextView.text = qrData.result

            binding.deleteImageView.setOnClickListener {
                onUserClickListener.onDeleteClick(qrData)
            }




        }


    }


    class OnUserClickListener(val clickListener: (qrData : QrData) -> Unit, val deleteClickListener : (qrData : QrData) -> Unit) {
        fun onClick(qrData: QrData) = clickListener(qrData)
        fun onDeleteClick(qrData: QrData) =deleteClickListener(qrData)
    }


}