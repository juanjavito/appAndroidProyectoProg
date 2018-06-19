package com.example.juan_.meinteresa;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.juan_.meinteresa.constantes.Sentencias;
import com.example.juan_.meinteresa.MapsActivity;
import com.example.juan_.meinteresa.entidad.Ubicacion;

import java.util.ArrayList;

public class Editar extends AppCompatActivity {

    Spinner spiner;
    ArrayList<String> listaTitulos;
    ArrayList<Ubicacion> ubicaciones;
    ConexionSQLiteHelper conn;
    TextView textLong,textLat,textDesc,textFecha;
    EditText textTitulo,textEditTextDesc;
    Button borrar,editar;
    Button mapear;
    ListView lv;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        //spiner = (Spinner) findViewById(R.id.spinner);
        textLong = (TextView) findViewById(R.id.textLong);
        textLat = (TextView) findViewById(R.id.textLat);
        textDesc = (TextView) findViewById(R.id.textDesc);
        textFecha = (TextView) findViewById(R.id.textFecha);
        borrar = (Button) findViewById(R.id.buttonEliminar);
        mapear = findViewById(R.id.buttonMapear);
        textTitulo = findViewById(R.id.editTextTitulo);
        textEditTextDesc = findViewById(R.id.editTextDesc);
        editar = findViewById(R.id.buttonEditar);
        lv = findViewById(R.id.listView);

        conn = new ConexionSQLiteHelper(this, "db_ubicacion", null, 1);


        consultarLista();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaTitulos);

       // spiner.setAdapter(adaptador);
        lv.setAdapter(adaptador);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int posicion, long id) {




                    textLong.setText(Double.toString(ubicaciones.get(posicion).getLongitud()));
                    textLat.setText(Double.toString(ubicaciones.get(posicion).getLatitud()));
                    textEditTextDesc.setText(ubicaciones.get(posicion ).getDescripcion());
                    textFecha.setText(ubicaciones.get(posicion).getFecha());
                    textTitulo.setText(ubicaciones.get(posicion).getTitulo());
                    mapear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent inten = new Intent(Editar.this,MapearActivity.class);

                            inten.putExtra("latitud",ubicaciones.get(posicion).getLatitud());
                            inten.putExtra("longitud",ubicaciones.get(posicion).getLongitud());
                            inten.putExtra("titulo",ubicaciones.get(posicion).getTitulo());

                            startActivity(inten);

                            finish();
                        }
                    });
                    editar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(textTitulo==null || textTitulo.getText().toString().trim().isEmpty()){
                                Snackbar.make(view, "El Titulo es obligatorio", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }else{
                                SQLiteDatabase dbM = conn.getWritableDatabase();


                                String[] parametroID = {ubicaciones.get(posicion).getId().toString()};
                                ContentValues values = new ContentValues();
                                values.put(Sentencias.campoTitulo,textTitulo.getText().toString());
                                values.put(Sentencias.campoDesc,textEditTextDesc.getText().toString());

                                int up= dbM.update(Sentencias.tablaNombreUb,values,Sentencias.campoID+"=?",parametroID);
                                dbM.close();
                                if(up!=-1){Toast.makeText(getApplicationContext(),"Actualizado correctamente" ,Toast.LENGTH_SHORT).show();}
                                else{Toast.makeText(getApplicationContext(),"Hubo un error al actualizar los datos" ,Toast.LENGTH_SHORT).show();

                                }

                                Intent inten = new Intent(Editar.this,Editar.class);
                                startActivity(inten);
                                finish();
                            }}
                    });
                    borrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Editar.this);
                            dialogo1.setTitle("Importante");
                            dialogo1.setMessage("¿ Esta seguro que desea eliminar " + ubicaciones.get(posicion).getTitulo() + "?");
                            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int j) {
                                    String st = ubicaciones.get(posicion).getId().toString();

                                    String[] parametro = {st};
                                    int up = db.delete(Sentencias.tablaNombreUb, Sentencias.campoID + "=?", parametro);
                                    if (up != -1) {
                                        Toast.makeText(getApplicationContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Hubo un error al borrar los datos", Toast.LENGTH_SHORT).show();

                                    }


                                    Intent inten = new Intent(Editar.this, Editar.class);
                                    startActivity(inten);
                                    finish();
                                }
                            });
                            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int j) {

                                }
                            });
                            dialogo1.show();

                        }

                    });

            }
        });
//        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, final int posicion, long id) {
//                if(posicion!=0) {
//                    textLong.setText(Double.toString(ubicaciones.get(posicion - 1).getLongitud()));
//                    textLat.setText(Double.toString(ubicaciones.get(posicion - 1).getLatitud()));
//                    textEditTextDesc.setText(ubicaciones.get(posicion - 1).getDescripcion());
//                    textFecha.setText(ubicaciones.get(posicion - 1).getFecha());
//                    textTitulo.setText(ubicaciones.get(posicion-1).getTitulo());
//                    mapear.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent inten = new Intent(Editar.this,MapearActivity.class);
//
//                            inten.putExtra("latitud",ubicaciones.get(posicion - 1).getLatitud());
//                            inten.putExtra("longitud",ubicaciones.get(posicion - 1).getLongitud());
//                            inten.putExtra("titulo",ubicaciones.get(posicion - 1).getTitulo());
//
//                            startActivity(inten);
//
//                            finish();
//                        }
//                    });
//                    editar.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if(textTitulo==null || textTitulo.getText().toString().trim().isEmpty()){
//                                Snackbar.make(view, "El Titulo es obligatorio", Snackbar.LENGTH_LONG)
//                                        .setAction("Action", null).show();
//
//                            }else{
//                            SQLiteDatabase dbM = conn.getWritableDatabase();
//
//
//                            String[] parametroID = {ubicaciones.get(posicion-1).getId().toString()};
//                            ContentValues values = new ContentValues();
//                            values.put(Sentencias.campoTitulo,textTitulo.getText().toString());
//                            values.put(Sentencias.campoDesc,textEditTextDesc.getText().toString());
//
//                           int up= dbM.update(Sentencias.tablaNombreUb,values,Sentencias.campoID+"=?",parametroID);
//                            dbM.close();
//                            if(up!=-1){Toast.makeText(getApplicationContext(),"Actualizado correctamente" ,Toast.LENGTH_SHORT).show();}
//                            else{Toast.makeText(getApplicationContext(),"Hubo un error al actualizar los datos" ,Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            Intent inten = new Intent(Editar.this,Editar.class);
//                            startActivity(inten);
//                            finish();
//                        }}
//                    });
//                    borrar.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            String st =ubicaciones.get(posicion-1).getId().toString();
//
//                            String[] parametro = {st};
//                           int up= db.delete(Sentencias.tablaNombreUb, Sentencias.campoID+"=?",parametro);
//                            if(up!=-1){Toast.makeText(getApplicationContext(),"Eliminado correctamente" ,Toast.LENGTH_SHORT).show();}
//                            else{Toast.makeText(getApplicationContext(),"Hubo un error al borrar los datos" ,Toast.LENGTH_SHORT).show();
//
//                            }
//
//
//                            Intent inten = new Intent(Editar.this,Editar.class);
//                            startActivity(inten);
//                          finish();
//
//                        }
//                    });
//                }else{
//                    textLong.setText("");
//                    textLat.setText("");
//                    textEditTextDesc.setText("Descripcion ");
//                    textFecha.setText("");
//                    textTitulo.setText("Titulo");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    private void consultarLista() {
         db= conn.getReadableDatabase();
        Ubicacion ubicacion = null;

        ubicaciones=new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Sentencias.tablaNombreUb,null);


        while(cursor.moveToNext()){
            ubicacion = new Ubicacion();
            ubicacion.setId(cursor.getInt(0));
            ubicacion.setLatitud(cursor.getDouble(1));
            ubicacion.setLongitud(cursor.getDouble(2));
            ubicacion.setTitulo(cursor.getString(4));
            ubicacion.setDescripcion(cursor.getString(5));
            ubicacion.setFecha(cursor.getString(3));

            ubicaciones.add(ubicacion);
        }
        obtenerLista();
    }
    private void obtenerLista(){
        listaTitulos = new ArrayList<String>();


        for (int i=0;i<ubicaciones.size();i++){

            listaTitulos.add(ubicaciones.get(i).getTitulo());
        }

    }
}
