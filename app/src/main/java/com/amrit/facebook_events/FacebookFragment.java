package com.amrit.facebook_events;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Amrit on 6/8/2017.
 */

public class FacebookFragment extends Fragment {
    private static final String TAG = "Facebook Fragment";
    private static final String PERMISSION = "publish_actions";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final String PENDING_ACTION_BUNDLE_KEY =
            "com.example.hellofacebook:PendingAction";
    JSONObject jsonObject;
    ArrayList<JsonModuleParcelable> list = new ArrayList<JsonModuleParcelable>();
    String userName;
    String userId;
    LinearLayout profileLayout;
    ImageView profileImage;
    TextView profileName;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private LoginButton logoutButton;
    private boolean postingEnabled = false;
    private ImageView profilePicImageView;
    private TextView greeting;
    private ProfileTracker profileTracker;
    private LinearLayout main_layout;
    private PendingAction pendingAction = PendingAction.NONE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        // Other app specific specialization
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // forward the login results to the callbackManager
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(getActivity());

        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_facebook, parent, false);
        loginButton = (LoginButton) v.findViewById(R.id.loginButton);
        main_layout = (LinearLayout) v.findViewById(R.id.main_layout);
        profilePicImageView = (ImageView) v.findViewById(R.id.profilePicture);
        greeting = (TextView) v.findViewById(R.id.greeting);
        profileLayout = (LinearLayout) v.findViewById(R.id.profile_details);
        profileImage = (ImageView) v.findViewById(R.id.profile_picture);
        profileName = (TextView) v.findViewById(R.id.profile_name);
        logoutButton = (LoginButton) v.findViewById(R.id.logoutButton);

        // If using in a fragment
        loginButton.setFragment(this);
        //To access additional profile information (must be submitted for app review if "logInWithReadPermissions" is used)
        loginButton.setReadPermissions(Arrays.asList("user_status"));
        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
        // to create a callback manager to handle login responses
        callbackManager = CallbackManager.Factory.create();

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        // Callback registration
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        postingEnabled = true;
                        Toast toast = Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT);
                        toast.show();
                        updateUI();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        if (pendingAction != PendingAction.NONE) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                    }

                    // after cancelled
                    private void showAlert() {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (pendingAction != PendingAction.NONE
                                && exception instanceof FacebookAuthorizationException) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                    }
                });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
            }
        };
        return v;
    }

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {
            new LoadProfileImage(profileImage).execute(profile.getProfilePictureUri(200, 200).toString());
            profileName.setText("Wass up " + profile.getFirstName() + " ?");
            postingEnabled = true;
            profileLayout.setVisibility(View.VISIBLE);
            main_layout.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.batman);
            profilePicImageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), icon, 200, 200, 200, false, false, false, false));
            greeting.setText(null);
            postingEnabled = false;
            profileLayout.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        }
    }

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                Bitmap resized = Bitmap.createScaledBitmap(result, 200, 200, true);
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(), resized, 250, 200, 200, false, false, false, false));
            }
        }
    }
}