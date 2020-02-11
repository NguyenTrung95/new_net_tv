package com.nencer.nencerwallet.ui.user

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.activity.BaseDataBindVMActivity
import com.nencer.nencerwallet.base.viewmodel.BaseViewModel
import com.nencer.nencerwallet.databinding.ActivityForgotPasswordBinding
import com.nencer.nencerwallet.base.dialog.AlertType
import com.nencer.nencerwallet.base.dialog.CommonAlertDialog
import com.nencer.nencerwallet.ext.optInt
import com.nencer.nencerwallet.ext.optString
import com.nencer.nencerwallet.service.user.model.UserForgotModel
import com.nencer.nencerwallet.service.user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.content_navitation.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : BaseDataBindVMActivity<ActivityForgotPasswordBinding>() {

    private val mViewModel: UserViewModel by viewModel()

    private val mUserForgotModel: UserForgotModel by lazy { UserForgotModel() }
    private var type = ""
    private var tmp_token = ""
    private var user = ""

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(
                    context,
                    ForgotPasswordActivity::class.java
                )
            )
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_forgot_password

    override fun getViewModel(): BaseViewModel = mViewModel

    override fun initView() {
        edt_new_pass.visibility = View.GONE
        edt_code.visibility = View.GONE
        tv_title.text = "Quên mật khẩu"

        btn_back_button.setOnClickListener { onBackPressed() }




        activity_forgot_button.setOnClickListener {


            submit()

        }
    }
    private fun submit(){
        val value = edt_email.text.toString().trim()

        if (value == null || value == "") {
            notifyError("Bạn phải nhập thông tin username")
            return
        }
        if (edt_new_pass.visibility == View.VISIBLE) {
            if (edt_new_pass.text.toString() == null || edt_new_pass.text.toString() == "") {
                notifyError("Bạn phải nhập thông tin mật khẩu mới")
                return
            }
            if (edt_code.text.toString() == null || edt_code.text.toString() == "") {
                notifyError("Bạn phải nhập thông tin mã xác nhận")
                return
            }
        }
        if (edt_new_pass.visibility == View.GONE) {

            mViewModel.forgot(value).observe(this, Observer {
                val msg = it.optString("message")

                when (it.optInt("error_code")) {
                    0 -> {
                        CommonAlertDialog.Builder(supportFragmentManager)
                            .setTitle(msg)
                            .setType(AlertType.SUCCESS)
                            .show()
                        tmp_token = it.optString("tmp_token")
                        type = it.optString("type")
                        user = it.optString("username")

                        edt_new_pass.visibility = View.VISIBLE
                        edt_code.visibility = View.VISIBLE
                    }
                    else ->
                        CommonAlertDialog.Builder(supportFragmentManager)
                            .setTitle(msg)
                            .setType(AlertType.ERROR)
                            .show()
                }
            })

        } else {

            mViewModel.forgotVerify(
                user,
                type,
                tmp_token,
                edt_new_pass.text.toString(),
                edt_code.text.toString()
            ).observe(this, Observer {
                val msg = it.optString("message")

                when (it.optInt("error_code")) {
                    0 -> {
                        registerSuccess(msg, "")
                    }
                    else ->
                        notifyError(msg)
                }
            })

        }
    }
    override fun initData() {
        mDataBind.viewModel = mUserForgotModel
    }

    private fun notifyError(msg: String) {
        CommonAlertDialog.Builder(supportFragmentManager)
            .setTitle(msg)
            .setType(AlertType.ERROR)
            .show()
    }

    private fun registerSuccess(msg: String, des: String) {
        CommonAlertDialog.Builder(supportFragmentManager)
            .setTitle(msg)
            .setMessage(des)
            .setType(AlertType.SUCCESS)
            .setOnDismissListener {
                finish()
            }
            .show()
    }
}