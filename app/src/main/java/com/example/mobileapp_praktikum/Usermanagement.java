package com.example.mobileapp_praktikum;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.concurrent.ExecutionException;


class Usermanagement {
    private static final String TAG = Usermanagement.class.getSimpleName();
    private static Usermanagement instance;


    private String securityToken = "";
    private String userID = "";

    //--Network Strings---------------------
    private final String API_URI = "https://treibhaus.informatik.rwth-aachen.de/praktikum-ss19/api";

    private final String KEY_EMAIL = "email";
    private final String KEY_PASSWORD = "password";
    private final String KEY_AUTHORIZATION = "Authorization:";

    //--
    private final String KEY_TOKEN = "token";
    private final String KEY_USER_ID = "id";
    private final String KEY_SHAREDPREFERENCE = "securityTokenPreference";
    private final String KEY_SHAREDPREFERENCE_SECURITYTOKEN = "securityToken";

    //--
    static final int OPERATION_SUCCESSFUL = 0;
    static final int OPERATION_FAILED = 1;
    static final int NO_INTERNET_CONNECTION = 2;
    static final int COULDNT_REACH_SERVER = 3;

    private Usermanagement() {

    }

    /**
     * Checks if there currently is a logged in user.
     * @param context Context of the current activity.
     * @return true - if a user is logged in.
     */
    boolean isLoggedIn(Context context) {
        return !getSecurityToken().equals("");
    }

    /**
     *
     * @return Singleton instance of the Usermanagement
     */
    static Usermanagement getInstance() {
        if(instance == null) {
            instance = new Usermanagement();
        }
        return instance;
    }

    /**
     * Returns the security token of the logged in user.
     * @return Security token used to make secured API requests.
     */
    String getSecurityToken() {
        /*if(securityToken.equals("")) {
            SharedPreferences pref = context.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
            pref.getString(KEY_SHAREDPREFERENCE_SECURITYTOKEN, "");
        }*/
        return this.securityToken;
    }

    private void setSecurityToken( String token, Context context) {
        securityToken = token;
        /*SharedPreferences pref = context.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_SHAREDPREFERENCE_SECURITYTOKEN, token);
        editor.apply();*/
    }

    private void setUserID( String id, Context context) {
        userID = id;
        /*SharedPreferences pref = context.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_SHAREDPREFERENCE_SECURITYTOKEN, token);
        editor.apply();*/
    }


    /**
     *
     * @return ID of the currently logged in user.
     *         String.valueOf(OPERATION_FAILED) if sth went wrong
     */
    String getUserID( Context context) {
        if(this.userID.equals("")) {
            if(isLoggedIn(context)) {
                /*Future<Response<JsonObject>> future = Ion.with(context)
                        .load(API_URI+"/users/me")
                        .setHeader(KEY_AUTHORIZATION, getSecurityToken(context))
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
                }*/



                Response<JsonObject> response = null;
                try {
                    response = Ion.with(context)
                            .load(API_URI + "/users/me")
                            .setLogging("LoginLog", Log.VERBOSE)
                            .setHeader(KEY_AUTHORIZATION, getSecurityToken())
                            .asJsonObject()
                            .withResponse()
                            .setCallback(new FutureCallback<Response<JsonObject>>() {
                                @Override
                                public void onCompleted(Exception e, Response<JsonObject> result) {
                                    if(e != null) {
                                        Log.e(TAG,"Error = " + e.toString());
                                    }
                                    if(result != null) {
                                        Log.w(TAG, "Code = " + String.valueOf(result.getHeaders().code()));
                                    }
                                }
                            }).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if((response != null) && (response.getHeaders().code() == 200 && (response.getResult() != null))) {
                    Log.w(TAG,"UserID = " + response.getResult().get(KEY_USER_ID).getAsString());
                    setUserID(response.getResult().get(KEY_USER_ID).getAsString(),context);
                }
                else {
                    return String.valueOf(OPERATION_FAILED);
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
    int login(String email, String password, Context context) {
        JsonObject body = new JsonObject();
        body.addProperty(KEY_EMAIL, email);
        body.addProperty(KEY_PASSWORD, password);
        /*Future<Response<JsonObject>> future = Ion.with(context)
                .load(API_URI+"/token")
                .setLogging("LoginLog", Log.VERBOSE)
                .setJsonObjectBody(body)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        Log.w(TAG,String.valueOf(result.getHeaders().code()));
                        Log.w(TAG, result.getResult().get(KEY_TOKEN).getAsString());
                    }
                });
        while (future.tryGet() == null) {

        }
        if(future.tryGet().getHeaders().code() == 200) {
            Log.w(TAG,"future ist nicht null");
            String token = future.tryGet().getResult().get(KEY_TOKEN).getAsString();
            this.setSecurityToken(token, context);
            return true;
        }
        else {
            return false;
        }*/
        Response<JsonObject> response = null;
        try {
            response = Ion.with(context)
                    .load(API_URI + "/token")
                    .setLogging("LoginLog", Log.VERBOSE)
                    .setJsonObjectBody(body)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if(e != null) {
                                Log.e(TAG,"Error = " + e.toString());
                            }
                            if(result != null) {
                                Log.w(TAG, "Code = " + String.valueOf(result.getHeaders().code()));
                            }
                        }
                    }).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(response==null){
            Log.w(TAG, "Response == null");
            return NO_INTERNET_CONNECTION;
        }
        else if(response.getResult()==null) {
            Log.w(TAG,"json = null");
            return COULDNT_REACH_SERVER;
        }
        else if(response.getHeaders().code() == 404) {
            Log.w(TAG, "COULNDT_REACH_SERVER");
            return COULDNT_REACH_SERVER;
        }
        else if((response != null) && (response.getHeaders().code() == 200)) {
            Log.w(TAG,"Token = " + response.getResult().get(KEY_TOKEN).getAsString());
            setSecurityToken(response.getResult().get(KEY_TOKEN).getAsString(),context);
            return OPERATION_SUCCESSFUL;
        }
        else {
            Log.w(TAG, "OPERATION_FAILED");
            return OPERATION_FAILED;
        }

    }
        /**
     *
     */
    int register(String email, String password, Context context) {
        Log.w(TAG,"register started");
        JsonObject body = new JsonObject();
        body.addProperty(KEY_EMAIL, email);
        body.addProperty(KEY_PASSWORD, password);
        /*Future<Response<JsonObject>> future = Ion.with(context)
                .load(API_URI+"/users")
                .setLogging("LoginLog", Log.VERBOSE)
                .setJsonObjectBody(body)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        Log.w(TAG,String.valueOf(result.getHeaders().code()));
                    }
                });
        Log.w(TAG, "register request send to "+API_URI+"/users");
        while (future.tryGet()==null) {}
        return future.tryGet().getHeaders().code() == 201;*/
        Response<JsonObject> response = null;
        try {
            response = Ion.with(context)
                    .load(API_URI+"/users")
                    .setLogging("RegisterLog", Log.VERBOSE)
                    .setJsonObjectBody(body)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if(e != null) {
                                Log.e(TAG,"Error = " + e.toString());
                            }
                            if(result != null) {
                                Log.w(TAG, "Code = " + String.valueOf(result.getHeaders().code()));
                            }
                        }
                    }).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(response==null){
            Log.w(TAG, "Response == null");
            return NO_INTERNET_CONNECTION;
        }
        else if(response.getResult()==null) {
            Log.w(TAG,"json = null");
            return COULDNT_REACH_SERVER;
        }
        else if(response.getHeaders().code() == 404) {
            Log.w(TAG, "COULNDT_REACH_SERVER");
            return COULDNT_REACH_SERVER;
        }
        else if((response != null) && (response.getHeaders().code() == 201)) {
            Log.w(TAG,"OPERATION_SUCCESSFUL" );
            return OPERATION_SUCCESSFUL;
        }
        else {
            Log.w(TAG, "OPERATION_FAILED");
            return OPERATION_FAILED;
        }
    }

    /**
     * Sends a request to the server to reset the password.
     * @param email user email
     * @return true if successful
     */
    int resetPassword(String email, Context context) {
        Log.w(TAG,"Reset Password started");
        JsonObject body = new JsonObject();
        body.addProperty(KEY_EMAIL, email);
        /*Future<Response<JsonObject>> future = Ion.with(context)
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
        return future.tryGet().getHeaders().code() == 200;*/
        Response<JsonObject> response = null;
        try {
            response = Ion.with(context)
                    .load(API_URI+"/password-reset")
                    .setLogging("ResetPwLog", Log.VERBOSE)
                    .setJsonObjectBody(body)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            if(e != null) {
                                Log.e(TAG,"Error = " + e.toString());
                            }
                            if(result != null) {
                                Log.w(TAG, "Code = " + String.valueOf(result.getHeaders().code()));
                            }
                        }
                    }).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(response==null){
            Log.w(TAG, "Response == null");
            return NO_INTERNET_CONNECTION;
        }
        else if(response.getHeaders().code() == 404) {
            Log.w(TAG, "COULNDT_REACH_SERVER");
            return COULDNT_REACH_SERVER;
        }
        else if((response != null) && (response.getHeaders().code() == 200)) {
            Log.w(TAG,"OPERATION_SUCCESSFUL" );
            return OPERATION_SUCCESSFUL;
        }
        else {
            Log.w(TAG, "OPERATION_FAILED");
            return OPERATION_FAILED;
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

