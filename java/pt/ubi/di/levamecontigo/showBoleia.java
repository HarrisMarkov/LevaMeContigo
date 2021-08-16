package pt.ubi.di.levamecontigo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class showBoleia extends AppCompatActivity {

    databaseHelper db;
    ListView listView_boleias;
    TextView textView_user;
    Button button_confirm;
    Button button_cancel;
    Button button_confirm2;
    Button button_cancel2;
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_boleia);

        db = new databaseHelper(this);
        listView_boleias = (ListView) findViewById(R.id.listView_boleias_combinadas);


        String email = getIntent().getStringExtra("Email");


        if(email != null){  // USER COM CONTA PODE VER TODAS AS BOLEIAS
            ArrayList<String> theList = new ArrayList<String>();
            Cursor cursor = db.getAllBoleias();
            if (cursor.getCount() == 0)
                Toast.makeText(showBoleia.this, "ListView update failed!", Toast.LENGTH_SHORT).show();
            else{
                while(cursor.moveToNext()){
                    String str = "";
                    if(cursor.getString(1).equalsIgnoreCase("pedido")){ // PEDE BOLEIA
                        str = "\nPEDIDO de: " + cursor.getString(3) + "\n" + cursor.getString(4) + " - " + cursor.getString(5) + "\n" + cursor.getString(6) + " -> " + cursor.getString(7) + "\n" + cursor.getString(0);
                    }
                    else if(cursor.getString(1).equalsIgnoreCase("oferta")){ // OFERECE BOLEIA
                        str = "\nOFERTA de: " + cursor.getString(2) + "\n" + cursor.getString(4) + " - " + cursor.getString(5) + "\n" + cursor.getString(6) + " -> " + cursor.getString(7) + "\nLugares: " + cursor.getString(8) + "\n€ " + cursor.getString(9)+ "\n"  + cursor.getString(0);
                    }

                    theList.add(str);
                    ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                    listView_boleias.setAdapter(listAdapter);
                }
            }
        }
        else {  // USER SEM CONTA SO PODE VER AS BOLEIAS DISPONIVEIS PARA HOJE E AMANHA
            ArrayList<String> theList = new ArrayList<String>();
            Cursor cursor = db.getAllBoleiasNextDays();
            if (cursor .getCount() == 0)
                Toast.makeText(showBoleia.this, "ListView update failed!", Toast.LENGTH_SHORT).show();
            else{
                while(cursor.moveToNext()){
                    String str = "";
                    if(cursor.getString(1).equalsIgnoreCase("pedido")){ // PEDE BOLEIA
                        str = "\n" + cursor.getString(4) + " - " + cursor.getString(5) + "\n" + cursor.getString(6) + " -> " + cursor.getString(7) + "\n"  + cursor.getString(0);
                    }
                    else if(cursor.getString(1).equalsIgnoreCase("oferta")){ // OFERECE BOLEIA
                        str = "\n" + cursor.getString(4) + " - " + cursor.getString(5) + "\n" + cursor.getString(6) + " -> " + cursor.getString(7) + "\n"  + cursor.getString(0);
                    }

                    theList.add(str);
                    ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                    listView_boleias.setAdapter(listAdapter);
                }
            }
        }

        listView_boleias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = listView_boleias.getItemAtPosition(i).toString().trim();
                String[] split = str.split("\n");
                String user = split[0].split(":")[1].trim();
                String data = split[1].split(" - ")[0].trim();
                String hora = split[1].split(" - ")[1].trim();
                String origem = split[2].split("->")[0].trim();
                String destino = split[2].split("->")[1].trim();
                //Toast.makeText(showBoleia.this, "user: " + user + "\ndata: " + data + "\thora: " + hora + "\nde: " + origem + "\tpara: " + destino, Toast.LENGTH_LONG).show();
                int id = Integer.parseInt(split[5]);
                if(email.equalsIgnoreCase(user)){ // EDITAR A PRÓPRIA OFERTA DE BOLEIA
                    //Toast.makeText(showBoleia.this, "user: " + user + "\ndata: " + data + "\thora: " + hora + "\nde: " + origem + "\tpara: " + destino, Toast.LENGTH_LONG).show();
                    createDialogApagarBoleia(id, email);
                }
                else {  // SOLICITAR A BOLEIA SELECIONADA
                    createDialogPedirBoleia(id, email, user, data, hora, origem, destino);
                }

            }
        });

    }

    public void createDialogApagarBoleia(int id, String email){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_apagar, null);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        button_confirm = (Button) popupView.findViewById(R.id.button_apagar);
        button_cancel = (Button) popupView.findViewById(R.id.button_cancelar_pop2);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean bool = db.deleteBoleia(id);
                if(bool)
                    Toast.makeText(showBoleia.this, "Boleia apagada!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(showBoleia.this, menu.class);
                intent.putExtra("Email", email);
                //intent.putExtra("Nome", str_nome);
                startActivity(intent);
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void createDialogPedirBoleia(int id, String mail, String user, String data, String hora, String origem, String destino){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_pede, null);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        //textView_user = (TextView) findViewById(R.id.textView_user);
        //textView_user.setText(user);
        button_confirm2 = (Button) popupView.findViewById(R.id.button_confirmar_pop);
        button_cancel2 = (Button) popupView.findViewById(R.id.button_cancelar_pop);

        button_confirm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long val = db.addPendente(id, mail, user, data, hora, origem, destino);
                if (val > 0)
                    Toast.makeText(showBoleia.this, "O pedido de boleia foi enviado!", Toast.LENGTH_LONG).show();
            }
        });

        button_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}