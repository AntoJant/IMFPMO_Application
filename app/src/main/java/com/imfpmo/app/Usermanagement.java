package com.imfpmo.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.concurrent.ExecutionException;


class Usermanagement {
    private static final String TAG = Usermanagement.class.getSimpleName();
    private static Usermanagement instance;
    private final Context APPLICATION_CONTEXT;

    private String securityToken = "";
    private String userID = "";

    //--API Strings---------------------
    private final String API_URI = "https://treibhaus.informatik.rwth-aachen.de/praktikum-ss19/api";

    private final String KEY_EMAIL = "email";
    private final String KEY_MODE = "mode";
    private final String KEY_PASSWORD = "password";
    private final String KEY_AUTHORIZATION = "Authorization";
    private final String KEY_XCONFPASS = "X-Conf-Pass";

    //--SharedPreferences Strings-------
    private final String KEY_TOKEN = "token";
    private final String KEY_USER_ID = "id";
    private final String KEY_USER_ACTIVE = "status";
    private final String KEY_SHAREDPREFERENCE = "securityTokenPreference";
    private final String KEY_SHAREDPREFERENCE_SECURITYTOKEN = "securityToken";
    private final String KEY_SHAREDPREFERENCE_USER_ID = "userID";

    //--Returns-------------------------
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

    /**
     * Creates a new Usermanagement instance. Needs to be called once before getInstance().
     * @param context The context needs to be the applicationcontext.
     */
    static void createInstance(Context context) {
        if(instance == null) {
            instance = new Usermanagement(context);
        }
    }

    /**
     * Returns the instance of Usermanagement. createInstance() needs to be called before first using
     * this.
     * @return Singleton instance of the Usermanagement, null if createInstance() was not called
     * before.
     */
    static Usermanagement getInstance() {
        if(instance == null) Log.w(TAG, "Instance is null");
        return instance;
    }

    /**
     * Returns the security token of the logged in user.
     * @return Security token used to make secured API requests, "" if no user is logged in.
     */
    String getSecurityToken() {
        return this.securityToken;
    }

    /**
     * Set a new securityToken and save it in a sharedPreference.
     * @param token new securityToken
     */
    private void setSecurityToken( String token) {
        securityToken = token;
        SharedPreferences pref = APPLICATION_CONTEXT.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_SHAREDPREFERENCE_SECURITYTOKEN, token);
        editor.apply();
        Log.w(TAG,"Set Securitytoken to " + token);
    }

    /**
     * Set a new userID and save it in a sharedPreference.
     * @param id new UserID
     */
    private void setUserID( String id) {
        userID = id;
        SharedPreferences pref = APPLICATION_CONTEXT.getSharedPreferences(KEY_SHAREDPREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_SHAREDPREFERENCE_USER_ID, id);
        editor.apply();
        Log.w(TAG, "Set UserID to " + id);
    }


    /**
     * Fetches the UserID of the currently logged in User from the API and saves it.
     * @return OPERATION_SUCCESSFUL if successful else 1:operation failed, 2: no internet connection
     * or 3:could not reach server, 10: E-Mail isn't verified yet.
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
                                        Log.w(TAG, "Code = " + result.getHeaders().code());
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
                if(response.getHeaders().code() == 200 && !response.getResult().get(KEY_USER_ACTIVE).getAsString().equals("active")) {
                    Log.w(TAG, "USER NOT ACTIVE");
                    Log.w(TAG, "Status = " + response.getResult().get(KEY_USER_ACTIVE).getAsString());
                    return 10;
                }
                if(response.getHeaders().code() == 200 && response.getResult() != null) {
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
     * Requests a new security token for the specified user, which you can get using the
     * 'getSecurityToken()' function.
     * @param email E-mail addess of the user that should be logged in.
     * @param password Password of the user that should be logged in.
     * @param context context of the current activity.
     * @return 0:OPERATION_SUCCESSFUL if successful else 1:operation failed, 2: no internet connection
     * or 3:could not reach server, 10: E-Mail isn't verified yet.
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
                                Log.w(TAG, "Code = " + result.getHeaders().code());
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
        else if(response.getHeaders().code() == 200) {
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
     * Creates a new user with specified credentials.
     * @param email Email address of the new user.
     * @param password Password of the new user.
     * @param context context of the current activity.
     * @return 0:OPERATION_SUCCESSFUL if successful else 1:operation failed, 2: no internet connection
     * or 3:could not reach server.
     */
    int register(String email, String password, Context context) {
        Log.w(TAG,"register started");
        JsonObject body = new JsonObject();
        body.addProperty(KEY_EMAIL, email);
        body.addProperty(KEY_PASSWORD, password);

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
                                Log.w(TAG, "Code = " + result.getHeaders().code());
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
        else if(response.getHeaders().code() == 201) {
            Log.w(TAG,"OPERATION_SUCCESSFUL" );
            return OPERATION_SUCCESSFUL;
        }
        else {
            Log.w(TAG, "OPERATION_FAILED");
            return OPERATION_FAILED;
        }
    }

    int makeTestUser(String email, String password, Context context) {
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
                    .load(API_URI+"/test-data-user")
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
                                Log.w(TAG, "Code = " + result.getHeaders().code());
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
        else if(response.getHeaders().code() == 201) {
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
     * @return 0:OPERATION_SUCCESSFUL if successful else 1:operation failed, 2: no internet connection
     * or 3:could not reach server.
     */
    int resetPassword(String email, Context context) {
        Log.w(TAG,"Reset Password started");
        JsonObject body = new JsonObject();
        body.addProperty(KEY_EMAIL, email);

        Response<JsonObject> response = null;
        try {
            response = Ion.with(context)
                    .load(API_URI+"/password-reset")
                    .setLogging("ResetPwLog", Log.VERBOSE)
                    .setHeader(KEY_AUTHORIZATION, "Bearer " + getSecurityToken())
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
                                Log.w(TAG, "Code = " + result.getHeaders().code());
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
        else if(response.getHeaders().code() == 200) {
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
     */
    void logout() {

        setSecurityToken( "");
        setUserID("");
        AnalysisLoader.getInstance().resetInstance();

    }

    /**
     * Deletes the all data of the currently logged in user from the database and returns calls
     * logout().
     * @param password password of the currently logged in user
     * @param context context of the current activity
     * @return 0:OPERATION_SUCCESSFUL if successful else 1:operation failed, 2: no internet connection,
     * 3:could not reach server or 10:wrong password.
     */
    int deleteUser(String password, Context context){
        Log.w(TAG,"deleteUser started");

        Response<JsonObject> response = null;
        try {
            response = Ion.with(context)
                    .load("DELETE", API_URI+"/users/" + getUserID())
                    .setLogging("DeleteUserLog", Log.VERBOSE)
                    .setHeader(KEY_XCONFPASS, password)
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
                                Log.w(TAG, "Code = " + result.getHeaders().code());
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
        else if(response.getHeaders().code() == 200) {
            Log.w(TAG,"OPERATION_SUCCESSFUL" );
            logout();
            return OPERATION_SUCCESSFUL;
        }
        else if(response.getHeaders().code() == 403) {
            Log.w(TAG, "WRONG_PASSWORD");
            return 10;
        }
        else {
            Log.w(TAG, "OPERATION_FAILED");
            return OPERATION_FAILED;
        }
    }

    JsonObject getAnalyseErgebnisse(Context context,int skip,int limit) {
        if (isLoggedIn()) {
            Response<JsonObject> response = null;
            try {
                response = Ion.with(context)
                        .load(API_URI + "/users/" + userID + "/analysis-results?skip="+skip+"&limit="+limit)
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
                                    Log.w(TAG, "Code = " + result.getHeaders().code());
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
            if (response.getHeaders().code() == 200 && response.getResult() != null) {

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


    public JsonObject getAnalyseWegeMonat(Context context,int monat, int jahr,int skip){
        if (isLoggedIn()) {
            Response<JsonObject> response = null;
            String monatS = Integer.toString(jahr);
            if(monat < 10){
                monatS = "0" + monat;
            }
            try {
                response = Ion.with(context)
                        .load(API_URI + "/users/" + userID + "/paths?year="+ jahr +"&month="+monatS +"&limit="+10000)
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
                                    Log.w(TAG, "Code = " + result.getHeaders().code());
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
            if (response.getHeaders().code() == 200 && response.getResult() != null) {

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

    public JsonObject changeRide(String analysisId, Ride ride){
        JsonObject body = new JsonObject();
        JsonObject start = new JsonObject();
        start.addProperty("timestamp", ride.start.timestamp);
        start.addProperty("name" , ride.start.name);

        JsonObject end = new JsonObject();
        end.addProperty("name", ride.start.name);

        return null;
    }
    public void patchRide(Context context, String rideId, String startAddress, String endAddress, String startTimestamp, String mode) {
        JsonObject body = new JsonObject();
        JsonObject start = new JsonObject();
        start.addProperty("name" , startAddress);
        start.addProperty("timestamp", startTimestamp);

        JsonObject end = new JsonObject();
        end.addProperty("name", endAddress);

        body.add("start", start);
        body.add("end", end);
        body.addProperty("mode", mode);

        Response<JsonObject> response = null;
        try {
            response = Ion.with(context)
                    .load("PATCH",API_URI + "/users/" + userID + "/rides/" + rideId)
                    .setLogging("RideLog", Log.VERBOSE)
                    .setHeader(KEY_AUTHORIZATION, "Bearer " + getSecurityToken())
                    .setHeader("Content-Type", "application/json")
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
                                Log.w(TAG, "Code = " + result.getHeaders().code());
                            }
                        }
                    }).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

