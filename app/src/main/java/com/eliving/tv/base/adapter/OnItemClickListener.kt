package com.eliving.tv.base.adapter

interface OnItemClickListener<T> {
    fun onClick(item:T, position: Int)
}