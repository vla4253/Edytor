package com.example.vladyslav.edytor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Settings extends AppCompatActivity
{
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    RadioButton internal;
    RadioButton external;
    EditText path;
    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preference = getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = preference.edit();
        internal = (RadioButton) findViewById(R.id.internal);
        external = (RadioButton) findViewById(R.id.external);
        path = (EditText) findViewById(R.id.path);
        name = (EditText) findViewById(R.id.name);
        if(!(preference.getBoolean("external", true)))
        {
            external.setVisibility(View.INVISIBLE);
        }
        else
        {
            external.setVisibility(View.VISIBLE);
        }
        if(!(preference.getBoolean("memory", false)))
        {
            internal.setChecked(true);
        }
        else
        {
            external.setChecked(true);
        }
        if(preference.getString("path", "") != "")
        {
            path.setText(preference.getString("path", ""));
        }
        name.setText(preference.getString("name", "notatka.txt"));
    }
    protected void onStop()
    {
        super.onStop();
        if(internal.isChecked())
        {
            editor.putBoolean("memory", false);
        }
        if(external.isChecked())
        {
            editor.putBoolean("memory", true);
        }
        if(path.getText().toString() != "")
        {
            editor.putString("path", path.getText().toString());
        }
        if(name.getText().toString().equals(""))
        {
            editor.putString("name", "notatka.txt");
        }
        else {
            editor.putString("name", name.getText().toString());
        }
        editor.commit();
    }
}
