package com.example.mobileappprogrammingproject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static RetrofitInterface retroInter = null;

//    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String BASE_URL = "http://10.0.2.2:5000";
    public static RetrofitInterface getRetroInterface(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        retroInter = retrofit.create(RetrofitInterface.class);
        return retroInter;
    }

    public interface RetrofitInterface{
        @POST("/api/v1/account/login")
        @FormUrlEncoded
        Call<String> getAuthToken(@Field("SDT") String phoneNum, @Field("Password") String password);

//        @POST("/api/v1/account/login")
//        Call<String> getAuthToken(@Body JsonObject jsonObject);

        @GET("/posts/1")
        Call<String> getPost();

        @FormUrlEncoded
        @POST("/api/v1/transaction/get-all")
        Call<String> getAllTrans(@Field("token") String authToken);

        @FormUrlEncoded
        @POST("/api/v1/bank/getCardBankByUser")
        Call<String> getCardBankByUser(@Field("token") String authToken);

        @FormUrlEncoded
        @POST("/api/v1/bank/sendmoney")
        Call<String> withdrawMoneyTrans(@Field("token") String authToken, @Field("MATK") String bankAcc, @Field("MONEY") String amount);

        @FormUrlEncoded
        @POST("/api/v1/services/getAllServices")
        Call<String> getAllBill(@Field("token") String authToken);

        @FormUrlEncoded
        @POST("/api/v1/services/payService")
        Call<String> payBill(@Field("token") String authToken, @Field("idDV") String billId);

        @FormUrlEncoded
        @POST("/api/v1/user/getUserByToken")
        Call<String> getUserName(@Field("token") String authToken);

        @FormUrlEncoded
        @POST("/api/v1/account/get-user-balance")
        Call<String> getBalanceByUser(@Field("token") String authToken);

    }
    public interface MyCallback {
        void execution();
    }
}
