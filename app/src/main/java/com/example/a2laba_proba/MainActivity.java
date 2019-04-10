package com.example.a2laba_proba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<State> states = new ArrayList();
    DBclass dbHelper;
    EditText etName;
    ListView countriesList;
    ViewGroup m_my_list;
    Button btnshow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final StateAdapter stateAdapter = new StateAdapter(this, R.layout.list_item, states);
        dbHelper = new DBclass(this);
        etName = (EditText) findViewById(R.id.etName);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Button btnOk = (Button) findViewById(R.id.button5);
        final CheckBox checkb=(CheckBox) findViewById(R.id.checkBox1);
        View.OnClickListener oclBtnOk=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                if(checkb.isChecked()) {


                    String name = etName.getText().toString();
                    //to convert Date to String, use format method of SimpleDateFormat class.
                    String strDate = dateFormat.format(date);
                    states.add(new State(name, strDate, R.drawable.file));
                    Toast.makeText(getApplicationContext(), "file was added ",
                            Toast.LENGTH_SHORT).show();

                    stateAdapter.notifyDataSetChanged();


                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBclass.KEY_NAME, name);
                    contentValues.put(DBclass.KEY_DATE, strDate);
                    database.insert(DBclass.TABLE_CONTACTS, null, contentValues);



                }
                else{
                    Toast.makeText(getApplicationContext(), "Turn on THE SUDO mode",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnOk.setOnClickListener(oclBtnOk);




        btnshow = (Button) findViewById(R.id.btnShow);
        View.OnClickListener addBtnOk=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = database.query(DBclass.TABLE_CONTACTS, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBclass.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBclass.KEY_NAME);
                    int dateIndex = cursor.getColumnIndex(DBclass.KEY_DATE);
                    do {
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", Date = " + cursor.getString(dateIndex));
                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog","0 rows");

                cursor.close();
            }
        };
        btnshow.setOnClickListener(addBtnOk);







        // начальная инициализация списка
        setInitialData();
        // получаем элемент ListView
        countriesList = (ListView) findViewById(R.id.countriesList);
        // создаем адаптер
        // устанавливаем адаптер
        countriesList.setAdapter(stateAdapter);
        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // получаем выбранный пункт
                State selectedState = (State)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Был выбран пункт " + selectedState.getName(),
                        Toast.LENGTH_SHORT).show();
                states.remove(states.indexOf(selectedState));

                stateAdapter.notifyDataSetChanged();
            }
        };
        countriesList.setOnItemClickListener(itemListener);





    }


    public void onClick(View v) {





    }



        private void setInitialData(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(date);

        states.add(new State ("labotarorna 1", strDate, R.drawable.ic_menu_share));
        states.add(new State ("Nakaz", strDate, R.drawable.ic_menu_share));
        states.add(new State ("RGR", strDate, R.drawable.ic_menu_share));
        states.add(new State ("Graphics", strDate, R.drawable.ic_menu_share));
        states.add(new State ("Mats", strDate, R.drawable.ic_menu_share));

    }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_file) {
            // show current file distribution
        } else if (id == R.id.nav_history) {
            //show order history

        }  else if (id == R.id.nav_settings) {
            //show settings = using payment methods

        } else if (id == R.id.nav_share) {
            // share with facebook instagram

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
