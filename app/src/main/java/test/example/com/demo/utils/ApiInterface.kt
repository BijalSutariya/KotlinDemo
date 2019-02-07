package test.example.com.demo.utils

import retrofit2.http.GET
import test.example.com.demo.model.MainModel
import io.reactivex.Observable
import retrofit2.Call


interface ApiInterface {

    @GET("jsonandroid")
    fun getData() : Call<MainModel>

}