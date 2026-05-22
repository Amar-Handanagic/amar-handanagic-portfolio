package etf.ri.rma.newsfeedapp.data.network.exception

import java.io.IOException

class InvalidImageURLException : IOException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable?) : super(message, cause)
}