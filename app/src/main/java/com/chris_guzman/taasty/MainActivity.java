package com.chris_guzman.taasty;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.f2prateek.dart.Dart;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.description) TextView description;
    @BindView(R.id.taco_img) ImageView tacoImg;
    @BindView(R.id.reject) Button rejectBtn;
    @BindView(R.id.save) Button saveBtn;
    @BindView(R.id.tags) EditText tagTxt;
    @BindViews({R.id.save, R.id.reject})
    List<Button> actionButtons;
    @BindDrawable(R.drawable.taco) Drawable tacoDrawable;
    @BindDrawable(R.drawable.sad_taco) Drawable sadTacoDrawable;

    private RandomTacoUtil tacoUtil;
    private TacoImageUtil imageUtil;
    private Realm realmInstance;
    private Taco taco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tacoUtil = new RandomTacoUtil();
        imageUtil = new TacoImageUtil();
        Realm.init(this);
        realmInstance = Realm.getDefaultInstance();
        Dart.inject(this);
        getNewTaco();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmInstance.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                startActivity(Henson.with(MainActivity.this).gotoSearchTacosActivity().build());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.taco_img)
    public void clickInfo() {
        goToTacoDetailActivity();
        goToNextActivity();
    }

    private void goToNextActivity() {
        startActivityWithHenson();
        startActivityWithoutHenson();
    }

    private void startActivityWithHenson() {
        Intent intent = Henson.with(this)
                .gotoTacoDetailActivity()
                .id(42)
                .name("Tempeh Taco")
                .url("http://i.imgur.com/vYEodo6.jpg")
                .build();

        startActivity(intent);
    }

    private void startActivityWithoutHenson() {
        Intent intent = new Intent(this, TacoDetailActivity.class)
                .putExtra("TACO_ID", 42)
                .putExtra("TACO_NAME", "Tempeh Taco")
                .putExtra("TACO_IMAGE_URL", "http://i.imgur.com/vYEodo6.jpg");
        startActivity(intent);
    }

    private void goToTacoDetailActivity() {
        Intent intent = Henson.with(this)
                .gotoTacoDetailActivity()
                .id(42)
                .name(taco.getName())
                .url(taco.getUrl())
                .imageUrl(taco.getImageUrl())
                .build();

        startActivity(intent);
    }

    @OnClick({R.id.reject, R.id.save})
    public void saveOrReject(Button button) {
        if (button.getId() == R.id.save) {
            Toast.makeText(this, "Yummy :)", LENGTH_SHORT).show();
            saveTaco();
        } else {
            Toast.makeText(this, "Ew Gross!", LENGTH_SHORT).show();
        }
        getNewTaco();
    }

    private void saveTaco() {
        realmInstance.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(taco);
            }
        });

    }

    static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setEnabled(false);
        }
    };

    static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };


    private void getNewTaco() {
        //load taco description
        tacoUtil.getRandomTaco().enqueue(new Callback<Taco>() {
            @Override
            public void onResponse(Call<Taco> call, Response<Taco> response) {
                taco = response.body();
                description.setText(taco.getName());
                setTacoImage(taco.getName());
            }

            @Override
            public void onFailure(Call<Taco> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error getting taco", Toast.LENGTH_LONG).show();
                Log.e(TAG, "error getting taco", t);
            }
        });
    }

    private void setTacoImage(String name) {
        imageUtil.getTacoImage(name).enqueue(new Callback<AzureResponse>() {
            @Override
            public void onResponse(Call<AzureResponse> call, Response<AzureResponse> response) {
                AzureResponse azureResponse = response.body();
                if (azureResponse.getValue().isEmpty()) {
                    Picasso.with(MainActivity.this).load(R.drawable.taco).fit().centerCrop().placeholder(tacoDrawable).into(tacoImg);
                } else {
                    Uri imgUri = Uri.parse(azureResponse.getValue().get(0).getContentUrl());
                    Picasso.with(MainActivity.this).load(imgUri.getQueryParameter("r")).resize(256, 256).centerCrop().placeholder(tacoDrawable).error(sadTacoDrawable).into(tacoImg);
                    taco.setImageUrl(imgUri.getQueryParameter("r"));
                }
                ButterKnife.apply(actionButtons, ENABLED, true);
            }

            @Override
            public void onFailure(Call<AzureResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error loading image", Toast.LENGTH_LONG).show();
                Log.e(TAG, "error loading image", t);
            }
        });
    }


}
