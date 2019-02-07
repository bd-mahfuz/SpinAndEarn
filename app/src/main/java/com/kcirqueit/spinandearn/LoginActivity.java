package com.kcirqueit.spinandearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kcirqueit.spinandearn.util.CountryData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.phone_et)
    EditText mPhoneEt;

    @BindView(R.id.country_spinner)
    Spinner mCountrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        // setting the country name to the spinner
        mCountrySpinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));




    }

    @OnClick(R.id.login_bt)
    public void loginOnClick() {

        String code = CountryData.countryAreaCodes[mCountrySpinner.getSelectedItemPosition()];
        String number = mPhoneEt.getText().toString();

        if (number.isEmpty() || number.length() < 10) {
            mPhoneEt.setError("Phone number is not valid!");
            mPhoneEt.requestFocus();
            return;
        }

        String phoneNumber = "+" + code + number;

        Intent verifyIntent = new Intent(this, VerificationActivity.class);
        verifyIntent.putExtra("phoneNumber", phoneNumber);
        startActivity(verifyIntent);


    }

    @OnClick(R.id.signup_tv)
    public void signUpOnClick() {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);

    }

}
