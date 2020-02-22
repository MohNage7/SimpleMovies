package com.mohnage7.movies.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

import com.mohnage7.movies.base.DataWrapper
import com.mohnage7.movies.db.AppExecutors

// ResultType: Type for the DataWrapper data. (database cache)
// RequestType: Type for the API response. (network request)
abstract class NetworkBoundResource<ResultType, RequestType>(private val appExecutors: AppExecutors) {
    private val results = MediatorLiveData<DataWrapper<ResultType>>()

    // Returns a LiveData object that represents the DataWrapper that's implemented
    // in the base class.
    val asLiveData: LiveData<DataWrapper<ResultType>>
        get() = results

    init {
        init()
    }

    private fun init() {

        // update LiveData for loading status
        results.setValue(DataWrapper.loading(null))

        // observe LiveData source from local db
        val dbSource = loadFromDb()

        results.addSource(dbSource) { ResultType ->

            results.removeSource(dbSource)

            if (shouldFetch(ResultType)) {
                // get data from the network
                fetchFromNetwork(dbSource)
            } else {
                results.addSource(dbSource) { ResultType1 -> setValue(DataWrapper.success(ResultType1)) }
            }
        }
    }

    /**
     * 1) observe local db
     * 2) if <condition></condition> query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5) begin observing local db again to see the refreshed data from network
     *
     * @param dbSource
     */
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        results.addSource(dbSource) { ResultType -> setValue(DataWrapper.loading(ResultType)) }

        val apiResponse = createCall()

        results.addSource(apiResponse) { RequestTypeApiResponse ->
            results.removeSource(dbSource)
            results.removeSource(apiResponse)

            if (RequestTypeApiResponse is ApiResponse.ApiSuccessResponse<*>) {
                // success
                appExecutors.diskIO().execute {
                    // save the response to the local db
                    saveCallResult(processResponse(RequestTypeApiResponse as ApiResponse.ApiSuccessResponse<*>) as RequestType)
                    appExecutors.mainThread().execute { results.addSource(loadFromDb()) { ResultType -> setValue(DataWrapper.success(ResultType)) } }
                }
            } else if (RequestTypeApiResponse is ApiResponse.ApiEmptyResponse<*>) {
                // empty response
                appExecutors.mainThread().execute { results.addSource(loadFromDb()) { ResultType -> setValue(DataWrapper.success(ResultType)) } }
            } else if (RequestTypeApiResponse is ApiResponse.ApiErrorResponse<*>) {
                // api error
                results.addSource(dbSource) { ResultType ->
                    setValue(
                            DataWrapper.error(
                                    (RequestTypeApiResponse as ApiResponse.ApiErrorResponse<*>).errorMessage,
                                    ResultType
                            )
                    )
                }
            }
        }
    }

    private fun processResponse(response: ApiResponse.ApiSuccessResponse<*>): ResultType {
        return response.body
    }

    private fun setValue(newValue: DataWrapper<ResultType>) {
        if (results.value != newValue) {
            results.setValue(newValue)
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}




