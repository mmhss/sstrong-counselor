package com.hsd.avh.standstrong.utilities




class UserAuthentication(txt1:String,txt2:String,txt3:String,txt4:String) {
    var t1: String
    var t2: String
    var t3: String
    var t4: String
    lateinit var code: String

    init{
        t1 = txt1
        t2 = txt2
        t3 = txt3
        t4 = txt4
    }


    fun setCode() {
        if(t1.isEmpty()) t1 = t1.replace("","*")
        if(t2.isEmpty()) t2 = t2.replace("","*")
        if(t3.isEmpty()) t3 = t3.replace("","*")
        if(t4.isEmpty()) t4 = t4.replace("","*")
        code = t1+t2+t3+t4
    }
    fun clearCode(){
        t1 = ""
        t2 = ""
        t3 = ""
        t4 = ""
        code = ""
    }

    fun isValidUser():Boolean {
        return enumContains<UserTypes>(code)
    }

    fun userType():UserTypes? {
        return enumValueOfOrNull<UserTypes>(code)
    }


    /**
     * Returns `true` if enum T contains an entry with the specified name.
     */
    inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == "C$name" }
    }

    /**
     * Returns an enum entry with the specified name or `null` if no such entry was found.
     */
    inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? {
        return enumValues<T>().find { it.name == "C$name"}
    }

}