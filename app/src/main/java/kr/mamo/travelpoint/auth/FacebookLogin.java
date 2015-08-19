package kr.mamo.travelpoint.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;

/**
 * Created by mentalmamo on 15. 8. 13..
 */
public class FacebookLogin implements FacebookLoginInterface {
    private Context context;
    private FacebookLoginResultInterface resultInterface;

    public FacebookLogin(Context context) {
        this.context = context;
    }
    @Override
    public void checkLoggedIn() {
        AccessToken at = AccessToken.getCurrentAccessToken();
        if (null != at) {
            GraphRequest request = GraphRequest.newMeRequest(at, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    try {
                        String email = jsonObject.getString("email");
                        String id = jsonObject.getString("id");
                        if (null != resultInterface && null != email && null != id) {
                            resultInterface.isLoggedIn(true, email, id);
                        } else {
                            resultInterface.isLoggedIn(false, null, null);
                        }
                    } catch (JSONException e) {
                    }

                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "email");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    @Override
    public void asyncLogin(String email, String password) {

    }

    public FacebookLoginResultInterface getResultInterface() {
        return resultInterface;
    }

    public void setResultInterface(FacebookLoginResultInterface resultInterface) {
        this.resultInterface = resultInterface;
    }

}
