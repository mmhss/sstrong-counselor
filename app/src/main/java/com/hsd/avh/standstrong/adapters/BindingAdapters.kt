package com.hsd.avh.standstrong.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.LocaleListCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hornet.dateconverter.DateConverter
import com.hsd.avh.standstrong.StandStrong
import com.hsd.avh.standstrong.utilities.InjectorUtils
import java.text.SimpleDateFormat
import java.util.*

class BindingAdapters {

    companion object {

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

        val sdfDateEng = SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH)
        val sdfDateNep = SimpleDateFormat("EEE, MMM d, yyyy", Locale("ne"))
        val dc = DateConverter()

        @JvmStatic
        fun provideNepaliString(date: Date?) : String {

            val cal = Calendar.getInstance()
            cal.timeInMillis = date?.time ?: System.currentTimeMillis()

            var result = sdfDateEng.format(cal.time)

            val model = dc.getNepaliDate(cal)

            cal.clear()
            cal.set(model.year, model.month, model.day)

            result += "; " + sdfDateNep.format(cal.time)

            return result
        }
    }
}