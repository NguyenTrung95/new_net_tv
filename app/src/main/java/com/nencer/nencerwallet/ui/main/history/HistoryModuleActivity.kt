package com.nencer.nencerwallet.ui.main.history

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentTransaction
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.activity.BaseActivity
import com.nencer.nencerwallet.ui.main.history.pager.*
import kotlinx.android.synthetic.main.activity_history.*


class HistoryModuleActivity : BaseActivity() {

    companion object {
        val TAG = HistoryModuleActivity::class.java.name
        fun start(context: Context,code:Int?=0) {
            val start = Intent(context, HistoryModuleActivity::class.java)
            start.putExtra(TAG,code)
            context.startActivity(start)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_history

    override fun initView() {
        val code = intent.getIntExtra(TAG,0)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        when(code){
            1 -> {
                fragmentTransaction.add(R.id.fl_container, TranferHistoryFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            2 -> {
                fragmentTransaction.add(R.id.fl_container, WithdrawHistoryFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            3 -> {
                fragmentTransaction.add(R.id.fl_container, DepositHistoryFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            4 -> {
                fragmentTransaction.add(R.id.fl_container, ExchangeHistoryFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            5 -> {
                fragmentTransaction.add(R.id.fl_container, TopupHistoryFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }


        btn_back_button.setOnClickListener { finish() }



    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}