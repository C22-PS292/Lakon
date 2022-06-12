package com.bangkit2022.capstone.lakon.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit2022.capstone.lakon.R
import com.bangkit2022.capstone.lakon.databinding.ItemWayangBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DataAdapter(var list: MutableList<Wayang> = mutableListOf(),
                  val listener: OnItemClickCallback? = null)
    :RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_wayang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val binding = ItemWayangBinding.bind(itemView)
        fun bind(data: Wayang){
            with(itemView){
                binding.tvItemName.text = data.nama
                Glide.with(this)
                    .load(data.avatar)
                    .apply (
                        RequestOptions()
                        .placeholder(R.drawable.nopp)
                        )
                    .into(binding.imgItemPhoto)

                setOnClickListener {
                    listener?.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Wayang)
    }
}