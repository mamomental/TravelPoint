package kr.mamo.travelpoint.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kr.mamo.travelpoint.R;
import kr.mamo.travelpoint.auth.FacebookLogin;
import kr.mamo.travelpoint.auth.FacebookLoginResultInterface;
import kr.mamo.travelpoint.auth.LocalLogin;
import kr.mamo.travelpoint.auth.LocalLoginResultInterface;
import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.TP;
import kr.mamo.travelpoint.db.domain.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LocalLoginResultInterface, FacebookLoginResultInterface {
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private LoginButton facebook;
    CallbackManager callbackManager;
    LocalLogin localLogin;
    FacebookLogin facebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        localLogin = new LocalLogin(getApplicationContext());
        localLogin.setResultInterface(this);
        facebookLogin = new FacebookLogin((getApplicationContext()));
        facebookLogin.setResultInterface(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean auto = prefs.getBoolean(Constants.Preference.Account.AUTO_LOGIN, true);
        if (auto) {
            if (localLogin.isLoggedIn()) {
                startMainActivity();
            } else {
                facebookLogin.checkLoggedIn();
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
                    String email = mEmailView.getText().toString();
                    String password = mPasswordView.getText().toString();

                    attemptLocalLogin(email, password);
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                attemptLocalLogin(email, password);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void attemptLocalLogin(final String email, final String password) {
        User user = TP.readUser(this, email);

        if (null != user && 0 != user.getType()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("test").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    convertLoginType(email, password, 0);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LoginManager.getInstance().logOut();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setTitle("Test");
            dialog.show();
        } else {
            localLogin.asyncLogin(email, password);
        }
    }

    private void convertLoginType(String email, String password, int type) {
        User user = TP.readUser(this, email);
        user.setPassword(password);
        user.setType(type);
        user.setSignIn(true);
        TP.updateUser(this, user);
        startMainActivity();
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
            facebookLogin.checkLoggedIn();
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException exception) {
        }
    };

    @Override
    public void onSuccess() {
        startMainActivity();
    }

    @Override
    public void onFail() {
        mPasswordView.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
    }

    @Override
    public void isLoggedIn(boolean loggedIn, final String email, final String id) {
        final Context context = this;
        if (loggedIn) {
            if (TP.validateUser(this, email, id, 1)) {
                startMainActivity();
            } else if (null != TP.readUser(this, email)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                User user = TP.readUser(this, email);
                builder.setMessage("test").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        convertLoginType(email, id, 1);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setTitle("Test");
                dialog.show();
            }
        }
    }
}

