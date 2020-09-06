package com.Aakash.Chaudhary.Covid19.api;

import com.Aakash.Chaudhary.Covid19.data.AllCountriesResponse;
import com.Aakash.Chaudhary.Covid19.data.CountriesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CoronaService {

    @GET("countries/?sort=country")
    Call<List<CountriesResponse>> getCountries();


    @GET("countries/{country}")
    Call<CountriesResponse> getCountryInfo(

            @Path("country") String country
    );

    @GET("all")
    Call<AllCountriesResponse> getAllCountries();
}
