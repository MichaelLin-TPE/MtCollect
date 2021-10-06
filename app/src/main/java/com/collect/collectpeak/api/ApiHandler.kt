package com.collect.collectpeak.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiHandler {

    companion object{

        private const val BASE_URL = "https://opendata.cwb.gov.tw/"

        private val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        private val retrofit = retrofitBuilder.build()

        private val requestApi = retrofit.create(RequestApi::class.java)

        fun getRequestApi(): RequestApi = requestApi

    }

}