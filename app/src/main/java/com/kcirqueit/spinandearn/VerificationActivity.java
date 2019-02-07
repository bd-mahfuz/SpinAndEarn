package com.kcirqueit.spinandearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationActivity extends AppCompatActivity {

    private String mVerificationId;
    private String valueFromSignUp = "";
    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    @BindView(R.id.code_et)
    EditText mCodeEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();

        ButterKnife.bind(this);

        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        Log.d("Phone number:", phoneNumber);

        String valueFromSignUp = getIntent().getStringExtra("signup");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait ...");

        sendVerificationCode(phoneNumber);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mProgressDialog.show();

    }

    @OnClick(R.id.submit_bt)
    public void submitOnClick() {

        String code = mCodeEt.getText().toString().trim();

        if (code.isEmpty() || code.length() < 6) {
            mCodeEt.setError("Enter code...");
            mCodeEt.requestFocus();
            return;
        }

        verifyCode(code);

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        if (!valueFromSignUp.equals("")) {
            signInWithCredential(credential);


        } else {
            signUpWithCredential(credential);
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent mainIntent = new Intent(VerificationActivity.this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);

                        } else {
                            Toast.makeText(VerificationActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
    }

    private void signUpWithCredential(PhoneAuthCredential credential) {

    }



    public void sendVerificationCode(String number) {
        Log.d("VerificationCode meth:", "");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, 60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );


    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    mVerificationId = s;
                    Log.d("VerificationId:", mVerificationId);
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    String code = phoneAuthCredential.getSmsCode();
                    Log.d("verification code:", code);
                    if (code != null) {
                        mCodeEt.setText(code);
                        mProgressDialog.dismiss();
                        //verifyCode(code);
                    }

                }

                @Override
                public void onVerificationFailed(FirebaseException e) {

                    Toast.makeText(VerificationActivity.this, e.getMessage()+"",
                            Toast.LENGTH_SHORT).show();

                }
            };
}
