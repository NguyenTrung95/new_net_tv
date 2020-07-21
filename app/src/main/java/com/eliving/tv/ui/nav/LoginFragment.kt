package com.eliving.tv.ui.nav

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.eliving.tv.R
import com.eliving.tv.base.dialog.AlertType
import com.eliving.tv.base.dialog.CommonAlertDialog
import com.eliving.tv.base.fragment.BaseVMFragment
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.config.Settings
import com.eliving.tv.ext.*
import com.eliving.tv.helper.ToastUtil
import com.eliving.tv.service.user.viewmodel.UserViewModel
import com.eliving.tv.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseVMFragment() {
    private val viewModel : UserViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun getLayoutRes(): Int = R.layout.fragment_login

    override fun initView() {

        btn_login.setOnClickListener {
           login(it)
        }

        btn_register.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_login_screen_to_register)
        }
        val isRootError = (activity as MainActivity?)?.isError?:false

        if (isRootError){
            ll_login.visible()
        }
        viewModel.errorMsg.observe(this, Observer {
            /*loadingDialog.dismiss()*/
            notifyError(it)
        })

    }


    private fun login(view:View) {
        val username = edt_username.text.toString().trim()
        val password = edt_password.text.toString().trim()
        Settings.Auth.isAuth = false
        username.isEmpty().yes {
            ToastUtil.show("Không được để trống tên đăng nhập")
        }.otherwise {
            password.isEmpty().yes {
                ToastUtil.show("Không được để trống mật khẩu")
            }.otherwise {
                viewModel.login(username, password).observe(this, Observer { rs ->
                    val status = rs.optString("status")
                    if (status == "success"){
                        viewModel.authorInfo().observe(this, Observer {rs ->
                            val status = rs.optString("status")
                            if (status == "success"){
                                var token = ""
                                rs.optJsonObject("data")?.let {
                                    token = it.optString("Authorization")
                                }
                                Settings.Auth.isAuth = true
                                token.let { Settings.Account.token = token.replace("Bearer","").trim() }
                                Navigation.findNavController(view).navigate(R.id.action_login_screen_to_home)

                            }
                        })
                    }

                })

            }
        }

    }

    private fun notifyError(msg: String) {
        CommonAlertDialog.Builder(fragmentManager!!)
            .setTitle(msg)
            .setType(AlertType.ERROR)
            .show()
    }


}