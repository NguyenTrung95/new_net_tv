package com.eliving.tv.ui.launcher.nettv.activity

import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.eliving.tv.R
import com.eliving.tv.base.activity.BaseVMActivity
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.config.Settings
import com.eliving.tv.service.live.viewmodel.LiveViewModel
import com.eliving.tv.service.user.model.response.LiveResponse
import com.eliving.tv.ui.channels.AdapterLive
import com.eliving.tv.ui.launcher.nettv.adapter.LiveAdapter
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.page_live.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LiveCategoryActivity : BaseVMActivity() , AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{
    private var isLive = false
    private val liveViewModel: LiveViewModel by viewModel()
    private lateinit var adapter: AdapterLive
    private var datas: ArrayList<LiveResponse> = ArrayList()
    private var position = 0
    override fun getLayoutId(): Int {
        return R.layout.page_live
    }

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        getChannel()
    }
    fun getChannel() {

        Settings.Auth.isAuth = true

        liveViewModel.getLiveChannel().observe(this, Observer {
            val data = it
            val jsonArr: JsonArray = data.getAsJsonArray("data")
            jsonArr.map { jsonElement -> jsonElement.asJsonObject }.map {
                datas.add(LiveResponse(it))
            }
            Log.d("thinh", datas.size.toString())
            for (i in 0 until datas.size - 1) {
                datas[i].isSelected = false
            }
            position = 0
            datas[0].isSelected = true
            adapter = AdapterLive(object : OnItemClickListener<LiveResponse> {
                override fun onClick(item: LiveResponse, position: Int) {
//                initChannel(item.day?:"",channelsData?.get(channelsPos)?.id?:"30")
                }

            })
            channellist.requestFocus()
            adapter.setListItems(datas)
            channellist.adapter = adapter
            channellist.visibility = VISIBLE
            li_live_list_layout.visibility = VISIBLE



        })

    }

    override fun getViewModel() =  liveViewModel
    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        val keyCode = event.keyCode
        Log.d("thinh", "[keycode]$keyCode")
        Log.d("thinh", "[KeyEvent..Action]" + event.action)
        Toast.makeText(this, "keyCode == >" + keyCode , Toast.LENGTH_SHORT).show()

        if (event.action == 1) {
            when (keyCode) {
                183, 184, 186 -> return true
            }
        } else if (event.action == 0) {
            when (keyCode) {
                4 -> {
                    onBackPressed()
                    return true
                }

                19 -> {
                    // giam kenh
                    for (i in 0 until datas.size - 1) {
                        if(datas[i].isSelected){
                            position = i
                            break
                        }
                    }
                    if (position > 0) position -= 1
                    else position = 0
                    for (i in 0 until datas.size - 1) {
                        datas[i].isSelected = false
                    }
                    datas[position].isSelected = true
                    channellist.scrollToPosition(position)
                    adapter?.notifyDataSetChanged()

                    return true
                }
                20 -> {
                    // tang kenh
                    for (i in 0 until datas.size - 1) {
                        if(datas[i].isSelected){
                            position = i
                            break
                        }
                    }
                    if (position < datas.size - 1) position += 1
                    else position = datas.size - 1
                    for (i in 0 until datas.size - 1) {
                            datas[i].isSelected = false
                    }
                    datas[position].isSelected = true
                    channellist.scrollToPosition(position)
                    Log.d("thinh - pos - key20", "${position}")
                    adapter?.notifyDataSetChanged()
                    return true
                }
                21 -> {
                    // trai
                    return true
                }
                22 -> {
                    // phai

                    return true
                }
                23 -> {
                    //ok
                    var mPos = 0
                    for (i in 0 until datas.size - 1) {
                        if(datas[i].isSelected){
                            mPos = i
                            break
                        }
                    }
                    var intent = Intent(this, LivePlayerActivity::class.java)
                    intent.putExtra("url",datas[mPos].stream_url)
                    intent.putExtra("pos", mPos)
                    startActivity(intent)
                    return true
                }
                82 -> {
                    // menu
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        for (i in 0 until datas.size - 1) {
            datas.get(i).isSelected = false
        }
        datas.get(p2).isSelected = true
        adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var intent = Intent(this, LivePlayerActivity::class.java)
        intent.putExtra("url",datas[position].stream_url)
        startActivity(intent)
    }
}