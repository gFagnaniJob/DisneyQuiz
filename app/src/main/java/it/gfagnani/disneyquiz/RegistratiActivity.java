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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class RegistratiActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private EditText edtEmail, edtUsername, edtPassword, edtConfirmPassword;
    private Button btnSignUp;
    private ConstraintLayout constraintLayout;
    private ParseUser parseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrati);

        edtEmail = findViewById(R.id.edtRegistratiEmail);
        edtUsername = findViewById(R.id.edtRegistratiUsername);
        edtPassword = findViewById(R.id.edtRegistratiPassword);
        edtConfirmPassword = findViewById(R.id.edtRegistratiConfermaPassword);

        Intent oldIntent = getIntent();
        String username = oldIntent.getExtras().getString("username");

        if (username.contains("@"))
            edtEmail.setText(username);
        else
            edtUsername.setText(username);

        btnSignUp = findViewById(R.id.btnRegistratiRegistrati);

        constraintLayout = findViewById(R.id.constraintRegistrati);

        btnSignUp.setOnClickListener(RegistratiActivity.this);
        constraintLayout.setOnClickListener(RegistratiActivity.this);

        edtConfirmPassword.setOnKeyListener(RegistratiActivity.this);

        parseUser = new ParseUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistratiRegistrati:
                signUp(edtEmail.getText().toString(),
                        edtUsername.getText().toString(),
                        edtPassword.getText().toString(),
                        edtConfirmPassword.getText().toString());
                break;
            case R.id.constraintRegistrati:
                rootLayoutTapped();
                break;
        }
    }

    private void goToHomeGame() {
        Intent intent = new Intent(RegistratiActivity.this, HomepageGameActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            onClick(btnSignUp);
        }
        return false;
    }

    private void rootLayoutTapped() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    private void signUp(String email, String username, String password, String cPassword) {
        if (password.equals(cPassword)) {
            try {
                if (email.equals("") || username.equals("") || password.equals("")) {
                    Utilities.showToast(RegistratiActivity.this,
                            "Tutti i campi sono richiesti",
                            FancyToast.INFO);
                } else {
                    parseUser.setEmail(email);
                    parseUser.setUsername(username);
                    parseUser.setPassword(password);

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(username + " si sta registrando...");
                    progressDialog.show();

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Utilities.showToast(RegistratiActivity.this,
                                        "Registrato con successo",
                                        FancyToast.SUCCESS);
                                goToHomeGame();
                                finish();
                            } else {
                                Utilities.showToast(RegistratiActivity.this,
                                        "Errore",
                                        FancyToast.ERROR);
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
            } catch (IllegalArgumentException e) {
                Utilities.showToast(RegistratiActivity.this,
                        "Tutti i campi sono obbligatori",
                        FancyToast.INFO);
            }
        } else {
            Utilities.showToast(RegistratiActivity.this,
                    "Le due password non corrispondono",
                    FancyToast.ERROR);
        }
    }
}
