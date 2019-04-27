package com.obsez.mobile.meijue.ui.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.facebook.drawee.backends.pipeline.Fresco
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator


fun ImageView.loadUrlViaPicasso(url: String, func: (RequestCreator.() -> RequestCreator)? = null) {
    val rc = Picasso.get().load(url)
    if (func != null) {
        rc.func()
    }
    rc.into(this)
}

/**
 * loadUrlViaGlide("http://...") {
 *     diskCacheStrategy(DiskCacheStrategy.ALL) // 改善磁盘缓存策略，避免多次下载同一图片
 *     override(300, 200) // resize the image
 *     centerCrop()
 *     transform(CircleTransform(context))
 *     placeholder(R.drawable.placeholder)
 *     error(R.drawable.image_not_found)
 * }
 *
 */
fun ImageView.loadUrlViaGlide(url: String, func: (RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>)? = null) {
    val rb = Glide.with(context).load(url)
    func?.let { rb.it() }
    rb.into(this)
}

/**
 * Study the concepts at first while using Fresco;
 *
 * 使用 Fresco 需要事先学习相关概念。
 *
 * https://www.jianshu.com/p/bb32bca8796b
 * https://blog.csdn.net/tiankongcheng6/article/details/53884611
 * https://fucknmb.com/2017/07/27/%E4%B8%80%E7%A7%8D%E4%BD%BF%E7%94%A8Fresco%E9%9D%9E%E4%BE%B5%E5%85%A5%E5%BC%8F%E5%8A%A0%E8%BD%BD%E5%9B%BE%E7%89%87%E7%9A%84%E6%96%B9%E5%BC%8F/
 *
 * TODO 完整地支持 Fresco 需要以后安排时间 (Need a plan to support Fresco fully)
 *
 */
fun ImageView.loadUrlViaFresco(url: String, func: (RequestBuilder<Drawable>.() -> RequestBuilder<Drawable>)? = null) {
    Fresco.initialize(this.context)
    val rb = Glide.with(context).load(url)
    if (func != null) {
        rb.func()
    }
    rb.into(this)
}

