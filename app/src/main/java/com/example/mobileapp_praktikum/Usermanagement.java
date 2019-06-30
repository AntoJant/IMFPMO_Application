package com.example.mobileapp_praktikum;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;


public class Usermanagement {
    private static final String TAG = LocationUpdatesService.class.getSimpleName();
    private static Usermanagement instance;


    private String securityToken = "";
    private String userID = "";

    //--Network Strings---------------------
    private final String API_URI = "";

    private final String KEY_EMAIL = "email";
    private final String KEY_PASSWORD = "password";
    private final String KEY_AUTHORIZATION = "Authorization:";

    //--
    private final String KEY_TOKEN = "token";
    private final String KEY_USER_ID = "id";
    private final String KEY_SHAREDPREFERENCE = "securityTokenPreference";
    private final String KEY_SHAREDPREFERENCE_SECURITYTOKEN = "securityToken";


    private Usermanagement() {

    }

    /**
     * Checks if there currently is a logged in user.
     * @param context Context of the current activity.
     * @return true - if a user is logged in.
     */
    public boolean isLoggedIn (Context context) {
        return !getSecurityToken(context).equals("");
    }

    /**
     *
     * @return Singleton instance of the Usermanagement
     */
    public static Usermanagement getInstance() {
        if(instance == null) {
            instance = new Usermanagement();
        }
        return instance;
    }

    /**
     * Returns the security token of the logged in user.
     * @param context Context of the current activity.
     * @return Security token used to make secured API requests.
     */
    public String getSecurityToken(Context context) {
        if(securityToken.equals("")) {
            SharedPreferences pref = context.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
            pref.getString(KEY_SHAREDPREFERENCE_SECURITYTOKEN, "");
        }
        return this.securityToken;
    }

    private void setSecurityToken( String token, Context context) {
        securityToken = token;
        SharedPreferences pref = context.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_SHAREDPREFERENCE_SECURITYTOKEN, token);
        editor.apply();
    }


    /**
     *
     * @return ID of the currently logged in user.
     */
    public String getUserID( Context context) {
        if(this.userID.equals("")) {
            if(isLoggedIn(context)) {
                Future<Response<JsonObject>> future = Ion.with(context)
                        .load(API_URI+"/token")
                        .setHeader(KEY_AUTHORIZATION, securityToken)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {
                                Log.w(TAG,String.valueOf(result.getHeaders().code()));
                            }
                        });
                if(future.tryGet().getHeaders().code() == 200) {
                    this.userID = future.tryGet().getResult().get(KEY_USER_ID).getAsString();
                }
            }
        }
        return this.userID;
    }

    /**
     * Requests a new security token for the specified user, which you can get using the 'getSecurityToken()' function.
     * @param email E-mail addess of the user that should be logged in.
     * @param password Password of the user that should be logged in.
     * @return true - if the login was successful.
     */
    boolean login(String email, String password, Context context) {
        JsonObject body = new JsonObject();
        body.addProperty(KEY_EMAIL, email);
        body.addProperty(KEY_PASSWORD, password);
        Future<Response<JsonObject>> future = Ion.with(context)
                .load(API_URI+"/token")
                .setJsonObjectBody(body)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        Log.w(TAG,String.valueOf(result.getHeaders().code()));
                    }
                });
        if(future.tryGet().getHeaders().code() == 200) {
            String token = future.tryGet().getResult().get(KEY_TOKEN).getAsString();
            this.setSecurityToken(token, context);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     */
    boolean register(String email, String password, Context context) {
        JsonObject body = new JsonObject();
        body.addProperty(KEY_EMAIL, email);
        body.addProperty(KEY_PASSWORD, password);
        Future<Response<JsonObject>> future = Ion.with(context)
                .load(API_URI+"/users")
                .setJsonObjectBody(body)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        Log.w(TAG,String.valueOf(result.getHeaders().code()));
                    }
                });
        if(future.tryGet().getHeaders().code() == 201) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Sends a request to the server to reset the password.
     * @param email
     * @return true if successful
     */
    boolean resetPassword(String email, Context context) {
        JsonObject body = new JsonObject();
        body.addProperty(KEY_EMAIL, email);
        Future<Response<JsonObject>> future = Ion.with(context)
                .load(API_URI+"/password-reset")
                .setJsonObjectBody(body)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        Log.w(TAG,String.valueOf(result.getHeaders().code()));
                    }
                });
        if(future.tryGet().getHeaders().code() == 200) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Deletes the saved security token.
     * @return true if successful
     */
    boolean logout( Context context) {
        setSecurityToken( "", context);
        return true;
    }
}

