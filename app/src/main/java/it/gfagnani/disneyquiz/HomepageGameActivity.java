package it.gfagnani.disneyquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class HomepageGameActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogout;
    private ParseUser parseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_game);

        parseUser = new ParseUser();

        btnLogout = findViewById(R.id.btnHomeGameLogout);
        btnLogout.setOnClickListener(HomepageGameActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHomeGameLogout:
                logout();
                break;
        }
    }

    private void logout () {
        if (ParseUser.getCurrentUser() != null) {
            parseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Utilities.showToast(HomepageGameActivity.this,
                                "Uscito con successo!",
                                FancyToast.SUCCESS);
                        goToHomeLogin();
                    } else {
                        Utilities.showToast(HomepageGameActivity.this,
                                "Errore",
                                FancyToast.ERROR);
                    }
                }
            });
        } else {
            Utilities.showToast(HomepageGameActivity.this,
                    "Errore",
                    FancyToast.ERROR);
        }
    }

    private void goToHomeLogin() {
        Intent intent = new Intent(HomepageGameActivity.this, HomepageLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
