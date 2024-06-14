package com.example.ejemplo1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import Configuracion.Personas;
import Configuracion.SQLiteConexion;
import Configuracion.Trans;

public class ActivityCombo extends AppCompatActivity {

    SQLiteConexion conexion;
    Spinner combopersonas;
    EditText nombres, apellidos, correo;

    ArrayList<Personas> lista;
    ArrayList<String> Arreglo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_combo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //CONEXION A BD
        conexion = new SQLiteConexion(this, Trans.DBname, null, Trans.Version);
        combopersonas = (Spinner) findViewById(R.id.spinner);
        nombres = (EditText) findViewById(R.id.cbnombre);
        apellidos = (EditText) findViewById(R.id.cbapellido);
        correo = (EditText) findViewById(R.id.cbcorreo);

        ObtenerInfo();
    }

    private void ObtenerInfo() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Personas persona = null;
        lista = new ArrayList<>();

        Cursor cursor = db.rawQuery(Trans.SelectAllPerson, null);

        while (cursor.moveToNext()) {
            persona = new Personas();
            persona.setId(cursor.getInt(0));
            persona.setNombres(cursor.getString(1));
            persona.setApellidos(cursor.getString(2));
            persona.setCorreo(cursor.getString(4));

            lista.add(persona);
        }

        cursor.close();

        FillData();
    }
    private void FillData() {
        Arreglo = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            Arreglo.add(lista.get(i).getId() + " " +
                    lista.get(i).getNombres() + " " +
                    lista.get(i).getApellidos() + " " +
                    lista.get(i).getCorreo());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arreglo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combopersonas.setAdapter(adapter);

        combopersonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Personas selectedPerson = lista.get(position);
                nombres.setText(selectedPerson.getNombres());
                apellidos.setText(selectedPerson.getApellidos());
                correo.setText(selectedPerson.getCorreo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }
}