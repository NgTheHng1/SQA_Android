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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitInterface {
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
