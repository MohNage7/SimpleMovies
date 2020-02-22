package com.mohnage7.movies.network


import androidx.lifecycle.LiveData
import com.mohnage7.movies.network.model.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory : CallAdapter.Factory() {


    /**
     * This method performs a number of checks and then returns the Response type for the Retrofit requests.
     * (@bodyType is the ResponseType. It can be RecipeResponse or RecipeSearchResponse)
     *
     *
     * CHECK #1) returnType returns LiveData
     * CHECK #2) Type LiveData<T> is of ApiResponse.class
     * CHECK #3) Make sure ApiResponse is parameterized. AKA: ApiResponse<T> exists.
    </T></T> */
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

        // Check #1
        // Make sure the CallAdapter is returning a type of LiveData
        if (CallAdapter.Factory.getRawType(returnType) != LiveData::class.java) {
            return null
        }

        // Check #2
        // Type that LiveData is wrapping
        val observableType = CallAdapter.Factory.getParameterUpperBound(0, returnType as ParameterizedType)

        // Check if it's of Type ApiResponse
        val rawObservableType = CallAdapter.Factory.getRawType(observableType)
        require(!(rawObservableType !== ApiResponse::class.java)) { "Type must be a defined resource" }

        // Check #3
        // Check if ApiResponse is parameterized. AKA: Does ApiResponse<T> exists? (must wrap around T)
        // FYI: T is either RecipeResponse or T will be a RecipeSearchResponse
        require(observableType is ParameterizedType) { "resource must be parameterized" }

        val bodyType = CallAdapter.Factory.getParameterUpperBound(0, observableType)
        return LiveDataCallAdapter<Type>(bodyType)
    }
}




















