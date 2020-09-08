package com.example.myapplication.Parser;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlParser {
    private LinearLayout linearLayout;
    private String text;
    private List<Map<String,String>> lists;
    private int idx = 0;

    public HtmlParser(LinearLayout linearLayout, String text){
        this.linearLayout = linearLayout;
        lists = new ArrayList<>();

        text = text.replaceAll("<br>","\n");
        text = text.replaceAll("\n \n", "\n");
        text = text.replaceAll("\n\n","\n");

        this.text = text;
    }

    //전처리 과정
    public List<Map<String,String>> preprocessing(){
        while(idx != -1){
            char current = text.charAt(idx);

            if(current == '<'){
                String tag = getTagAttribute();

                if(tag == null || tag.equals("<br>")) {
                    continue;
                }
                String content = getContent();
                makeTagMap(tag,content);
                continue;
            }else{
                String content = getContent().trim();

                if(!content.isEmpty()) {
                    Map<String, String> temp = new HashMap<>();

                    temp.put("tag","text");
                    temp.put("content", content);

                    lists.add(temp);
                }
            }
        }


        return lists;
    }

    //태그에 맞게 속성들을 나눠 Map을 만드는 메소드드
    private void makeTagMap(String tag, String content){
        Map<String,String> temp = new HashMap<>();
        getAttribute(temp,tag,content);
        lists.add(temp);
    }

    private void getAttribute(Map<String,String> map, String tag, String content){
        tag = tag.trim();
        tag = tag.substring(1, tag.length()-1);

        String split[] = tag.split(" ");
        String tag_name = null;
        for(String s : split){
            if(!s.contains("=")){
                tag_name = s;
            }else{
                String inner_split[] = s.split("=");

                String attr_name = inner_split[0];
                String attr_source = inner_split[1];
                attr_source = attr_source.replaceAll("\"","");

                map.put(attr_name,attr_source);
            }
        }

        map.put("content",content);
        map.put("tag",tag_name);

    }

    //태그 안의 내용을 얻는 메소드
    private String getContent(){
        int end = text.indexOf("<",idx);

        if(end == -1){
            String content = text.substring(idx,text.length());
            idx = end;
            return content;
        }

        String content = text.substring(idx,end);
        idx = end;
        return content;
    }

    //태그와 속성을 얻는 메소드
    private String getTagAttribute(){
        int end = text.indexOf(">",idx)+1;

        String tag = text.substring(idx,end);

        idx = end;
        if(tag.contains("</"))
            return null;

        return tag;
    }
}
