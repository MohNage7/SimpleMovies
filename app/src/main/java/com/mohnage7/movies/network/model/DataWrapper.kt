package com.mohnage7.movies.network.model

class DataWrapper<T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {

        fun <T> success(data: T): DataWrapper<T> {
            return DataWrapper(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): DataWrapper<T> {
            return DataWrapper(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): DataWrapper<T> {
            return DataWrapper(Status.LOADING, data, null)
        }
    }

}
