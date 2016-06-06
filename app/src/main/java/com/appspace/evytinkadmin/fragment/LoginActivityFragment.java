package com.appspace.evytinkadmin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.appspace.evytinkadmin.R;
import com.appspace.evytinkadmin.activity.MainActivity;
import com.appspace.evytinkadmin.manager.APIManager;
import com.appspace.evytinkadmin.model.EvyTinkUser;
import com.appspace.evytinkadmin.util.DataStoreUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityFragment extends Fragment {

    private final String TAG = "LoginActivity";

    LoginButton btnFacebookLogin;
    CallbackManager fbCallbackManager;
    AccessTokenTracker fbAccessTokenTracker;
    AccessToken fbAccessToken;
    ProfileTracker fbProfileTracker;
    Profile fbProfile;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    public LoginActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbCallbackManager = CallbackManager.Factory.create();
        fbAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                if (currentAccessToken == null) {
                    LoggerUtils.log2D(TAG, "facebook:Logout");
                    FirebaseAuth.getInstance().signOut();
                    DataStoreUtils.getInstance().setLogin(false);
                }
                fbAccessToken = currentAccessToken;
            }
        };
        // If the access token is available already assign it.
        fbAccessToken = AccessToken.getCurrentAccessToken();

        fbProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
                fbProfile = currentProfile;
            }
        };

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    LoggerUtils.log2D(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    gotoMainActivity();

                    if (fbProfile != null) {
                        String fId = fbProfile.getId();
                        String fName = fbProfile.getName();
                        registerEvyTinkUser(fId, fName);
                    }

                } else {
                    // User is signed out
                    LoggerUtils.log2D(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fbAccessTokenTracker.stopTracking();
        fbProfileTracker.stopTracking();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
        btnFacebookLogin = (LoginButton) view.findViewById(R.id.btnFacebookLogin);
        btnFacebookLogin.setReadPermissions("public_profile", "email");
        btnFacebookLogin.setFragment(this);
        btnFacebookLogin.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoggerUtils.log2D(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                LoggerUtils.log2D(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                LoggerUtils.log2DT(TAG, "facebook:onError", error);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        LoggerUtils.log2D(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        LoggerUtils.log2D(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        LoggerUtils.log2D(TAG, "signInWithCredential:onComplete:Uid:" + user.getUid());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            LoggerUtils.log2DT(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    protected void registerEvyTinkUser(String id, String name) {
        LoggerUtils.log2D(TAG, "registerEvyTinkUser:id:" + id);
        LoggerUtils.log2D(TAG, "registerEvyTinkUser:name:" + name);
        Call<EvyTinkUser[]> call = APIManager.getInstance().getEvyTinkAPIService().register(id, name);
        call.enqueue(new Callback<EvyTinkUser[]>() {
            @Override
            public void onResponse(Call<EvyTinkUser[]> call, Response<EvyTinkUser[]> response) {

                LoggerUtils.log2D(TAG, "registerEvyTinkUser:message:" + response.message());
                EvyTinkUser evyTinkUser = response.body()[0];

                DataStoreUtils.getInstance().setLogin(true);
                DataStoreUtils.getInstance().setAppUserId(evyTinkUser.evyaccountid);
                DataStoreUtils.getInstance().setFacebookId(evyTinkUser.evyfacebookid);

                gotoMainActivity();
            }

            @Override
            public void onFailure(Call<EvyTinkUser[]> call, Throwable t) {

            }
        });
    }

    protected void gotoMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        // close this activity
        getActivity().finish();
    }
}
