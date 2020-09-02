package com.example.myapplication.Parser;

import android.content.Context;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;

public class ParserHelper {
    private static final String DONGA = "donga", JOONGANG ="joongang";

    private ParserHelper(){
    }

    public static void addComponent(ViewGroup vg , Context c, String kind, String article){
        if (kind.equals(DONGA)) {
            new DongaParser(c,vg,article).parsing();
        }else if(kind.equals(JOONGANG)){
            new JoongangParser(c, vg, article).parsing();
        }
    }
}
