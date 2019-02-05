package com.hsd.avh.standstrong.workers

class MyAPIException : Exception {

    val code: Int
    val serialVersionUID = 7718828512143293558L

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(message: String, cause: Throwable, code: Int) : super(message, cause) {
        this.code = code
    }

    constructor(message: String, code: Int) : super(message) {
        this.code = code
    }

    constructor(cause: Throwable, code: Int) : super(cause) {
        this.code = code
    }


}