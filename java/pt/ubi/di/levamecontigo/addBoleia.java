package pt.ubi.di.levamecontigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class addBoleia extends AppCompatActivity {

    EditText categoria;
    EditText origem;
    EditText destino;
    EditText lugares;
    EditText contribuicao;
    EditText data;
    EditText hora;
    Button save;
    DatePicker data_picker;
    databaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boleia);

        db = new databaseHelper(this);

        categoria = (EditText) findViewById(R.id.categoria);
        origem = (EditText) findViewById(R.id.origem);
        destino = (EditText) findViewById(R.id.destino);
        lugares = (EditText) findViewById(R.id.lugares);
        contribuicao = (EditText) findViewById(R.id.contribuicao);
        data = (EditText) findViewById(R.id.data);
        hora = (EditText) findViewById(R.id.hora);
        save = (Button) findViewById(R.id.save);
        String email = getIntent().getStringExtra("Email");
        String nome = getIntent().getStringExtra("Nome");
        //Toast.makeText(addBoleia.this, "Email: " + email + "\nNome: " + nome, Toast.LENGTH_SHORT).show();


        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String str_categoria = categoria.getText().toString().trim();
                String str_origem = origem.getText().toString().trim();
                String str_destino = destino.getText().toString().trim();
                String i_lug = lugares.getText().toString().trim();
                float f_cont = Float.parseFloat(contribuicao.getText().toString().trim());
                String str_data = data.getText().toString().trim();
                String str_hora = hora.getText().toString().trim();

                int day = Integer.parseInt(str_data.split("/")[2]);
                int month = Integer.parseInt(str_data.split("/")[1]);
                int year = Integer.parseInt(str_data.split("/")[0]);

                int horas = Integer.parseInt(str_hora.split(":")[0]);
                int minutos = Integer.parseInt(str_hora.split(":")[1]);

                if (year >= 2020) {
                    if (year == 2020) {
                        if (month == 12) {
                            if (day > 10 && day < 31) {
                                if (horas >= 0 && horas < 24) {
                                    if (minutos >= 0 && minutos < 60) {
                                        if (str_categoria.equalsIgnoreCase("Pedido") || str_categoria.equalsIgnoreCase("Oferta")) {
                                            String temp = email.split("@")[0];
                                            long val = db.addBoleia(str_categoria, temp, str_data, str_hora, str_origem, str_destino, i_lug, f_cont);
                                            if (val > 0) {
                                                Toast.makeText(addBoleia.this, "Event added successfully!", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(addBoleia.this, "Email: " + email + "Data: " + str_data + " " + str_hora + "Origem: " + str_origem + "\nDestino: " + str_destino + "\nLugares: " + i_lug + "\nContribuição: " + f_cont, Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(addBoleia.this, "Pedido ou categoria is wrong!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(addBoleia.this, "Invalid time!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(addBoleia.this, "Invalid time!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(addBoleia.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(addBoleia.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (year > 2020) {
                        if (month > 12 || month < 0) {
                            Toast.makeText(addBoleia.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (day > 31 || day < 0) {
                                Toast.makeText(addBoleia.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (horas >= 0 && horas < 24) {
                                    if (minutos >= 0 && minutos < 60) {
                                        if (str_categoria.equalsIgnoreCase("Pedido") || str_categoria.equalsIgnoreCase("Oferta")) {
                                            String temp = email.split("@")[0];
                                            long val = db.addBoleia(str_categoria, temp, str_data, str_hora, str_origem, str_destino, i_lug, f_cont);
                                            if (val > 0) {
                                                Toast.makeText(addBoleia.this, "Event added successfully!", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(addBoleia.this, "Email: " + email + "Data: " + str_data + " " + str_hora + "Origem: " + str_origem + "\nDestino: " + str_destino + "\nLugares: " + i_lug + "\nContribuição: " + f_cont, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(addBoleia.this, "Pedido ou categoria is wrong!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(addBoleia.this, "Invalid time!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(addBoleia.this, "Invalid time!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                    else
                        Toast.makeText(addBoleia.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


