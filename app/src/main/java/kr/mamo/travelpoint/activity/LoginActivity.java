package kr.mamo.travelpoint.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private LoginButton facebook;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean auto = prefs.getBoolean(Constants.Preference.Account.AUTO_LOGIN, true);
        if (auto) {
            kr.mamo.travelpoint.db.domain.User user = TP.autoLogin(getApplicationContext());
            if (null != user) {
                startMainActivity();
            }
        }

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLocalLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLocalLogin();
            }
        });

        facebook = (LoginButton) findViewById(R.id.facebook_sign_in_button);
        facebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                startFacebookActivity();
            }
        });
        facebook.setReadPermissions("email", "public_profile");
        // If using in a fragment
//        facebook.setFragment(this);
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        facebook.registerCallback(callbackManager, facebookCallback);

        AccessToken at = AccessToken.getCurrentAccessToken();
        if (null != at) {
            Log.i(Constants.LOGCAT_TAGNAME, "at : " + at.getToken());

            GraphRequest request = GraphRequest.newMeRequest(at, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    Log.i(Constants.LOGCAT_TAGNAME, "response : " + graphResponse.toString());

                    try {
                        int responseCode = graphResponse.getJSONObject().getInt("responseCode");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void attemptLocalLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(getApplicationContext(), email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private void startFacebookActivity() {
        Intent intent = new Intent(this, FacebookActivity.class);
        startActivity(intent);
//        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.i(Constants.LOGCAT_TAGNAME, "facebook success :");
            AccessToken token = loginResult.getAccessToken();
        }

        @Override
        public void onCancel() {
            Log.i(Constants.LOGCAT_TAGNAME, "facebook cancel :");

        }

        @Override
        public void onError(FacebookException exception) {
            Log.i(Constants.LOGCAT_TAGNAME, "facebook error :");

        }
    };

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

            if (success) {
                startMainActivity();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

