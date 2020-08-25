package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DataVO.articleVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class Parser extends AsyncTask<Object, List<articleVO>, List<articleVO>> {
    private String FILE = "jsons/";
    private String title;
    private Context context;
    private final int TODAY = 0;
    private RecycleAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private final int FIX = 5;

    public Parser(String title, Context context, RecycleAdapter adapter,RecyclerView recyclerView, ProgressBar progressBar) {
        this.title = title;
        this.context = context;
        this.adapter = adapter;
        this.progressBar =progressBar;
        this.recyclerView= recyclerView;
    }



    @Override
    protected void onProgressUpdate(List<articleVO>... values) {
        super.onProgressUpdate(values);
        Log.d("jms","progressUpdate");
        adapter.setList(values[0]);
        adapter.setSize(values[0].size());
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<articleVO> doInBackground(Object... objects) {
        int state = (int) objects[0];
        List<articleVO> list = (List<articleVO>) objects[1];
        JSONObject object = read_JSON_File();
        String base_URL = null;

        try {
            base_URL = object.getString("index");
            choose_state(object, state, list, base_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    //고른 아이콘에 맞는 크롤링
    private void choose_state(JSONObject head, int state, List<articleVO> list, String base_URL) {
        JSONObject body = null;
        try {
            if (state == TODAY) {
                body = head.getJSONObject("today");
                today_crawling(list, body, base_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void today_crawling(List<articleVO> list, JSONObject body, String base_URL) throws IOException, JSONException {
        String today_class = body.getString("today_class");
        String today_title = body.getString("today_title");
        String today_article_photo = body.getString("today_article_photo");
        String today_article_txt = body.getString("today_article_txt");

        Document document = Jsoup.connect(base_URL).get();
        Elements elements = document.select(today_class);

        for (Element e : elements) {
            Log.d("jms",title);
            String link = e.select("a").attr("href");
            Document temp_document = Jsoup.connect(link).get();
            String img_url = temp_document.select(today_article_photo).attr("src");
            String title = temp_document.select(today_title).text();
            String content = temp_document.select(today_article_txt).text();

            list.add(new articleVO(img_url,link,title,content));

            if(list.size() == FIX){
                publishProgress(list);
            }
        }

    }

    //JSON 파일을 읽어 들인다
    private JSONObject read_JSON_File() {
        AssetManager manager = context.getAssets();
        JSONObject ret = null;
        try {
            FILE += title + ".json";
            InputStream is = manager.open(FILE);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            ret = new JSONObject(sb.toString()).getJSONObject(title);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
