package com.trimitrasis.finosapps.Connection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * Created by rahman on 28/07/2017.
 */

public interface RetrofitInterfaceDownload {

    @GET("/item/zipImage/{id}")
    @Streaming
    Call<ResponseBody> downloadFile(@Path("id") String id);

}
