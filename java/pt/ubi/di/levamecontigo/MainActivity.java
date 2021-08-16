package pt.ubi.di.levamecontigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button login;
    Button register;
    Button no_account;
    databaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new databaseHelper(this);
        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_pass);
        login = (Button) findViewById(R.id.button_login);
        register = (Button) findViewById(R.id.button_register);
        no_account = (Button) findViewById(R.id.button_no_account);

        // LIMPAR A BASE DE DADOS SE TIVER USERS COM CAMPOS A NULL
        //db.deleteNullUsers();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_pass = password.getText().toString().trim();
                if(str_email.isEmpty() || str_pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fill all the fields before trying to login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Boolean res = db.checkUser(str_email, str_pass);
                if(res) {
                    Toast.makeText(MainActivity.this, "Login successfull!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, menu.class);
                    intent.putExtra("Email", str_email);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this, "Login failed!\nCheck username and password and try again...", Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
            }
        });

        no_account.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, showBoleia.class);
                startActivity(intent);
            }
        });

    }
}