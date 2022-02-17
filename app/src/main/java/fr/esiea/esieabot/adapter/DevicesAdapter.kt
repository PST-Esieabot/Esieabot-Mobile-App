package fr.esiea.esieabot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.esiea.esieabot.MainActivity
import fr.esiea.esieabot.R
import fr.esiea.esieabot.model.DevicesModel

class DevicesAdapter(
    private val context: MainActivity,
    private val devicesList: List<DevicesModel>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<DevicesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val deviceName =view.findViewById<TextView>(R.id.tv_device_name)
        val deviceAddress =view.findViewById<TextView>(R.id.tv_device_address)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDevice = devicesList[position]

        // Met a jour les infos
        holder.deviceName.text = currentDevice.deviceName
        holder.deviceAddress.text = currentDevice.deviceAddress
    }

    override fun getItemCount(): Int {
        return devicesList.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}