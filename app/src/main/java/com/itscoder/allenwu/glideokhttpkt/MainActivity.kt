package com.itscoder.allenwu.glideokhttpkt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget



class MainActivity : AppCompatActivity() {

    var mUrl : String = "https://cn.bing.com/s/hpb/NorthMale_EN-US8782628354_1920x1080.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView(){
        var mImageView = findViewById<ImageView>(R.id.iv_photo)
        var mProgressBar = findViewById<ProgressBar>(R.id.pg_load)
        findViewById<Button>(R.id.btn_load).setOnClickListener(View.OnClickListener {
            ProgressInterceptor.addListener(mUrl,MyProgressListener(mProgressBar))
            Glide.
                    with(this)!!
                    .load(mUrl)!!
                    .into(MyGlideDrawableImageViewTarget(mImageView,mProgressBar,mUrl))
        })
    }

    private class MyProgressListener(private val mProgressBar: ProgressBar) : ProgressListener {

        override fun onProgress(progress: Int) {
            mProgressBar.setProgress(progress)
        }
    }

    private class MyGlideDrawableImageViewTarget(view: ImageView, private val mRoundProgressBar: ProgressBar, private val mUrl: String) : GlideDrawableImageViewTarget(view) {

        override fun onLoadStarted(placeholder: Drawable?) {
            super.onLoadStarted(placeholder)
            mRoundProgressBar.setVisibility(View.VISIBLE)
            mRoundProgressBar.setProgress(1)
        }

        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
            mRoundProgressBar.setVisibility(View.GONE)
            ProgressInterceptor.removeListener(mUrl)
            super.onLoadFailed(e, errorDrawable)
        }

        override fun onResourceReady(resource: GlideDrawable, animation: GlideAnimation<in GlideDrawable>?) {
            super.onResourceReady(resource, animation)
            mRoundProgressBar.setVisibility(View.GONE)
            ProgressInterceptor.removeListener(mUrl)
        }
    }
}
