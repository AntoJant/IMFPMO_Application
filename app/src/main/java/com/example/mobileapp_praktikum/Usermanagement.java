package com.example.mobileapp_praktikum;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;


class Usermanagement {
    private static final String TAG = Usermanagement.class.getSimpleName();
    private static Usermanagement instance;
    private final Context APPLICATION_CONTEXT;

    private String securityToken = "";
    private String userID = "";

    //--Network Strings---------------------
    private final String API_URI = "https://treibhaus.informatik.rwth-aachen.de/praktikum-ss19/api";

    private final String KEY_EMAIL = "email";
    private final String KEY_PASSWORD = "password";
    private final String KEY_AUTHORIZATION = "Authorization";

    //--
    private final String KEY_TOKEN = "token";
    private final String KEY_USER_ID = "id";
    private final String KEY_SHAREDPREFERENCE = "securityTokenPreference";
    private final String KEY_SHAREDPREFERENCE_SECURITYTOKEN = "securityToken";
    private final String KEY_SHAREDPREFERENCE_USER_ID = "userID";

    //--
    static final int OPERATION_SUCCESSFUL = 0;
    static final int OPERATION_FAILED = 1;
    static final int NO_INTERNET_CONNECTION = 2;
    static final int COULDNT_REACH_SERVER = 3;

    private Usermanagement(Context context) {
        APPLICATION_CONTEXT = context;
        SharedPreferences pref = APPLICATION_CONTEXT.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
        setSecurityToken(pref.getString(KEY_SHAREDPREFERENCE_SECURITYTOKEN, ""));
        Log.w(TAG, "Loaded Securitytoken = " + securityToken);
        setUserID(pref.getString(KEY_SHAREDPREFERENCE_USER_ID, ""));
        Log.w(TAG, "Loaded UserID = " + userID);
    }

    /**
     * Checks if there currently is a logged in user.
     * @return true - if a user is logged in.
     */
    boolean isLoggedIn() {
        return !getSecurityToken().equals("");
    }

    static void createInstance(Context context) {
        if(instance == null) {
            instance = new Usermanagement(context);
        }
    }

    /**
     *
     * @return Singleton instance of the Usermanagement
     */
    static Usermanagement getInstance() {
        if(instance == null) Log.w(TAG, "Instance is null");
        return instance;
    }

    /**
     * Returns the security token of the logged in user.
     * @return Security token used to make secured API requests.
     */
    String getSecurityToken() {
        return this.securityToken;
    }

    private void setSecurityToken( String token) {
        securityToken = token;
        SharedPreferences pref = APPLICATION_CONTEXT.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_SHAREDPREFERENCE_SECURITYTOKEN, token);
        editor.apply();
        Log.w(TAG,"Set Securitytoken to " + token);
    }

    private void setUserID( String id) {
        userID = id;
        SharedPreferences pref = APPLICATION_CONTEXT.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_SHAREDPREFERENCE_USER_ID, id);
        editor.apply();
        Log.w(TAG, "Set UserID to " + id);
    }


    /**
     *
     * Fetches the UserID of the currently logged in User and saves it
     * @return OPERATION_SUCCESSFUL if successful else 1-3
     */
    private int fetchUserID( Context context) {
            if(isLoggedIn()) {
                Response<JsonObject> response = null;
                try {
                    response = Ion.with(context)
                            .load(API_URI + "/users/me")
                            .setLogging("LoginLog", Log.VERBOSE)
                            .setHeader(KEY_AUTHORIZATION, "Bearer " + getSecurityToken())
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
                if((response != null) && (response.getHeaders().code() == 200 && (response.getResult() != null))) {
                    Log.w(TAG,"UserID = " + response.getResult().get(KEY_USER_ID).getAsString());
                    setUserID(response.getResult().get(KEY_USER_ID).getAsString());
                    return OPERATION_SUCCESSFUL;
                }
                else {
                    Log.w(TAG, "OPERATION_FAILED");
                    return OPERATION_FAILED;
                }
            }
            else {
                Log.w(TAG, "User isn't logged in");
                return OPERATION_FAILED;
            }
    }

    /**
     * @return ID of the currently logged in user.
     *         String.valueOf(OPERATION_FAILED) if sth went wrong
     */
    String getUserID () {
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
            Log.w(TAG, "Token = " + response.getResult().get(KEY_TOKEN).getAsString());
            setSecurityToken(response.getResult().get(KEY_TOKEN).getAsString());
            int gotUserID = fetchUserID(context);
            if (gotUserID == OPERATION_SUCCESSFUL) {
                return OPERATION_SUCCESSFUL;
            }
            else {
                setSecurityToken("");
                return gotUserID;
            }
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
        setSecurityToken( "");
        setUserID("");
        return true;
    }

    JsonObject getAnalyseErgebnisse(Context context) {
        if (isLoggedIn()) {
            Response<JsonObject> response = null;
            try {
                response = Ion.with(context)
                        .load(API_URI + "/users/" + userID + "/analysis-results")
                        .setLogging("AnalyseLog", Log.VERBOSE)
                        .setHeader(KEY_AUTHORIZATION, "Bearer " + getSecurityToken())
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {
                                if (e != null) {
                                    Log.e(TAG, "Error = " + e.toString());
                                }
                                if (result != null) {
                                    Log.w(TAG, "Code = " + String.valueOf(result.getHeaders().code()));
                                }
                            }
                        }).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (response == null) {
                Log.w(TAG, "Response == null");
                return null;
            } else if (response.getResult() == null) {
                Log.w(TAG, "json = null");
                return null;
            } else if (response.getHeaders().code() == 404) {
                Log.w(TAG, "COULNDT_REACH_SERVER");
                return null;
            }
            if ((response != null) && (response.getHeaders().code() == 200 && (response.getResult() != null))) {

                return response.getResult();
            } else {
                Log.w(TAG, "OPERATION_FAILED");
                return null;
            }
        } else {
            Log.w(TAG, "User isn't logged in");
            return null;
        }
    }

    JsonObject getAnalyseErgebnis(Context context, int analyseId){
        if (isLoggedIn()) {
            Response<JsonObject> response = null;
            try {
                response = Ion.with(context)
                        .load(API_URI + "/users/" + userID + "/analysis-results/" + analyseId)
                        .setLogging("AnalyseLog", Log.VERBOSE)
                        .setHeader(KEY_AUTHORIZATION, "Bearer " + getSecurityToken())
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {
                                if (e != null) {
                                    Log.e(TAG, "Error = " + e.toString());
                                }
                                if (result != null) {
                                    Log.w(TAG, "Code = " + String.valueOf(result.getHeaders().code()));
                                }
                            }
                        }).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (response == null) {
                Log.w(TAG, "Response == null");
                return null;
            } else if (response.getResult() == null) {
                Log.w(TAG, "json = null");
                return null;
            } else if (response.getHeaders().code() == 404) {
                Log.w(TAG, "COULNDT_REACH_SERVER");
                return null;
            }
            if ((response != null) && (response.getHeaders().code() == 200 && (response.getResult() != null))) {

                return response.getResult();
            } else {
                Log.w(TAG, "OPERATION_FAILED");
                return null;
            }
        } else {
            Log.w(TAG, "User isn't logged in");
            return null;
        }
    }

    JsonObject getAnalyseErgebnisseTag(Context context, Calendar tag) {
        if (isLoggedIn()) {
            Response<JsonObject> response = null;
            String tagS ="";
            tagS = tagS.concat(tag.get(Calendar.YEAR) + "-");
            int monat = tag.get(Calendar.MONTH)+1;
            if (monat < 10){
                tagS = tagS.concat("0" + monat +"-");
            }else {
                tagS =tagS.concat(monat+"-");
            }
            if(tag.get(Calendar.DAY_OF_MONTH) < 10){
                tagS = tagS.concat("0" + tag.get(Calendar.DAY_OF_MONTH));
            }else{
                tagS = tagS.concat(""+ tag.get(Calendar.DAY_OF_MONTH));
            }

            try {
                response = Ion.with(context)
                        .load(API_URI + "/users/" + userID + "/paths?date="+tagS)
                        .setLogging("AnalyseLog", Log.VERBOSE)
                        .setHeader(KEY_AUTHORIZATION, "Bearer " + getSecurityToken())
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {
                                if (e != null) {
                                    Log.e(TAG, "Error = " + e.toString());
                                }
                                if (result != null) {
                                    Log.w(TAG, "Code = " + String.valueOf(result.getHeaders().code()));
                                }
                            }
                        }).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (response == null) {
                Log.w(TAG, "Response == null");
                return null;
            } else if (response.getResult() == null) {
                Log.w(TAG, "json = null");
                return null;
            } else if (response.getHeaders().code() == 404) {
                Log.w(TAG, "COULNDT_REACH_SERVER");
                return null;
            }
            if ((response != null) && (response.getHeaders().code() == 200 && (response.getResult() != null))) {

                return response.getResult();
            } else {
                Log.w(TAG, "OPERATION_FAILED");
                return null;
            }
        } else {
            Log.w(TAG, "User isn't logged in");
            return null;
        }
    }

    JsonObject getAnalyseErgebnisWeg(Context context, String wegId){
        if (isLoggedIn()) {
            Response<JsonObject> response = null;
            try {
                response = Ion.with(context)
                        .load(API_URI + "/users/" + userID + "/paths/" + wegId)
                        .setLogging("AnalyseLog", Log.VERBOSE)
                        .setHeader(KEY_AUTHORIZATION, "Bearer " + getSecurityToken())
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {
                                if (e != null) {
                                    Log.e(TAG, "Error = " + e.toString());
                                }
                                if (result != null) {
                                    Log.w(TAG, "Code = " + String.valueOf(result.getHeaders().code()));
                                }
                            }
                        }).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (response == null) {
                Log.w(TAG, "Response == null");
                return null;
            } else if (response.getResult() == null) {
                Log.w(TAG, "json = null");
                return null;
            } else if (response.getHeaders().code() == 404) {
                Log.w(TAG, "COULNDT_REACH_SERVER");
                return null;
            }
            if ((response != null) && (response.getHeaders().code() == 200 && (response.getResult() != null))) {

                return response.getResult();
            } else {
                Log.w(TAG, "OPERATION_FAILED");
                return null;
            }
        } else {
            Log.w(TAG, "User isn't logged in");
            return null;
        }
    }
}

