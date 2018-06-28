package com.example.prakharagarwal.moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView title;
    TextView overview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        title=findViewById(R.id.title);
        overview=findViewById(R.id.overview);

        if(getIntent().getStringExtra("title")!=null){
            title.setText(getIntent().getStringExtra("title"));
            overview.setText(getIntent().getStringExtra("overview"));
        }


    }
}
