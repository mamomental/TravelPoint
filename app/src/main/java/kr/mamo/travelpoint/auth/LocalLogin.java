package kr.mamo.travelpoint.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;

import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;

/**
 * Created by mentalmamo on 15. 8. 13..
 */
public class LocalLogin implements LocalLoginInterface {
    private UserLoginTask mAuthTask = null;
    private Context context;
    private LocalLoginResultInterface resultInterface;

    public LocalLogin(Context context) {
        this.context = context;
    }
    @Override
    public boolean isLoggedIn() {
        kr.mamo.travelpoint.db.domain.User user = TP.autoLogin(context);
        return (null != user);
    }

    @Override
    public void asyncLogin(String email, String password) {
        if (mAuthTask != null) {
            return;
        }

        boolean cancel = false;
        View focusView = null;

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(context, email, password);
            mAuthTask.execute((Void) null);
        }
    }

    public LocalLoginResultInterface getResultInterface() {
        return resultInterface;
    }

    public void setResultInterface(LocalLoginResultInterface resultInterface) {
        this.resultInterface = resultInterface;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final Context mContext;
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(Context context, String email, String password) {
            mContext = context;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean result = TP.validateUser(mContext, mEmail, mPassword);
            if (result) {
                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                prefs.putString(Constants.Preference.Account.EMAIL, mEmail);
                prefs.commit();

            }
            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (null != resultInterface) {
                if (success) {
                    resultInterface.onSuccess();
                } else {
                    resultInterface.onSuccess();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
