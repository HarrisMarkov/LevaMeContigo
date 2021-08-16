package pt.ubi.di.levamecontigo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class boleiasCombinadas extends AppCompatActivity {

    databaseHelper db;
    ListView listView_boleias_combinadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boleias_combinadas);

        db = new databaseHelper(this);
        listView_boleias_combinadas = (ListView) findViewById(R.id.listView_boleias_combinadas);

        String email = getIntent().getStringExtra("Email");
        //Toast.makeText(boleiasCombinadas.this, "Email: " + email, Toast.LENGTH_SHORT).show();
        ArrayList<String> theList = new ArrayList<String>();
        Cursor cursor = db.getBoleiasCombinadasOferece(email);

        if(cursor.getCount() == 0){
            //Toast.makeText(boleiasCombinadas.this, "ListView update failed!", Toast.LENGTH_SHORT).show();
            cursor = db.getBoleiasCombinadasPede(email);
            if(cursor.getCount() == 0)
                Toast.makeText(boleiasCombinadas.this, "ListView update failed!", Toast.LENGTH_SHORT).show();
            else{
                while(cursor.moveToNext()){
                    String str = "";
                    str = "\nOFERTA de: " + cursor.getString(2) + "\n" + cursor.getString(4) + " - " + cursor.getString(5) + "\n" + cursor.getString(6) + " -> " + cursor.getString(7) + "\nLugares: " + cursor.getString(8) + "\n€ " + cursor.getString(9)+ "\n";

                    theList.add(str);
                    ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                    listView_boleias_combinadas.setAdapter(listAdapter);
                }
            }
        } else {
            while (cursor.moveToNext()){
                String str = "";
                if(cursor.getString(2).equalsIgnoreCase(email)){
                    str = "\nPEDIDO de: " + cursor.getString(3) + "\n" + cursor.getString(4) + " - " + cursor.getString(5) + "\n" + cursor.getString(6) + " -> " + cursor.getString(7) + "\n";
                }
                else if(cursor.getString(3).equalsIgnoreCase(email)){ // OFERECE BOLEIA
                    //str = "\nOFERTA de: " + cursor.getString(2) + "\n" + cursor.getString(4) + " - " + cursor.getString(5) + "\n" + cursor.getString(6) + " -> " + cursor.getString(7) + "\nLugares: " + cursor.getString(8) + "\n€ " + cursor.getString(9)+ "\n";
                }

                theList.add(str);
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                listView_boleias_combinadas.setAdapter(listAdapter);
            }
        }

    }
}