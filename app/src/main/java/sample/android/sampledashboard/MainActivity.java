package sample.android.sampledashboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sample.android.sampledashboard.dashboard.DashboardActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordField.getText() == null || passwordField.getText().toString().length() < 6 ||
                        usernameField.getText() == null || usernameField.getText().toString().length() < 1) {
                    if (passwordField.getText() == null || passwordField.getText().toString().length() < 6) {
                        passwordField.setError("Password must be more than 6 characters long");
                        passwordField.requestFocus();
                    }
                    if (usernameField.getText() == null || usernameField.getText().toString().length() < 1) {
                        usernameField.setError("Username cannot be empty");
                        usernameField.requestFocus();
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("USERNAME", usernameField.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
