package com.hsd.avh.standstrong.utilities

/**
 * Constants used throughout the app.
 */
const val DATABASE_NAME = "sstrong-db"
//const val TEMP_POST_DATA = "posts_1000.json"
//const val TEMP_PEOPLE_DATA = "person_1000.json"
//const val TEMP_AWARD_DATA = "awards_10.json"
enum class UserTypes(val userGroupName: String,val userGroupCode:String) {
    C2222("Counsellor","2222"),
    C3333("Supervisor","3333"),
    C1111("Admin","1111"),
    C5555("RA","5555")
}

