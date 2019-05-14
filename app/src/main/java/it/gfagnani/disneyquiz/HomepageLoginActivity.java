package it.gfagnani.disneyquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class HomepageLoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnSignUp;
    private ParseUser parseUser;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_login);

        constraintLayout = findViewById(R.id.constraintHome);

        edtUsername = findViewById(R.id.edtHomeUsername);
        edtPassword = findViewById(R.id.edtHomePassword);

        btnLogin = findViewById(R.id.btnHomeLogin);
        btnSignUp = findViewById(R.id.btnHomeRegistrati);

        btnLogin.setOnClickListener(HomepageLoginActivity.this);
        btnSignUp.setOnClickListener(HomepageLoginActivity.this);
        constraintLayout.setOnClickListener(HomepageLoginActivity.this);

        edtPassword.setOnKeyListener(HomepageLoginActivity.this);

        parseUser = new ParseUser();

        if (ParseUser.getCurrentUser() != null) {
            goToHomeGame();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHomeLogin:
                login(edtUsername.getText().toString(), edtPassword.getText().toString());
                break;
            case R.id.btnHomeRegistrati:
                goToSignUp("");
                break;
            case R.id.constraintHome:
                rootLayoutTapped();
                break;
        }
    }

    private void goToHomeGame() {
        Intent intent = new Intent(HomepageLoginActivity.this, HomepageGameActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSignUp(String username) {
        Intent intent = new Intent(HomepageLoginActivity.this, RegistratiActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void login(final String username, String password) {
        if (username.equals("") || password.equals("")) {
            Utilities.showToast(HomepageLoginActivity.this,
                    "Username e password sono richiesti per entrare",
                    FancyToast.INFO);
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(username + " sta entrando...");
            progressDialog.show();
            parseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        if (user != null) {
                            Utilities.showToast(HomepageLoginActivity.this,
                                    "Autenticazione riuscita!",
                                    FancyToast.SUCCESS);
                            goToHomeGame();
                            finish();
                        }
                    } else {
                        Utilities.showToast(HomepageLoginActivity.this,
                                "Nessun utente trovato. Registrati per giocare!",
                                FancyToast.ERROR);
                        goToSignUp(username);
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void rootLayoutTapped() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            onClick(btnLogin);
        }
        return false;
    }
}
