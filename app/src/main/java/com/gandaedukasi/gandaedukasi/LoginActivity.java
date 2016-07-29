package com.gandaedukasi.gandaedukasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button btnLoggin, btnReg;
    EditText textEmail, textPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLoggin = (Button)findViewById(R.id.buttonLogin);
        btnReg = (Button)findViewById(R.id.buttonReg);
        textEmail = (EditText)findViewById(R.id.editEmail);
        textPassword = (EditText)findViewById(R.id.editPassword);

        btnLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });
    }
    private void attemptLogin() {
        textEmail.setError(null);
        textPassword.setError(null);

        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            textPassword.setError(getString(R.string.id_error_password_empty));
            focusView = textPassword;
            cancel = true;
        }else if (!isPasswordValid(password)){
            textPassword.setError(getString(R.string.id_error_passowrd_length));
            focusView = textPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            textEmail.setError(getString(R.string.id_error_email_empty));
            focusView = textEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            textEmail.setError(getString(R.string.id_error_email_invalid));
            focusView = textEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // TODO: proses login
            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(i);
        }
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
