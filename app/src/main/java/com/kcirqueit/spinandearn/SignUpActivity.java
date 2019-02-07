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

public class SignUpActivity extends AppCompatActivity {


    @BindView(R.id.s_phone_et)
    EditText mPhoneEt;

    @BindView(R.id.s_spinner)
    Spinner mCountrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        ButterKnife.bind(this);

        mCountrySpinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

    }

    @OnClick(R.id.signup_bt)
    public void signUp() {

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
        verifyIntent.putExtra("signup", "signup");
        startActivity(verifyIntent);

    }

}
