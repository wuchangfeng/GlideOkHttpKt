package com.itscoder.allenwu.glideokhttpkt

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import okhttp3.OkHttpClient
import java.io.InputStream


class MyGlideModule: GlideModule{

    val DISK_CACHE_SIZE = 500 * 1024 * 1024
    val DISK_CACHE_NAME = "guolinglide"

    override fun applyOptions(context: Context?, builder: GlideBuilder?) {
        builder!!.setDiskCache(ExternalCacheDiskCacheFactory(context, DISK_CACHE_NAME, DISK_CACHE_SIZE))
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888)
    }

    override fun registerComponents(context: Context?, glide: Glide?) {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(ProgressInterceptor())
        val okHttpClient = builder.build()
        glide!!.register(GlideUrl::class.java, InputStream::class.java, OkHttpGlideUrlLoader.Factory(okHttpClient))
    }
}