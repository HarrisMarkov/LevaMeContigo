package pt.ubi.di.levamecontigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class editPerfil extends AppCompatActivity {

    EditText user;
    EditText mail;
    EditText pass;
    EditText pass_conf;
    TextView bol_ofer;
    TextView bol_req;
    Button save;
    databaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        db = new databaseHelper(this);

        user = (EditText) findViewById(R.id.origem);
        mail = (EditText) findViewById(R.id.destino);
        pass = (EditText) findViewById(R.id.lugares);
        pass_conf = (EditText) findViewById(R.id.contribuicao);
        //bol_ofer = (TextView) findViewById(R.id.bol_ofer);
        //bol_req = (TextView) findViewById(R.id.bol_req);
        save = (Button) findViewById(R.id.save);
        String email = getIntent().getStringExtra("Email");

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String str_user = user.getText().toString().trim();
                String str_email = mail.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                String str_pass_conf = pass_conf.getText().toString().trim();
                if(str_pass.equals(str_pass_conf)){
                    Boolean update = db.updateUser(str_user, email, str_pass);
                    if(update) {
                        Toast.makeText(editPerfil.this, "Update successfull!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(editPerfil.this, menu.class);
                        intent.putExtra("Email", email);
                        intent.putExtra("Name", str_user);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(editPerfil.this, "Update failed!\n", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(editPerfil.this, "Check the passwords...\n", Toast.LENGTH_SHORT).show();
            }
        });
    }
}