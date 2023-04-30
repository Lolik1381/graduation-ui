package ru.stankin.compose.model

class CommonResponse<T>(
    var error: Boolean = false,
    var code: Int? = null,
    var message: String? = null,
    var body: T? = null
) {
    constructor(message: String) : this(true, 200, message, null)
    constructor(code: Int, message: String) : this(true, code, message, null)

    companion object {

        fun <T> ok(body: T? = null): CommonResponse<T> {
            return CommonResponse(false, 200, null, body)
        }
    }
}