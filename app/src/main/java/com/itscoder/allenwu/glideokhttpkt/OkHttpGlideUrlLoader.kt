package com.itscoder.allenwu.glideokhttpkt

import android.content.Context
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import okhttp3.OkHttpClient
import java.io.InputStream
import com.bumptech.glide.load.model.GenericLoaderFactory
import com.bumptech.glide.load.model.ModelLoaderFactory



class OkHttpGlideUrlLoader(client: OkHttpClient): ModelLoader<GlideUrl,InputStream>{

    private var okHttpClient: OkHttpClient = client

    class Factory (client: OkHttpClient): ModelLoaderFactory<GlideUrl, InputStream> {

        private var client: OkHttpClient = client

        private val okHttpClient: OkHttpClient
            @Synchronized get() {
                if (client == null) {
                    client = OkHttpClient()
                }
                return client
            }

        override fun build(context: Context, factories: GenericLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpGlideUrlLoader(okHttpClient)
        }

        override fun teardown() {}
    }

    override fun getResourceFetcher(model: GlideUrl, width: Int, height: Int): DataFetcher<InputStream> {
        return OkHttpFetcher(okHttpClient, model)
    }
}