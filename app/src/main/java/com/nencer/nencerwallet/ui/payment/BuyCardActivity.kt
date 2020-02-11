package com.nencer.nencerwallet.ui.payment

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.Observer
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.activity.BaseVMActivity
import com.nencer.nencerwallet.base.adapter.OnItemClickListener
import com.nencer.nencerwallet.base.viewmodel.BaseViewModel
import com.nencer.nencerwallet.base.dialog.AlertType
import com.nencer.nencerwallet.base.dialog.CommonAlertDialog
import com.nencer.nencerwallet.base.dialog.LoadingIndicator
import com.nencer.nencerwallet.config.Settings
import com.nencer.nencerwallet.ext.*
import com.nencer.nencerwallet.ui.main.home.adapter.CardPriceAdapter
import com.nencer.nencerwallet.ui.main.home.adapter.CardAdapter
import com.nencer.nencerwallet.ui.main.home.adapter.OrderAdapter
import com.nencer.nencerwallet.ui.main.home.model.Item
import com.nencer.nencerwallet.ui.main.home.model.OrderData
import com.nencer.nencerwallet.service.payment.PayViewModel
import com.nencer.nencerwallet.service.payment.response.Card
import com.nencer.nencerwallet.service.payment.response.CardList
import kotlinx.android.synthetic.main.content_navitation.*
import kotlinx.android.synthetic.main.activity_payment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class BuyCardActivity : BaseVMActivity() {
    private lateinit var adapter: CardAdapter
    private lateinit var cardAdapter: CardPriceAdapter
    private lateinit var orderAdapter: OrderAdapter
    private val handler = Handler()
    private fun loader(user_id: String,
                       api_token: String,
                       softcard_id: Int) = Runnable {
        mPaymentViewModel.getCardItems(user_id,api_token,softcard_id)
    }
    private val mPaymentViewModel: PayViewModel by viewModel()

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, BuyCardActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_payment

    override fun getViewModel(): BaseViewModel = mPaymentViewModel

    override fun initView() {

        tv_title.text = "Mua thẻ"
        tv_title.setTextColor(resources.getColor(R.color.black_1))

        btn_back_button.setOnClickListener { onBackPressed() }

        initHomeAdapter()
        initCardAdapter()
        initOrderAdapter()

    }

    override fun initData() {
        btn_pay.setOnClickListener {
            Settings.Payment.isPayment = true
            mPaymentViewModel.buyCard(Settings.Account.id,orderAdapter.listItems).observe(this, Observer { rs->
                Settings.Payment.isPayment = false
                val status = rs.optString("error_code")
                status.isNotEmpty().yes {
                    val msg = rs.optString("message")
                    notifyError(msg)
                }.otherwise {
                    PaymentDetailActivity.start(this,
                        OrderData(rs)
                    )
                }
            })
        }

        mPaymentViewModel.getAllCard(Settings.Account.id
            ,Settings.Account.token).observe(this, Observer {
            adapter.setListItems(it.items.apply {
                it.items[0].isSeleted = true
            })
            handler.post(loader(Settings.Account.id
                ,Settings.Account.token,
                it.items[0].id?:0))
        })

        rcyPrice.adapter = cardAdapter

        mPaymentViewModel.liscard.observe(this, Observer {
            if(orderAdapter.listItems.size > 0){
                val data = CardList(it).datas
                data.forEach { currencyCard ->
                    orderAdapter.listItems.forEach {orderCard ->
                        if( currencyCard.name == orderCard.name){
                            currencyCard.isOrder = true
                        }
                    }
                }
                cardAdapter.setListItems(data)
            }else cardAdapter.setListItems(CardList(it).datas)
            cardAdapter.notifyDataSetChanged()
        })
    }



    private fun notifyError(msg: String){
        CommonAlertDialog.Builder(supportFragmentManager)
            .setTitle( msg)
            .setType(AlertType.ERROR)
            .show()
    }

    private fun registerSuccess(msg: String){
        CommonAlertDialog.Builder(supportFragmentManager)
            .setTitle( msg)
            .setType(AlertType.SUCCESS)
            .setOnDismissListener { 
                finish()
            }
            .show()
    }

    private fun initHomeAdapter(){
        adapter = CardAdapter(object :
            OnItemClickListener<Item> {
            override fun onClick(
                item: Item,
                position: Int
            ) {
                item.isSeleted = true
                adapter.listItems.filter { it.id != item.id }.forEach {
                    it.isSeleted = false
                }
                adapter.notifyDataSetChanged()
                handler.post(
                    loader(
                        Settings.Account.id
                        , Settings.Account.token,
                        item.id ?: 0
                    )
                )

            }

        })
        rcyCard.adapter = adapter
    }


    private fun initCardAdapter(){
        cardAdapter = CardPriceAdapter(object :
            OnItemClickListener<Card> {
            override fun onClick(item: Card, position: Int) {
                item.isOrder = !item.isOrder
                item.count = 1

                //order change
                if (item.isOrder) orderAdapter.listItems.add(item)
                else {
                    orderAdapter.listItems.remove(orderAdapter.listItems.filter { it.name == item.name }[0])
                }
                orderAdapter.notifyDataSetChanged()

                //card change
                cardAdapter.notifyDataSetChanged()

                logicShowOrderView()
                showViewPayment()
            }

        })
        rcyCard.adapter = adapter

    }

    private fun initOrderAdapter(){
        orderAdapter =
            OrderAdapter(minusListener = { item, position ->
               if(item.count > 1){
                   item.count = item.count - 1
                   if (item.count <= 0) {

                       //order
                       orderAdapter.listItems.removeAt(orderAdapter.listItems.indexOf(item))
                       orderAdapter.notifyItemRemoved(position)

                       //card
                       try {
                           cardAdapter.listItems[cardAdapter.listItems.indexOf(item)].isOrder = false
                           cardAdapter.notifyDataSetChanged()
                       } catch (e: ArrayIndexOutOfBoundsException) {
                           e.message
                       }

                   }

                   logicShowOrderView()
                   showViewPayment()
                   orderAdapter.notifyDataSetChanged()
               }

            }, plusListener = { item, _ ->
                item.count = item.count + 1
                orderAdapter.notifyDataSetChanged()

                logicShowOrderView()
                showViewPayment()
            })
        rcyMyOrder.adapter = orderAdapter

    }

    private fun showViewPayment(){
        var pay : BigDecimal = BigDecimal.ZERO
        orderAdapter.listItems.forEach {
            pay += (it.price?: BigDecimal.ZERO).times((it.count.toBigDecimal()))
        }

        tv_payment_balance.text = "Số tiền cần thanh toán: ${balance(pay)} ${Settings.Account.currencyCode}"
    }

    private fun logicShowOrderView(){
        if (orderAdapter.listItems.size > 0){
            tv_payment_balance.visible()
            btn_pay.visible()
            txtNotify.gone()
        }else{
            tv_payment_balance.gone()
            btn_pay.gone()
            txtNotify.visible()
        }
    }


}