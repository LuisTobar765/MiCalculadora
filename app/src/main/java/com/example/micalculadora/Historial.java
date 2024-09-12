package com.example.micalculadora;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Historial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial);

        ListView listaHistorial = findViewById(R.id.ListaHistorial);
        Button btnVolver = findViewById(R.id.btnVolver);

        ArrayList<String> history = getIntent().getStringArrayListExtra("history");

        if (history != null && !history.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, history);
            listaHistorial.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No hay historial", Toast.LENGTH_SHORT).show();
        }

        btnVolver.setOnClickListener(v -> finish());
    }
}
