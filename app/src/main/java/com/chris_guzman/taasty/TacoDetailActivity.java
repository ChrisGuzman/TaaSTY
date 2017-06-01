package com.chris_guzman.taasty;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.HensonNavigable;
import com.f2prateek.dart.InjectExtra;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TacoDetailActivity extends AppCompatActivity {
    private static final String TAG = TacoDetailActivity.class.getSimpleName();
    @InjectExtra String name;
    @InjectExtra int id;
    @Nullable @InjectExtra String imageUrl;
    @Nullable @InjectExtra String url;

    @BindView(R.id.saved_taco_img) ImageView savedTacoImg;
    @BindView(R.id.saved_taco_name) TextView savedTacoName;
    @BindView(R.id.saved_taco_description) TextView savedTacoDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taco_detail);
        Dart.inject(this);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            name = bundle.getString("TACO_NAME");
            imageUrl = bundle.getString("TACO_IMAGE_URL");
        }

        savedTacoName.setText(name);
        if (imageUrl != null) {
            Picasso.with(this).load(imageUrl).into(savedTacoImg);
        }
        new TacoDescriptionUtil().getRandomTaco(url).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                savedTacoDescription.setText(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(TacoDetailActivity.this, "error loading description", Toast.LENGTH_LONG).show();
                Log.e(TAG, "error loading description", t);
            }
        });

    }
}
