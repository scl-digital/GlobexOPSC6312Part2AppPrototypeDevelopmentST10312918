package com.example.opsc6312part2appprototypedevelopmentst10312918.data.api

import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.ConversionResponse
import com.example.opsc6312part2appprototypedevelopmentst10312918.data.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    
    @GET("v6/{apiKey}/latest/{baseCurrency}")
    suspend fun getLatestRates(
        @Path("apiKey") apiKey: String,
        @Path("baseCurrency") baseCurrency: String
    ): Response<ExchangeRateResponse>
    
    @GET("v6/{apiKey}/pair/{fromCurrency}/{toCurrency}")
    suspend fun getConversionRate(
        @Path("apiKey") apiKey: String,
        @Path("fromCurrency") fromCurrency: String,
        @Path("toCurrency") toCurrency: String
    ): Response<ConversionResponse>
    
    @GET("v6/{apiKey}/pair/{fromCurrency}/{toCurrency}/{amount}")
    suspend fun convertAmount(
        @Path("apiKey") apiKey: String,
        @Path("fromCurrency") fromCurrency: String,
        @Path("toCurrency") toCurrency: String,
        @Path("amount") amount: Double
    ): Response<ConversionResponse>
}