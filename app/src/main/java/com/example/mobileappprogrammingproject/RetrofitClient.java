package com.example.mobileappprogrammingproject;

import com.example.mobileappprogrammingproject.APIResult.BasicResult;
import com.example.mobileappprogrammingproject.APIResult.ForgetpasswordResult;
import com.example.mobileappprogrammingproject.APIResult.GetAllBanksResult;
import com.example.mobileappprogrammingproject.APIResult.GetAllUserTransferedResult;
import com.example.mobileappprogrammingproject.APIResult.GetCardBankbyUserResult;
import com.example.mobileappprogrammingproject.APIResult.GetMoneyResult;
import com.example.mobileappprogrammingproject.APIResult.GetUserbyTokenResult;
import com.example.mobileappprogrammingproject.APIResult.LinkBankResult;
import com.example.mobileappprogrammingproject.APIResult.LoginResult;
import com.example.mobileappprogrammingproject.APIResult.SignupResult;
import com.example.mobileappprogrammingproject.APIResult.TransferResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static RetrofitInterface retroInter = null;

//    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String BASE_URL = "http://10.0.2.2:5000";
    public static RetrofitInterface getRetroInterface(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroInter = retrofit.create(RetrofitInterface.class);
        return retroInter;
    }

    public interface RetrofitInterface{
        @FormUrlEncoded
        @POST("/api/v1/account/login")
        Call<String> getAuthToken(@Field("SDT") String phoneNum, @Field("Password") String password);

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

        @POST("/api/v1/account/login")
        Call<LoginResult> executeLogin(@Body HashMap<String, String> map);
        @POST("/api/v1/account/sign-up")
        Call<SignupResult> executeSignup(@Body HashMap<String, String> map);
        @POST("/api/v1/user/getUserByToken")
        Call<GetUserbyTokenResult> executeGetUser(@Body HashMap<String, String> map);
        @POST("/api/v1/bank/getCardBankByUser")
        Call<GetCardBankbyUserResult> executeGetCardBank(@Body HashMap<String, String> map);
        @POST("/api/v1/bank/getmoney")
        Call<GetMoneyResult> executeGetMoneyResult(@Body HashMap<String, String> map);
        @POST("/api/v1/account/forgetpassword")
        Call<ForgetpasswordResult> executeForgetpassword(@Body HashMap<String, String> map);
        @POST("api/v1/transaction/get-all-user-transfered")
        Call<GetAllUserTransferedResult> executeGetAllUserTransferedResult(@Body HashMap<String, String> map);
        @POST("api/v1/transaction/transfer")
        Call<TransferResult> executeTransferResult(@Body HashMap<String, String> map);
        @POST("api/v1/bank/link")
        Call<LinkBankResult> executeLinkBankResult(@Body HashMap<String, String> map);
        @GET("api/v1/bank/getAllBanks")
        Call<GetAllBanksResult> executeGetAllBanksResult();
        @PUT("/api/v1/user/update")
        Call<BasicResult> excuteUpdateUser(@Body HashMap<String, String> map);
    }
    public interface MyCallback {
        void execution();
    }
}
