package com.example.myapplication.Parser;

import android.content.Context;
import android.text.Spanned;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.ParameterizedType;

public class ParserHelper {
    private static final String DONGA = "donga", JOONGANG ="joongang", KUKMIN = "kukmin", KHAN = "khan"
            , YTN = "ytn";

    private ParserHelper(){
    }

    //todo 개선 필
    public static void attachCompoent(LinearLayout v, String kind, String article){
        if (kind.equals(DONGA)) {
            new DongaParser(v, article).init();
        }else if(kind.equals(JOONGANG)){
            new JoongangParser(v,article).init();
        }else if(kind.equals(KUKMIN))
            new KukminParser(v, article).init();
        else if(kind.equals(YTN))
            new YtnParser(v,article).init();
        else if(kind.equals(KHAN))
            new KhanParser(v,article).init();
    }
}
