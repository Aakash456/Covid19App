package com.Aakash.Chaudhary.Covid19.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Aakash.Chaudhary.Covid19.R;
import com.Aakash.Chaudhary.Covid19.api.CoronaApi;
import com.Aakash.Chaudhary.Covid19.api.CoronaService;
import com.Aakash.Chaudhary.Covid19.data.CountriesResponse;
import com.Aakash.Chaudhary.Covid19.ui.adapter.CountryAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private SearchView searchView;
    private RecyclerView recyclerView;
    private CountryAdapter countryAdapter;
    private List<CountriesResponse> countriesResponseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/* Recyler view supports the display collection of data. it modernized version of the listview and the gridview
* classes provided by the android framework. it addresses several issues that the existing widgets have. it enforced
* a programming style that results in good performance. it also comes with defaults animations for removing and adding
* elements.
* Recycler view allow to use different layout manager for positioning items. It uses a viewHolder to store references
* to the views for one entry in the recycler view. A viewHolder class is a static inner class in your adpater which
* holds references to the relevent views. with these references your code can avoid the time consuming findViewByid()
* method to update the widgets with new Data.
*/
        recyclerView = findViewById(R.id.rvCountry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        countryAdapter = new CountryAdapter();
        recyclerView.setAdapter(countryAdapter);

        countriesResponseList = new ArrayList<>();

        CoronaService coronaService =
                CoronaApi.getRetrofitInstance().create(CoronaService.class);


        Call<List<CountriesResponse>> call = coronaService.getCountries();
        call.enqueue(new Callback<List<CountriesResponse>>() {
            @Override
            public void onResponse(Call<List<CountriesResponse>> call, Response<List<CountriesResponse>> response) {

                countriesResponseList = response.body();


                if (countriesResponseList != null) {
                    for (CountriesResponse countriesResponse : countriesResponseList) {

                        System.out.println("Country Name : " + countriesResponse.getCountry() + " - Death Count : " + countriesResponse.getDeaths() + "\n");

                        countryAdapter.setCountryList(getApplicationContext(), countriesResponseList);


                    }
                }


            }

            @Override
            public void onFailure(Call<List<CountriesResponse>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                countryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                countryAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


}
