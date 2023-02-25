package com.sunnyweather.android.ui.roomList

import android.content.Intent
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.chad.library.adapter.base.module.LoadMoreModule
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.RoomInfo
import com.sunnyweather.android.ui.customerUIs.BlackWhiteTransformation
import com.sunnyweather.android.ui.liveRoom.LiveRoomActivity
import kotlinx.android.synthetic.main.activity_liveroom.*

class RoomListAdapter(private val fragment: Fragment, private val roomList: ArrayList<RoomInfo>) :
    RecyclerView.Adapter<RoomListAdapter.ViewHolder>(), LoadMoreModule{

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomPic: ImageView = view.findViewById(R.id.roomPic)
        val ownerPic: ImageView = view.findViewById(R.id.ownerPic)
        val ownerName: TextView = view.findViewById(R.id.ownerName)
        val roomName: TextView = view.findViewById(R.id.roomName)
        val roomCategory: TextView = view.findViewById(R.id.roomCategory)
        val liveNum: TextView = view.findViewById(R.id.liveNum)
        val notLive: TextView = view.findViewById(R.id.room_not_live)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.layoutPosition
            val roomInfo = roomList[position]
            val intent = Intent(parent.context, LiveRoomActivity::class.java).apply {
                putExtra("platform", roomInfo.platForm)
                putExtra("roomId", roomInfo.roomId)
            }
            fragment.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roomInfo = roomList[position]
        holder.ownerName.text = SunnyWeatherApplication.platformName(roomInfo.platForm) + "·" + roomInfo.ownerName
        holder.roomCategory.text = roomInfo.categoryName
        holder.liveNum.text = getWan(roomInfo.online)
        holder.roomName.text = roomInfo.roomName

        if((roomInfo.ownerHeadPic)?.startsWith("http://") == true){
            (roomInfo.ownerHeadPic) = (roomInfo.ownerHeadPic).replaceFirst("http://", "https://")
        }
        if((roomInfo.ownerPic)?.startsWith("http://") == true){
            (roomInfo.ownerPic) = (roomInfo.ownerPic).replaceFirst("http://", "https://")
        }
        if((roomInfo.roomPic)?.startsWith("http://") == true){
            (roomInfo.roomPic) = (roomInfo.roomPic).replaceFirst("http://", "https://")
        }

        if (roomInfo.isLive == 1) {
            Glide.with(fragment).load(roomInfo.ownerHeadPic).transition(withCrossFade()).into(holder.ownerPic)
            Glide.with(fragment).load(roomInfo.roomPic).transition(withCrossFade()).into(holder.roomPic)
        } else {
            Glide.with(fragment).load(roomInfo.ownerHeadPic).transition(withCrossFade()).into(holder.ownerPic)
            Glide.with(fragment).load(roomInfo.roomPic).transition(withCrossFade()).into(holder.roomPic)
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    private fun getWan(num: Int): String {
        val numString = num.toString().trim()
        return if (numString.length > 4){
            val numCut = numString.substring(0, numString.length-4)
            val afterPoint = numString.substring(numString.length-4,numString.length-3)
            numCut+'.'+afterPoint+'万'
        }else {
            numString+'人'
        }
    }
}
