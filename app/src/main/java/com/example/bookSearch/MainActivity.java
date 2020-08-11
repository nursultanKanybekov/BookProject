package com.example.bookSearch;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookSearch.models.Book;
import com.example.bookSearch.models.ResponseBody;
import com.example.bookSearch.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {


    private NumbersAdapter numbersAdapter;


    APIDeclaration service = RetrofitInstance.getRetrofitInstance().create(APIDeclaration.class);
    NumbersAdapter.OnItemClickListener clickListener = new NumbersAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Book book) {
            String url = book.volumeInfo.getPreviewLink();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();

        String textSearch = intent.getStringExtra("name");


        RecyclerView recyclerView = findViewById(R.id.rv_book_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        numbersAdapter = new NumbersAdapter(clickListener);

        recyclerView.setAdapter(numbersAdapter);

        String langRestrict = "en";
        int maxResults = 40;
        String printType = "books";
        String key = "AIzaSyAyHcoY2k3k0vcKe0fkFqxPF4EqNHsI26Y";
        service.getBooks(langRestrict, maxResults, printType, key, textSearch)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.body() != null) {
                            numbersAdapter.setData(response.body().bookList);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Sorry we can't find this book",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }
}


