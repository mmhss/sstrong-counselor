package com.hsd.avh.standstrong.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hornet.dateconverter.DateConverter
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.utilities.InjectorUtils
import java.text.SimpleDateFormat
import java.util.*

class BindingAdapters {

    companion object {

        val dc = DateConverter()

        @BindingAdapter("isGone")
        @JvmStatic
        fun bindIsGone(view: View, isGone: Boolean) {
            view.visibility = if (isGone) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        @BindingAdapter("imageFromUrl")
        @JvmStatic
        fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(imageUrl)
                    .into(view)
                //.transition(DrawableTransitionOptions.withCrossFade())
            }
        }

        @BindingAdapter("srcFromDrawable")
        @JvmStatic
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
        @JvmStatic
        fun bindServerDate(textView: TextView, date: Date?) {

            if (date != null) {

                val nepaliDate = provideNepaliString(date)

                textView.text = nepaliDate
            }
        }

        @JvmStatic
        fun provideNepaliString(date: Date?) : String {

            var sdfDate = SimpleDateFormat("EEE, MMM d, yyyy")
            val cal = Calendar.getInstance()
            cal.timeInMillis = date?.time ?: Date().time

            val model = dc.getNepaliDate(cal)

            cal.clear()
            cal.set(model.year, model.month, model.day)

            return sdfDate.format(cal.time)
        }
    }
}