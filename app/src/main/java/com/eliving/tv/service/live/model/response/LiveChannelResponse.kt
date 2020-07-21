package com.eliving.tv.service.live.model.response

import com.google.gson.annotations.SerializedName

data class LiveChannelResponse(
    @SerializedName("data") var data: MutableList<LiveObject> = mutableListOf()
)

data class LiveObject(

    @SerializedName("id")
    var id:  String? = "",
    @SerializedName("genre_id")
    var genre_id: String? = "",
    @SerializedName("channel_number")
    var channel_number: String? = "",
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("icon_url")
    var icon_url: String? = "",
    @SerializedName("channel_streams.stream_source_id")
    var stream_source_id: Int = 0,
    @SerializedName("channel_streams.stream_url")
    var stream_url: String? = "",
    @SerializedName("channel_streams.token_url")
    var token_url: String = ""


)
