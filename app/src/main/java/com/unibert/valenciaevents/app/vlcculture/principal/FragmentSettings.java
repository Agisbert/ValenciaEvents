package com.unibert.valenciaevents.app.vlcculture.principal;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.PlusClient;
import com.unibert.valenciaevents.app.vlcculture.R;



public class FragmentSettings extends Fragment {


	EditText nombre;
	CheckBox notificaciones;
	RadioButton todos;
	RadioButton free;
	RadioButton family;
	Button guardar;
	RadioGroup grupo;
	public static SharedPreferences settings;
	public static SharedPreferences.Editor editor;
	private Button mSignInButton;


	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
    private TextView userName;
    private LoginButton loginBtn;
    private ProfilePictureView profilePictureView;
    private GraphUser userLog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView = inflater.inflate(
				R.layout.fragment_settings, container, false);

        userName = (TextView) rootView.findViewById(R.id.nombreFB);
        loginBtn = (LoginButton) rootView.findViewById(R.id.authButton);
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                userLog = user;
                updateUI();

            }
        });
        profilePictureView = (ProfilePictureView) rootView.findViewById(R.id.profilePicture);

		return rootView;
	}


    private void updateUI() {
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());

        if (enableButtons && userLog != null) {
            profilePictureView.setProfileId(userLog.getId());
            userName.setText(userLog.getFirstName());
        } else {
            profilePictureView.setProfileId(null);
            userName.setText(null);
        }
    }
}