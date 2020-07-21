package com.eliving.tv.ui.nav
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.eliving.tv.R
import com.eliving.tv.base.dialog.AlertType
import com.eliving.tv.base.dialog.CommonAlertDialog
import com.eliving.tv.base.fragment.BaseVMFragment
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.config.Settings
import com.eliving.tv.ext.optString
import com.eliving.tv.ext.otherwise
import com.eliving.tv.ext.yes
import com.eliving.tv.helper.ToastUtil
import com.eliving.tv.service.user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class  RegisterFragment : BaseVMFragment() {

    private val viewModel : UserViewModel by viewModel()

    companion object {
        private var TAG = RegisterFragment::class.java.name
        fun start(context : Context){
            val starter = Intent(context, RegisterFragment::class.java)
            context.startActivity(starter)
        }


    }

    override fun getLayoutRes(): Int = R.layout.fragment_register
    override fun initView() {
        btn_register.setOnClickListener { register() }
        imv_close.setOnClickListener {  Navigation.findNavController(view!!).navigate(R.id.action_register_to_close) }
        viewModel.errorMsg.observe(this, Observer {
            /*loadingDialog.dismiss()*/
            notifyError(it)
        })

    }

    private fun notifyError(msg: String) {
        CommonAlertDialog.Builder(fragmentManager!!)
            .setTitle(msg)
            .setType(AlertType.ERROR)
            .show()
    }
    override fun handleError() {

    }

    private fun register(){
            val username = edt_username.text.toString().trim()
            val email = edt_email.text.toString().trim()
            val password = edt_password.text.toString().trim()
        Settings.Auth.isAuth = false

        username.isEmpty().yes {
                ToastUtil.show("Không được để trống tên đăng nhập")
            }.otherwise {
                password.isEmpty().yes {
                    ToastUtil.show("Không được để trống mật khẩu")
                }.otherwise {
                    viewModel.register(username, password,"${email}@gmail.com").observe(this, Observer { rs ->
                        val status = rs.optString("status")
                        if (status == "success"){
                            notifySuccess()                        }

                    })

                }
            }


    }

    private fun notifySuccess() {
        CommonAlertDialog.Builder(fragmentManager!!)
            .setTitle("Đăng kí thành công !!")
            .setType(AlertType.SUCCESS)
            .setOnDismissListener {
                Navigation.findNavController(view!!).navigate(R.id.action_register_to_login)
            }
            .show()
    }



    override fun getViewModel(): BaseViewModel = viewModel

    }

