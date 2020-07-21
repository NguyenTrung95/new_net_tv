package com.eliving.tv.ui.radio

import androidx.lifecycle.Observer
import com.eliving.tv.R
import com.eliving.tv.base.activity.BaseVMActivity
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.config.Settings
import com.eliving.tv.ext.optJsonArray
import com.eliving.tv.service.live.model.response.RadioEntity
import com.eliving.tv.service.live.model.response.RadioResponse
import com.eliving.tv.service.radio.viewmodel.RadioViewModel
import com.eliving.tv.ui.channels.PlayerActivity
import kotlinx.android.synthetic.main.fragment_radio.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RadioActivity : BaseVMActivity(){
    private val viewModel: RadioViewModel by viewModel()

    private lateinit var adapter: RadioAdapter
    private var mSelectcolorIconBg = intArrayOf(
        R.drawable.radio_red_sel,
        R.drawable.radio_green_sel,
        R.drawable.radio_yellow_sel,
        R.drawable.radio_blue_sel
    )
    override fun getLayoutId(): Int = R.layout.fragment_radio

    override fun initView() {
        colorIcon1_image.setBackgroundResource(this.mSelectcolorIconBg[0])
        ad_logo_image.setBackgroundResource(R.drawable.radio_ad1)

        adapter = RadioAdapter(object : OnItemClickListener<RadioEntity>{
            override fun onClick(item: RadioEntity, position: Int) {
                PlayerActivity.start(this@RadioActivity, item.radio_url ?: "")

            }

        })
        rcyRadio.adapter = adapter
        Settings.Auth.isAuth = true
        viewModel.radios().observe(this, Observer {
            val data = it.optJsonArray("data")
            data?.let {
                val radios = RadioResponse(it)
                adapter.setListItems(radios.datas)
            }
        })

    }

    override fun getViewModel(): BaseViewModel = viewModel

}