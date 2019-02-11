package com.hsd.avh.standstrong.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.utilities.InjectorUtils
import android.widget.TextView
import androidx.annotation.NonNull
import com.hsd.avh.standstrong.R
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
                .load(imageUrl)
                .into(view)
        //.transition(DrawableTransitionOptions.withCrossFade())
    }
}

@BindingAdapter("srcFromDrawable")
fun setImageDrawable(view: ImageView, imageStr: String?) {
    if (imageStr.isNullOrEmpty()) {
        view.setImageURI(null)
    } else {
        //Note (1): no path (and no extension, in case of images).
        //Note (2): use "drawable" for drawables, "string" for strings, .
        val resId = InjectorUtils.getResourceID(imageStr, "drawable", StandStrong.applicationContext())
        //view.setImageResource(resId)
        Glide.with(view.context)
                .load(resId)
                .into(view)
    }
}

@BindingAdapter("readableDate")
fun bindServerDate(textView: TextView, date: Date) {
    var sdfDate = SimpleDateFormat("EEE, MMM d, yyyy")
    textView.text = sdfDate.format(date)
}
