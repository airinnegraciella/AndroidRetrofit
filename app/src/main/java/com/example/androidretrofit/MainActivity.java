package com.example.androidretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.androidretrofit.Adapter.PostAdapter;
import com.example.androidretrofit.Model.Post;
import com.example.androidretrofit.Retrofit.IMyAPI;
import com.example.androidretrofit.Retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    IMyAPI myAPI;
    RecyclerView recycler_posts;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(IMyAPI.class);

        //View
        recycler_posts = (RecyclerView)findViewById(R.id.recycler_posts);
        recycler_posts.setHasFixedSize(true);
        recycler_posts.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    private void fetchData() {
        myAPI.getPosts()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<List<Post>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);

            }

            @Override
            public void onSuccess(List<Post> posts) {
                displayData(posts);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayData(List<Post> posts) {
        PostAdapter adapter = new PostAdapter(this, posts);
        recycler_posts.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
