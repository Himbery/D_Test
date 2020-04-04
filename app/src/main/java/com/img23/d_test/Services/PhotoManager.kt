package com.img23.d_test.Services

import com.img23.d_test.Model.Photo
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class PhotoManager {

    fun search( friends: List<Photo>, offset: Int, count: Int,  completion: (users: List<Photo>) -> Unit) {
        VK.execute(VKFriendsRequest( friends, offset, count), object : VKApiCallback<List<Photo>> {
            override fun fail(error: Exception) {
            }

            override fun success(result: List<Photo>) {
                completion(result)
            }
        })
    }

    // определяем нужный метод загрузки и параметры
    class VKFriendsRequest : VKRequest<List<Photo>> {
        constructor( friends: List<Photo>, offset: Int, count: Int) : super("friends.get") {
                addParam("fields", "photo_200_orig")
                addParam("offset", offset)
                addParam("count", count)
        }


        //парсинг данных
        override fun parse(r: JSONObject): List<Photo> {
            val response = r.getJSONObject("response")
            val users = response.getJSONArray("items")
            val result = ArrayList<Photo>()
            for (i in 0 until users.length()) {
                result.add(Photo.parse(users.getJSONObject(i)))
            }
            return result
        }
    }
}