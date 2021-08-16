package pt.ubi.di.levamecontigo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class menu extends AppCompatActivity {

    Button add_boleia;
    Button ver_boleias;
    Button ver_boleias_all;
    Button edit_perfil;
    Button button_confirm;
    Button button_cancel;
    TextView textView_user;
    TextView textView_ori_dest;
    TextView textView_data_hora;
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;
    databaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        db = new databaseHelper(this);
        add_boleia = (Button) findViewById(R.id.button_add_boleia);
        ver_boleias = (Button) findViewById(R.id.button_ver_boleias);
        ver_boleias_all = (Button) findViewById(R.id.button_ver_boleias_all);
        edit_perfil = (Button) findViewById(R.id.button_edit_perfil);

        String email = getIntent().getStringExtra("Email");
        String nome = getIntent().getStringExtra("Nome");
        //Toast.makeText(menu.this, "Email: " + email, Toast.LENGTH_SHORT).show();

        Cursor cursor = db.checkPendentes(email);
        if(cursor.getCount() == 0){
            Toast.makeText(menu.this, "Não tem pedidos por aceitar", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                int id = Integer.parseInt(cursor.getString(1));
                String pede = cursor.getString(2);
                String data = cursor.getString(4);
                String hora = cursor.getString(5);
                String origem = cursor.getString(6);
                String destino = cursor.getString(7);
                createDialogConfirmarBoleia(id, email, pede, data, hora, origem, destino);
                //Toast.makeText(menu.this, "Pede: " + pede + "\nData: " + data + " - " + hora + "\n" + origem + " -> " + destino, Toast.LENGTH_LONG).show();
            }

        }

        add_boleia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu.this, addBoleia.class);
                intent.putExtra("Email", email);
                intent.putExtra("Nome", nome);
                startActivity(intent);
            }
        });

        ver_boleias.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu.this, boleiasCombinadas.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        ver_boleias_all.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu.this, showBoleia.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        edit_perfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu.this, editPerfil.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

    }

    public void createDialogConfirmarBoleia(int id, String user, String pede, String data, String hora, String origem, String destino){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_confirma, null);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        textView_user = (TextView) popupView.findViewById(R.id.textView_user);
        textView_ori_dest = (TextView) popupView.findViewById(R.id.textView_origem_destino);
        textView_data_hora = (TextView) popupView.findViewById(R.id.textView_data_hora);

        //Toast.makeText(menu.this, "Pede: " + pede + "\nData: " + data + " - " + hora + "\n" + origem + " -> " + destino, Toast.LENGTH_LONG).show();

        textView_user.setText(pede);
        textView_ori_dest.setText(origem + " -> " + destino);
        textView_data_hora.setText(data + "   " + hora);

        button_confirm = (Button) popupView.findViewById(R.id.button_confirmar);
        button_cancel = (Button) popupView.findViewById(R.id.button_cancelar);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean val = db.updateBoleia(id, user, pede);
                if(val){
                    Toast.makeText(menu.this, "Pedido confirmado!", Toast.LENGTH_SHORT).show();
                    Boolean del = db.deletePendente(id);
                    if(del){
                        Toast.makeText(menu.this, "DB update successfull!", Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(menu.this, "DB update failed...", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}