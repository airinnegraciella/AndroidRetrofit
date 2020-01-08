package com.example.androidretrofit.Retrofit;

import com.example.androidretrofit.Model.Post;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface IMyAPI {
    @GET("posts")
    Single<List<Post>> getPosts();
}
