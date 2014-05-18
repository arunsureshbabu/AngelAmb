package com.arunsureshbabu.angelamb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends ActionBarActivity {
	SharedPreferences prefs = null;
	private String username, password, confirmpassword, userType, EMail,
			phonenum, firstName, lastName, EmergencyContactNumber1,
			EmergencyContactNumber2, EmergencyContactNumber3, EmergencyEmail1,
			EmergencyEmail2, EmergencyEmail3;
	private Spinner UserType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	private class PrefetchData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// before making http calls
			EditText Username = (EditText) findViewById(R.id.Username_editText);
			username = Username.getText().toString();
			EditText Password = (EditText) findViewById(R.id.Password_editText);
			password = Password.getText().toString();
			EditText ConfirmPassword = (EditText) findViewById(R.id.confirmPassord_editText);
			confirmpassword = ConfirmPassword.getText().toString();
			UserType = (Spinner) findViewById(R.id.userType_spinner);
			userType = UserType.getSelectedItem().toString();
			EditText email = (EditText) findViewById(R.id.Email_editText);
			EMail = email.getText().toString();
			EditText PhoneNum = (EditText) findViewById(R.id.PhoneNum_editText);
			phonenum = PhoneNum.getText().toString();
			EditText FirstName = (EditText) findViewById(R.id.FirstName_editText);
			firstName = FirstName.getText().toString();
			EditText LastName = (EditText) findViewById(R.id.LastName_editText);
			lastName = LastName.getText().toString();
			EmergencyContactNumber1 = "102";
			EmergencyContactNumber2 = "101";
			EmergencyContactNumber3 = "100";
			EmergencyEmail1 = "test1@gmail.com";
			EmergencyEmail2 = "test2@gmail.com";
			EmergencyEmail3 = "test3@gmail.com";
			TextView msgTextField = (TextView) findViewById(R.id.RegLabel_textView);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (password.equals(confirmpassword)) {
				if (username.length() > 0) {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://angelamb.antolabs.com/angelamb/user");

					try {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								12);
						nameValuePairs.add(new BasicNameValuePair("name",
								username));
						nameValuePairs
								.add(new BasicNameValuePair("mail", EMail));
						nameValuePairs.add(new BasicNameValuePair(
								"field_first_name[und][0][value]", firstName));
						nameValuePairs.add(new BasicNameValuePair(
								"field_last_name[und][0][value]", lastName));
						nameValuePairs.add(new BasicNameValuePair(
								"field_user_phone[und][0][value]", phonenum));
						nameValuePairs.add(new BasicNameValuePair(
								"field_patient_type[und][0][value]", userType));
						nameValuePairs.add(new BasicNameValuePair(
								"field_phone1[und][0][value]",
								EmergencyContactNumber1));
						nameValuePairs.add(new BasicNameValuePair(
								"field_phone2[und][0][value]",
								EmergencyContactNumber2));
						nameValuePairs.add(new BasicNameValuePair(
								"field_phone3[und][0][value]",
								EmergencyContactNumber3));
						nameValuePairs
								.add(new BasicNameValuePair(
										"field_email1[und][0][value]",
										EmergencyEmail1));
						nameValuePairs
								.add(new BasicNameValuePair(
										"field_email2[und][0][value]",
										EmergencyEmail2));
						nameValuePairs
								.add(new BasicNameValuePair(
										"field_email3[und][0][value]",
										EmergencyEmail3));
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs));
						HttpResponse response = httpclient.execute(httppost);
						// msgTextField.setText("");
						// reset the message text field
						Toast.makeText(getBaseContext(), "Sent",
								Toast.LENGTH_SHORT).show();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					Intent i;
					switch (UserType.getSelectedItemPosition()) {
					case 1:
						prefs.edit().putBoolean("isEmergencyCrew", true)
								.commit();
						i = new Intent(RegistrationActivity.this,
								EmergencyCrewActivity.class);
						startActivity(i);
						System.out.println("Emergency Crew"); // Close this
						finish();
						break;
					case 2:
						prefs.edit().putBoolean("isDoctor", true).commit();
						i = new Intent(RegistrationActivity.this,
								DoctorActivity.class);
						startActivity(i);
						System.out.println("Doctor"); // Close this Activity.
						finish();
						break;
					default:
						prefs.edit().putBoolean("isGeneralUser", true).commit();
						i = new Intent(RegistrationActivity.this,
								GeneralUserActivity.class);
						startActivity(i);
						System.out.println("General User"); // Close this
						finish();
						break;
					}

				} else {
					// display message if text field is empty
					Toast.makeText(getBaseContext(), "All fields are required",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getBaseContext(), "Passwords not Match!",
						Toast.LENGTH_SHORT).show();
			}
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_registration,
					container, false);
			return rootView;
		}
	}

	public void reset(View v) {
		EditText Username = (EditText) findViewById(R.id.Username_editText);
		Username.setText("");
		EditText Password = (EditText) findViewById(R.id.Password_editText);
		Password.setText("");
		;
		EditText ConfirmPassword = (EditText) findViewById(R.id.confirmPassord_editText);
		ConfirmPassword.setText("");
		Spinner UserType = (Spinner) findViewById(R.id.userType_spinner);
		UserType.setSelection(0);
		EditText email = (EditText) findViewById(R.id.Email_editText);
		email.setText("");
		EditText PhoneNum = (EditText) findViewById(R.id.PhoneNum_editText);
		PhoneNum.setText("");
		EditText FirstName = (EditText) findViewById(R.id.FirstName_editText);
		FirstName.setText("");
		EditText LastName = (EditText) findViewById(R.id.LastName_editText);
		LastName.setText("");
	}

	public void submit(View v) {
		new PrefetchData().execute();
	}
}
