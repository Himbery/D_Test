package com.img23.d_test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.img23.d_test.Model.Photo
import com.img23.d_test.Services.PhotoManager
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope

class MainActivity : AppCompatActivity() {

    val adapter = PhortoAdapter()
    lateinit var photoList: RecyclerView
    val photoManager: PhotoManager = PhotoManager()
    lateinit var pullToRefresh: SwipeRefreshLayout
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    val count: Int = 30
    var offset: Int = 0
    var isLoading: Boolean = false
    var friends: List <Photo> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VK.login(this, arrayListOf(VKScope.FRIENDS, VKScope.WALL))

        photoList = findViewById(R.id.photo_list)
        photoList.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)

        scrollListener = object : RecyclerView.OnScrollListener() {      //скроллинг
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layout: StaggeredGridLayoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val totalItemCount = layout.itemCount

                val lastItemPositions = layout.findLastVisibleItemPositions(null)
                val lastVisibleItemPosition = getLastVisibleItem(lastItemPositions)     //определяется последняя загруженная строка

                if ((lastVisibleItemPosition >= totalItemCount - 1) && !isLoading) {         // если последняя загруженная строка больше, чем общее
                   addPhoto(friends)                                                         // загруженное число строк, идет новая загруззка
                }
            }
        }
        photoList.addOnScrollListener(scrollListener)

        addPhoto(friends)

        pullToRefresh = findViewById(R.id.pullToRefresh)      //обновление списка
        pullToRefresh.setOnRefreshListener {
            offset = 0

            addPhoto(friends)
            pullToRefresh.isRefreshing = false
        }
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
            }

            override fun onLoginFailed(errorCode: Int) {
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun addPhoto(friends: List<Photo>) {
        isLoading = true

        photoManager.search(friends, offset, count, { users -> Unit
            if(users.isEmpty()) {
                Toast.makeText(this, "Ничего не найдено", Toast.LENGTH_LONG).show();
                return@search
            }

            if (offset > 0) {
                adapter.addData(users)          //загрузка новых строк

            } else {
                adapter.setData(users)         //первичная загрузка списка
                photoList.adapter = adapter
            }

            offset = offset + count
            isLoading = false

        })
    }
}
