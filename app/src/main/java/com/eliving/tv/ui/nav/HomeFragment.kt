package com.eliving.tv.ui.nav

import android.view.View.VISIBLE
import android.view.animation.DecelerateInterpolator
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eliving.tv.R
import com.eliving.tv.base.fragment.BaseVMFragment
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.customview.PagerScroller
import com.eliving.tv.linktaro.launcher.adapter.MyViewPagerAdapter
import com.eliving.tv.service.live.viewmodel.LiveViewModel
import com.eliving.tv.ui.MainActivity
import com.eliving.tv.ui.config.ConfigActivity
import com.eliving.tv.ui.info.ServiceActivity
import com.eliving.tv.ui.launcher.nettv.fragment.TvFragment
import com.eliving.tv.ui.mail.EmailActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : BaseVMFragment(){
    private val userModel : LiveViewModel by viewModel()

    private var loading: Boolean = false
    private var hasLoadedAllItems = false
    private var mTvFragment: TvFragment? = null
    private var mViewPagerAdapter: MyViewPagerAdapter? = null
    private var fragments: ArrayList<Fragment>? = null
    private var tagBtns: IntArray ? = null

    override fun getViewModel(): BaseViewModel = userModel

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initView() {
        this.tagBtns = IntArray(this.radiogroup_tag.childCount)
        for (i in tagBtns!!.indices) {
            this.tagBtns!![i] = this.radiogroup_tag.getChildAt(i).id
        }
        setViewPagerScrollDuration(ll_homepage, 800)
        this.ll_homepage.offscreenPageLimit = this.radiogroup_tag.childCount
        email_item.setOnClickListener {

        }

        service_item.setOnClickListener {

        }

        media_item.setOnClickListener {

        }


        radiogroup_tag.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

            when(checkedId){

                R.id.media_item -> {
                    ConfigActivity.start(activity!!,"")

                }

                R.id.email_item -> {
                    EmailActivity.start(activity!!,"")

                }

                R.id.service_item -> {
                    ServiceActivity.start(activity!!,"")

                }


            }
        })

        this.fragments = ArrayList<Fragment>()
         initDataView()
    }

    private  fun initDataView() {
        mViewPagerAdapter = MyViewPagerAdapter(activity?.supportFragmentManager, this.fragments)
        mTvFragment = TvFragment()
        this.fragments?.add(mTvFragment!!)
        this.ll_homepage.adapter = mViewPagerAdapter
        this.ll_homepage.visibility = VISIBLE
    }


    private fun setViewPagerScrollDuration(viewPager: ViewPager, duration: Int) {
        try {
            val field = ViewPager::class.java.getDeclaredField("mScroller")
            field.isAccessible = true
            val scroller = PagerScroller(activity, DecelerateInterpolator())
            scroller.setmDuration(duration)
            field[viewPager] = scroller
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}