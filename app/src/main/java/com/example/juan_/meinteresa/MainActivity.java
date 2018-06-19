package com.example.juan_.meinteresa;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juan_.meinteresa.constantes.Sentencias;
import com.google.android.gms.maps.model.LatLng;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText campoTitulo, campoDesc, campoLat,campoLong;
    double latitude=0, longitude=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);











        campoDesc = findViewById(R.id.editTextDesc);
        campoTitulo = findViewById(R.id.textTitulo);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(campoTitulo==null || campoTitulo.getText().toString().trim().isEmpty()){
                  Snackbar.make(view, "El Titulo es obligatorio", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

              }else {
                  registrar();
                  campoDesc.setText("");
                  campoTitulo.setText("");
              }
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"db_ubicacion",null,1);
    }
    public String setFechaActual()
    {

        final Calendar c = Calendar.getInstance();

        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = formatter.format(c.getTime());

        return s;
    }
    private void registrar() {

        final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }else{
            LocationManager locationManager;
            locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
            Location location =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){
                 latitude = location.getLatitude();
                 longitude = location.getLongitude();
                LatLng ubicacion = new LatLng(latitude, longitude);

            }else{
                Toast.makeText(getApplicationContext(),"Ha ocurrido un error compruebe si tiene la ubicacion activada" ,Toast.LENGTH_SHORT).show();
            }}
        if(longitude!=0 && latitude !=0){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"db_ubicacion",null,1);
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Sentencias.campoDesc,campoDesc.getText().toString());
        cv.put(Sentencias.campoLatitud,latitude);
        cv.put(Sentencias.campoLongitud,longitude);
        cv.put(Sentencias.campoTitulo,campoTitulo.getText().toString());
        cv.put(Sentencias.campoFecha,setFechaActual());
        Long idResult=db.insert(Sentencias.tablaNombreUb, Sentencias.campoID,cv);
        longitude=0;
        latitude=0;

      if(idResult!=null && idResult !=0){  Toast.makeText(getApplicationContext(),"Registrado con exito en la base de datos... ID: "+idResult ,Toast.LENGTH_SHORT).show();}

        db.close();
    }}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit) {
            Intent inten = new Intent(MainActivity.this,Editar.class);
            startActivity(inten);
        } else if (id == R.id.nav_map) {
            Intent inten = new Intent(MainActivity.this,MapsActivity.class);
            startActivity(inten);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
