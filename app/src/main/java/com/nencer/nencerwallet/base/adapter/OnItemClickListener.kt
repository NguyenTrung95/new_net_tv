package com.nencer.nencerwallet.base.adapter

interface OnItemClickListener<T> {
    fun onClick(item:T, position: Int)
}