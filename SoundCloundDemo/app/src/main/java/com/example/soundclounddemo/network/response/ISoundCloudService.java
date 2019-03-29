package com.example.soundclounddemo.network.response;

import com.example.soundclounddemo.network.response.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ISoundCloudService {
    @GET("tracks")
    Call<List<SearchResponse>> getSearchResponse(
            @Query("q") String q,
            @Query("client_id") String clientId
    );

    @GET("tracks")
    Call<SearchPaginationResponse> getSearchPaginationResponse(
            @Query("q") String q,
            @Query("client_id") String clientId,
            @Query("linked_partitioning") int linkedPartitioning,
            @Query("limit") int limit
    );
    @GET
    Call<SearchPaginationResponse> getSearchPaginationNextHrefResponse(@Url String url);
}
