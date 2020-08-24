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

public class Parser extends AsyncTask<Void, Void, List<articleVO>> {
    private String FILE = "jsons/";
    private String index;
    private String title;
    private int state;
    private List<articleVO> list;
    private Context context;
    private RecycleAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    public Parser(String title, int state, List<articleVO> list, Context context, RecycleAdapter adapter) {
        this.title = title;
        this.state = state;
        this.list = list;
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<articleVO> articleVOS) {
        adapter.setList(articleVOS);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<articleVO> doInBackground(Void... voids) {
        parsing(title, 0, context);
        return list;
    }

    public void setInit(RecyclerView recyclerView, ProgressBar progressBar) {
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
    }

    public void parsing(String title, int state, Context context) {
        AssetManager manager = context.getResources().getAssets();

        try {
            //파일 불러오기
            FILE += title + ".json";
            InputStream is = manager.open(FILE);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = br.readLine();

            while (line != null) {
                buffer.append(line);
                line = br.readLine();
            }

            String jsonData = buffer.toString();
            JSONObject object = new JSONObject(jsonData).getJSONObject(title);
            index = object.getString("index");

            parse_main(object);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //메인 파싱
    private void parse_main(JSONObject jsonObject) throws IOException, JSONException {
        JSONObject object = jsonObject.getJSONObject("main");
        String main_class = object.getString("main_class");
        String main_container_sub_class = object.getString("main_container_sub_class");
        String title = object.getString("title");
        String article_photo = object.getString("article_photo");
        String article_txt = object.getString("article_txt");

        Document document = Jsoup.connect(index).get();

        Elements elements = document.select(main_class);
        Elements core = elements.select(main_container_sub_class);

        list = new ArrayList<>();
        for (Element e : core) {
            String link = e.select("a").attr("href");

            Document sub_doc = Jsoup.connect(link).get();
            String sub_title = sub_doc.select(title).text();
            String sub_content = sub_doc.select(article_txt).text();
            String sub_img = sub_doc.select(article_photo).select("img").attr("src");

            list.add(new articleVO(sub_img, link, sub_title, sub_content));

            break;
        }

    }

}
