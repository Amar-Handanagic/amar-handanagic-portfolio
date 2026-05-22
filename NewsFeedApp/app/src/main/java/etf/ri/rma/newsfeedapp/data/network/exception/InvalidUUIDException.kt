package etf.ri.rma.newsfeedapp.data.network.exception

import java.io.IOException

class InvalidUUIDException : IOException {
    constructor(message: String) : super(message)
}