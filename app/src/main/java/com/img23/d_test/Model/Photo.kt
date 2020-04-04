package com.img23.d_test.Model

import org.json.JSONObject

data class Photo (
    val id: Int = 0,
    val image: String = ""
) {


    companion object CREATOR {

        fun parse(json: JSONObject) = Photo(
            id = json.optInt("id", 0),
            image = json.optString("photo_200_orig", ""))

    }
}
