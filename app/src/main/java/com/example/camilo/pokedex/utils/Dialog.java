package com.example.camilo.pokedex.utils;

import android.app.Activity;
import android.os.Bundle;

import com.example.camilo.pokedex.R;

public class Dialog extends android.app.Dialog{

    public Activity c;

    public Dialog(Activity a) {
        super(a);
        this.c=a;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
    }
}
