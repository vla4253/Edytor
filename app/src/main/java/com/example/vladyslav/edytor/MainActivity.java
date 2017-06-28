package com.example.vladyslav.edytor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity
{
    EditText text;
    FileOutputStream OPS;
    FileInputStream FIS;
    String line;
    String message;
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    File notatkaTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.text);
        preference = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putString("path", "");
        editor.putString("name", "notatka.txt");
        message = "";
        if(isExternalStorageWritable())
        {
            editor.putBoolean("external", true);
        }
        else
        {
            editor.putBoolean("external", false);
            editor.putBoolean("memory", false);
        }
    }
    public boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        return false;
    }
    public void write(View v)
    {
        message = text.getText().toString();
        if(!(preference.getBoolean("memory", false)))
        {
            try
            {
                OPS = openFileOutput(preference.getString("name", "notatka.txt"), Context.MODE_PRIVATE);
                OPS.write(message.getBytes());
                OPS.close();
                Toast.makeText(getApplicationContext(), "Dane zostały zapisane do pliku '" + preference.getString("name", "notatka.txt") + "' w pamięci wewnétrznej", Toast.LENGTH_LONG).show();
            } catch (IOException error)
            {

            }
        }
        else
        {
            if(isExternalStorageWritable()) {
                File externalStorage = Environment.getExternalStorageDirectory();
                if (preference.getString("path", "none") != "") {
                    File dir = new File(externalStorage.getAbsolutePath() + "/" + preference.getString("path", ""));
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    notatkaTxt = new File(dir, preference.getString("name", "notatka.txt"));
                    try {
                        OPS = new FileOutputStream(notatkaTxt);
                    } catch (FileNotFoundException error) {

                    }
                } else {
                    notatkaTxt = new File(externalStorage, preference.getString("name", "notatka.txt"));
                    try {
                        OPS = new FileOutputStream(notatkaTxt);
                    } catch (FileNotFoundException error) {

                    }
                }
                try {
                    OPS.write(message.getBytes());
                    OPS.close();
                    Toast.makeText(getApplicationContext(), "Dane zostały zapisane do pliku '" + preference.getString("name", "notatka.txt") + "' w pamięci zewnętrznej", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException error) {

                } catch (IOException error) {

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Pamięć zewnętrzna nie jest dostępna", Toast.LENGTH_LONG).show();
                editor.putBoolean("external", false);
                editor.putBoolean("memory", false);
            }
        }
        text.setText("");
        message = "";
    }
    public void read(View v)
    {
        if(!(preference.getBoolean("memory", false)))
        {
            try
            {
                FileInputStream read = openFileInput(preference.getString("name", "notatka.txt"));
                InputStreamReader ISR = new InputStreamReader(read);
                BufferedReader BR = new BufferedReader(ISR);
                while ((line = BR.readLine()) != null) {
                    message = message + line + "\n";
                }
                text.setText(message.toString());
                Toast.makeText(getApplicationContext(), "Dane zostały odczytane z pliku '" + preference.getString("name", "notatka.txt") +"' w pamięci wewnętrznej", Toast.LENGTH_LONG).show();
                message = "";
                BR.close();
                ISR.close();
                read.close();
            } catch (FileNotFoundException error) {

            } catch (IOException error) {

            }
        }
        else
        {
            if(isExternalStorageWritable()) {
                File externalStorage = Environment.getExternalStorageDirectory();
                if (preference.getString("path", "") != "") {
                    File dir = new File(externalStorage.getAbsolutePath() + "/" + preference.getString("path", ""));
                    notatkaTxt = new File(dir, preference.getString("name", "notatka.txt"));
                    try {
                        FIS = new FileInputStream(notatkaTxt);
                    } catch (FileNotFoundException error) {

                    }
                } else {
                    notatkaTxt = new File(externalStorage, preference.getString("name", "notatka.txt"));
                }
                try {
                    FIS = new FileInputStream(notatkaTxt);
                    InputStreamReader ISR = new InputStreamReader(FIS);
                    BufferedReader BR = new BufferedReader(ISR);
                    while ((line = BR.readLine()) != null) {
                        message = message + line + "\n";
                    }
                    text.setText(message.toString());
                    Toast.makeText(getApplicationContext(), "Dane zostały odczytane z pliku '" + preference.getString("name", "notatka.txt") + "' w pamięci zewnętrznej", Toast.LENGTH_LONG).show();
                    message = "";
                    BR.close();
                    ISR.close();
                    FIS.close();
                } catch (FileNotFoundException error) {

                } catch (IOException error) {

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Pamięć zewnętrzna nie jest dostępna", Toast.LENGTH_LONG).show();
                editor.putBoolean("external", false);
                editor.putBoolean("memory", false);
            }
        }
    }
    public void clear(View v)
    {
        text.setText("");
        Toast.makeText(getApplicationContext(), "Pole tekstowe zostało wyczyszczone", Toast.LENGTH_LONG).show();
    }
    public void close(View v)
    {
        finish();
    }
    public void settings(View v)
    {
        Intent settings = new Intent(this, Settings.class);
        startActivity(settings);
    }

}
