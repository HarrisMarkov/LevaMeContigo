package pt.ubi.di.levamecontigo;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {

    private static final String CHAR_LIST = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!?@#$%&€/|()[]{}.,;-_*+";

    EditText email;
    EditText password;
    EditText password_conf;
    Button register;
    databaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new databaseHelper(this);
        email = (EditText) findViewById(R.id.edixtText_email);
        password = (EditText) findViewById(R.id.editText_password);
        password_conf = (EditText) findViewById(R.id.editText_confirm);
        register = (Button) findViewById(R.id.button_add_boleia);

        String str = generateRandomStringUsingSecureRandom(15);
        password.setHint(str);
        password_conf.setHint(str);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_pass = password.getText().toString().trim();
                String str_conf = password_conf.getText().toString().trim();

                if(!str_email.isEmpty() && !str_pass.isEmpty() && str_pass.equals(str_conf)){
                    if(db.checkUser(str_email))
                        Toast.makeText(register.this, "Tente outro e-mail!", Toast.LENGTH_SHORT).show();
                    else{
                        long val = db.addUser(str_email, str_pass);
                        if(val > 0){
                            Toast.makeText(register.this, "Registration successfull!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(register.this, login.class);
                            intent.putExtra("email", str_email);
                            startActivity(intent);
                        }
                    }
                }
                else{
                    Toast.makeText(register.this, "Registration failed!\nTry again in a few seconds...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String generateRandomStringUsingSecureRandom(int length){
        StringBuffer randStr = new StringBuffer(length);
        SecureRandom secureRandom = new SecureRandom();
        for( int i = 0; i < length; i++ )
            randStr.append( CHAR_LIST.charAt( secureRandom.nextInt(CHAR_LIST.length()) ) );
        return randStr.toString();
    }
}