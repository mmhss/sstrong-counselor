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
        val sdfDateDay = SimpleDateFormat("EEE", Locale.ENGLISH)

        val sdfDateNepMonth = SimpleDateFormat("MM", Locale("ne_EN"))
        val sdfDateNepDate = SimpleDateFormat("d, yyyy", Locale("ne"))

        val dc = DateConverter()

        @JvmStatic
        fun provideNepaliString(date: Date?) : String {

            val cal = Calendar.getInstance()
            cal.timeInMillis = date?.time ?: System.currentTimeMillis()

            var result = sdfDateEng.format(cal.time)
            var day = sdfDateDay.format(cal.time)

            val model = dc.getNepaliDate(cal)

            cal.clear()
            cal.set(model.year, model.month, model.day)

            result += "; "
            result += " " + translateToNepaliDay(day)
            result += ", " + translateToNepaliMonth(sdfDateNepMonth.format(cal.time))
            result += " " + sdfDateNepDate.format(cal.time)


            return result
        }

        @JvmStatic
        private fun translateToNepaliMonth(monthNumber: String?): Any? {
            var result = when (monthNumber) {
                "01" -> "बैसाख"
                "02" -> "जेठ"
                "03" -> "असार"
                "04" -> "साउन"
                "05" -> "भाद्र"
                "06" -> "असोज"
                "07" -> "कार्तिक"
                "08" -> "मंसिर"
                "09" -> "पुष"
                "10" -> "माघ"
                "11" -> "फाल्गुन"
                "12" -> "चैत्र"
                else -> monthNumber
            }

            return result
        }

        @JvmStatic
        private fun translateToNepaliDay(day: String?): Any? {
            var result = when (day) {
                "Sun" -> "आइत"
                "Mon" -> "सोम"
                "Tue" -> "मगल"
                "Wed" -> "बुध"
                "Thu" -> "बिहि"
                "Fri" -> "शुक्र"
                "Sat" -> "शनि"
                else -> day
            }

            return result
        }
    }
}