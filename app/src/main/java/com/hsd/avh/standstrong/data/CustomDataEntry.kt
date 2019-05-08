package com.hsd.avh.standstrong.data

import com.anychart.chart.common.dataentry.ValueDataEntry

open class CustomDataEntry internal constructor(x: String, value: Number, value2: Number) :
ValueDataEntry(x, value) {
    init {
        setValue("value2", value2)
    }
}
