package com.example.myapplication.Parser;

import android.content.Context;
import android.text.Spanned;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.ParameterizedType;

public class ParserHelper {
    private static final String DONGA = "donga", JOONGANG ="joongang";

    private ParserHelper(){
    }

    //todo 개선 필
    public static Spanned makeHtml(LinearLayout v, String kind, String article){
        if (kind.equals(DONGA)) {
            return new DongaParser(v, article).parsing();
        }else if(kind.equals(JOONGANG)){
        }

        return null;
    }
}
