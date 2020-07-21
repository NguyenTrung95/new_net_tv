package com.eliving.tv.ui

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import com.eliving.tv.R
import com.eliving.tv.base.activity.BaseVMActivity
import com.eliving.tv.base.dialog.LoadingIndicator
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.config.Settings
import com.eliving.tv.ext.optJsonObject
import com.eliving.tv.ext.optString
import com.eliving.tv.ext.visible
import com.eliving.tv.service.user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class  MainActivity : BaseVMActivity() {

    private val viewModel : UserViewModel by viewModel()
    private lateinit var navController:NavController
    private lateinit var navGraph:NavGraph
    private val loadingDialog by lazy {
        LoadingIndicator(this)
    }
    var isError = false;

    companion object {
        private var TAG = MainActivity::class.java.name
        fun start(context : Context){
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }


    }

    override fun getLayoutId(): Int = R.layout.activity_launch
    override fun initView() {
        loadingDialog.show()
        navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        navGraph =
            navController.navInflater.inflate(R.navigation.navigation)

        Settings.Auth.isAuth = false
       viewModel.authorInfo().observe(this, Observer {rs ->
           val status = rs.optString("status")
           if (status == "success"){
               loadingDialog.hide()
               var token = ""
               rs.optJsonObject("data")?.let {
                   token = it.optString("Authorization")
               }
               token.let { Settings.Account.token = token.replace("Bearer","").trim() }

                navGraph.startDestination = R.id.home_screen
                navController.graph = navGraph

           }
       })


    }

    override fun handleError() {
        loadingDialog.hide()
        isError = true
        navGraph.startDestination = R.id.login_screen
        navController.graph = navGraph
    }

    override fun onBackPressed() {

    }



    override fun getViewModel(): BaseViewModel = viewModel

    }

