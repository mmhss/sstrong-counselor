package com.hsd.avh.standstrong.data.posts

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
        tableName = "activity",
        indices = arrayOf(Index(value = ["id"],
                unique = true)))
class ApiActivity {
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null
    @SerializedName("captureDate")
    @Expose
    @ColumnInfo(name = "capture_date")
    var captureDate: String? = null
    @SerializedName("latitude")
    @Expose
    @ColumnInfo(name = "latitude")
    var latitude: Double? = null
    @SerializedName("longitude")
    @Expose
    @ColumnInfo(name = "longitude")
    var longitude: Double? = null
    @SerializedName("altitude")
    @Expose
    @ColumnInfo(name = "altitude")
    var altitude: Int? = null
    @SerializedName("accuracy")
    @Expose
    @ColumnInfo(name = "accuracy")
    var accuracy: Int? = null
    @SerializedName("mother")
    @Expose
    @Ignore
    var mother: Mother? = null

    @ColumnInfo(name = "mother_id")
    var motherId: Int? = mother!!.id


    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param id
     * @param mother
     * @param altitude
     * @param longitude
     * @param latitude
     * @param captureDate
     * @param accuracy
     */
    constructor(id: Int?, captureDate: String, latitude: Double?, longitude: Double?, altitude: Int?, accuracy: Int?, mother: Mother) : super() {
        this.id = id
        this.captureDate = captureDate
        this.latitude = latitude
        this.longitude = longitude
        this.altitude = altitude
        this.accuracy = accuracy
        this.mother = mother
    }

    inner class Mother {

        @SerializedName("id")
        @Expose
        @Ignore
        var id: Int? = null

        /**
         * No args constructor for use in serialization
         *
         */
        constructor() {}

        /**
         *
         * @param id
         */
        constructor(id: Int?) : super() {
            this.id = id
        }

    }

}