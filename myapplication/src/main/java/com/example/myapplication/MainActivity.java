package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    private int idx = 0;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout)findViewById(R.id.container);
        String text ="<div id=\"article_body\" itemprop=\"articleBody\" class=\"article_body mg fs4\"> \n" +
                " <div class=\"ab_photo photo_center \" style=\"width: 560px;\"> \n" +
                "  <div class=\"image\"> \n" +
                "   <img alt=\"31일 광주광역시 동구 조선대학교병원 앞에서 의과대학 학생이 의대 정원 확충 및 공공의대 신설에 반대하는 1인 시위를 하고 있다. 프리랜서 장정필\" data-src=\"https://pds.joins.com/news/component/htmlphoto_mmdata/202008/31/cdc3c953-4266-4b92-ba1c-951d5501925e.jpg\" src=\"https://pds.joins.com/news/component/htmlphoto_mmdata/202008/31/cdc3c953-4266-4b92-ba1c-951d5501925e.jpg\" data-type=\"article\"> <span class=\"mask\"></span> \n" +
                "  </div> \n" +
                "  <p class=\"caption\">31일 광주광역시 동구 조선대학교병원 앞에서 의과대학 학생이 의대 정원 확충 및 공공의대 신설에 반대하는 1인 시위를 하고 있다. 프리랜서 장정필</p> \n" +
                " </div> ‘일하는전공의’ 운영자가 의사가 아니라는 의문이 제기되고 있다. 일하는전공의는 페이스북 계정이다.&nbsp;정부의 의대정원 확대 방침 등에 맞서 전공·전임의가 집단휴진(파업)을 이어가고 있는데 지난 29일 ‘이정도면 됐다’며 파업중단을 촉구하는&nbsp;글을 써 주목받았다. \n" +
                " <br> &nbsp; \n" +
                " <br> 일하는전공의는 “(의대정원 확대 등 정부가 추진 중인) 의료 정책에 있어 의사들 생각이 중요한 건 맞다”며 “그렇지만 13만 의사들의 의견이 정책에 얼마나 영향을 미치는 것이 옳은가”라고 소신 발언을 썼다. 이후 상당한 주목을 받았다.&nbsp; \n" +
                " <br> &nbsp; \n" +
                " <br> \n" +
                " <div class=\"ab_sub_heading\">\n" +
                "  <div class=\"ab_sub_headingline\">\n" +
                "   <h2> 쏟아진 제보&nbsp; </h2>\n" +
                "  </div>\n" +
                " </div> 하지만 정작 일하는전공의와 메신저로 대화에 나선 전공의 등은 기본적인 의학용어를 모르는 데다 심지어 중국식 표현까지 쓰고 있다고 그의 정체를 의심하고 있다. 대한의사협회에 관련 제보가 쏟아졌다. 이와 관련, 일하는전공의는 한 언론과의 화상 인터뷰를 통해 자신이 전공의가 맞다고 주장했다. 의사면허증도 공개했다고 한다.&nbsp; &nbsp; \n" +
                " <br> &nbsp; \n" +
                " <br> 31일 대한의사협회 등에 따르면 ‘일하는전공의’ 운영자는 정형외과를 전공한 것으로 보인다. 하지만 정작 손(의학용어 수부)에 대한 기초 해부학적 지식을 묻는 말에 계속 엉뚱한 답변을 내놨다고 한다.&nbsp; \n" +
                " <br> \n" +
                " <div class=\"ab_photo photo_center \" style=\"width: 550px;\"> \n" +
                "  <div class=\"image\"> \n" +
                "   <img alt=\"손바닥 뼈의 줄임말인 '호시탐탐'은 의대시험 단골출제다. 이에 본과1학년만 되도 다 알정도라고 한다. 하지만 '일하는전공의' 페이지 운영자는 엉뚱한 답을 했다. 자료 대한의사협회\" data-src=\"https://pds.joins.com/news/component/htmlphoto_mmdata/202008/31/7c78b0c9-8a87-40cb-aaec-f5041af6d71c.jpg\" data-type=\"article\"> <span class=\"mask\"></span> \n" +
                "  </div> \n" +
                "  <p class=\"caption\">손바닥 뼈의 줄임말인 '호시탐탐'은 의대시험 단골출제다. 이에 본과1학년만 되도 다 알정도라고 한다. 하지만 '일하는전공의' 페이지 운영자는 엉뚱한 답을 했다. 자료 대한의사협회</p> \n" +
                " </div> &nbsp; \n" +
                " <br> \n" +
                " <div class=\"ab_sub_heading\">\n" +
                "  <div class=\"ab_sub_headingline\">\n" +
                "   <h2> 본과 1학년이면 알 문제를 모른다?&nbsp; </h2>\n" +
                "  </div>\n" +
                " </div> ‘호시탐탐’(H·C·T·Tm) 관련해서다. 호시탐탐은 손바닥에 자리한 4개 뼈를 일컫는 용어다. 의과대학 시험 단골로 출제문제라 외우기 쉽게 앞글자만 딴 것이다. H·C·T·Tm 즉, 호시탐탐은 Hamate(유구골·갈고리 모양의 손목뼈), Capitate(소두골) 등이다.\n" +
                " <br> \n" +
                " <br> &nbsp; \n" +
                " <br> 하지만 한 전공의가 메시지로 호시탐탐을 묻자 ‘일하는전공의’&nbsp;운영자는&nbsp;“해부학 배운지 오래인데”라며 오히려 “알려주세요”라고 말한다. 현직 의사는 이에 대해 “(호시탐탐은) 본과 1학년 시험에 무조건 나온다”며&nbsp;“정형외과가 아니어도 의대만 다니면 모를 수가 없는 줄임말이다”고 주장했다. \n" +
                " <br> &nbsp; \n" +
                " <br> \n" +
                " <div class=\"ab_sub_heading\">\n" +
                "  <div class=\"ab_sub_headingline\">\n" +
                "   <h2> 바이탈 사인이 '인성? 존중?'&nbsp; </h2>\n" +
                "  </div>\n" +
                " </div> 또 ‘일하는전공의’ 운영자는&nbsp;혈압과 맥박·호흡·체온 등을 의미하는 생체활력징후인 바이탈 사인(vital sign)에 대해서도 엉뚱한 답을 내놨다. 바이탈 사인은 흔히 ‘V/S’로 줄여 쓴다. 하지만 일하는전공의는 혈압·맥박 등으로 대답하지 못했고,&nbsp;“인성·생각·존중·마음”이라고 답했다. \n" +
                " <br> &nbsp; \n" +
                " <br> 특히 ‘일하는전공의’ 운영자는 대화에 중국식 표현을 썼다. 한 전공의가&nbsp;“글 내용이 전혀 병원에서 근무한 사람이 썼을 것 같지 않은 단어가 많다”고 하자 그는 “정말 (병원에서) 근무한 사람이 적었는지 회의한다”고 응수했다.&nbsp; \n" +
                " <br> \n" +
                " <div class=\"ab_photo photo_center \" style=\"width: 509px;\"> \n" +
                "  <div class=\"image\"> \n" +
                "   <img alt=\"일하는전공의는 '의심하다'를 중국식 표현인 '회의하다'로 썼다. 사진 대한의사협회\" data-src=\"https://pds.joins.com/news/component/htmlphoto_mmdata/202008/31/0817fc87-f859-4908-be4a-c9b6ca2dd0db.jpg\" data-type=\"article\"> <span class=\"mask\"></span> \n" +
                "  </div> \n" +
                "  <p class=\"caption\">일하는전공의는 '의심하다'를 중국식 표현인 '회의하다'로 썼다. 사진 대한의사협회</p> \n" +
                " </div> &nbsp; \n" +
                " <br> \n" +
                " <div class=\"ab_sub_heading\">\n" +
                "  <div class=\"ab_sub_headingline\">\n" +
                "   <h2> 의심한다는 중국식 표현으로 써&nbsp; </h2>\n" +
                "  </div>\n" +
                " </div> 하지만 ‘회의하다’라는 표현은 ‘회의(怀疑)’로 우리말의 ‘의심하다’와 같다. 그러자 이 전공의는 번역프로그램을 이용했냐고 물었고, 일하는전공의 운영자는&nbsp;“아니다”고 해명했다. \n" +
                " <br> &nbsp; \n" +
                " <br> 의협 김대하 대변인은 “제보 내용에 따르면 전공의, 의사는 물론 한국인도 아닐 가능성이 있어 보인다”며 “만약 이게 사실이라면 누군가 전공의 단체행동에 대한 국민 여론을 조작하려 전공의를 사칭한 것”이라고 지적했다. \n" +
                " <br> &nbsp; \n" +
                " <br> \n" +
                " <div class=\"ab_sub_heading\">\n" +
                "  <div class=\"ab_sub_headingline\">\n" +
                "   <h2> 의협, \"만일 여론조작이면 매우 충격적\"&nbsp; </h2>\n" +
                "  </div>\n" +
                " </div> 이어 김 대변인은 “(만일) 의료계의 정당한 주장을 폄훼하기 위해 누군가에 의해 조직적으로 (여론조작이) 이뤄지고 있다면 매우 충격적인 일”이라고 덧붙였다.\n" +
                " <br> \n" +
                " <br> &nbsp; \n" +
                " <br> 한편, 30일부터 이러한 의혹이 집중적으로 제기되자 ‘일하는전공의 페이지’ 운영자는 나는 개인이라고 밝혔다. 그러면서 한국과 중국, 북한 지도자에 대한 욕설도 함께 썼다. 또 당분간 쉬겠다는 내용도 올렸다. 일하는전공의 페이지는 현재 검색되지 않는다. 비공개로 전환하거나 삭제된 것으로 보인다. \n" +
                " <br> &nbsp; \n" +
                " <br> 중앙일보는 ‘일하는전공의’ 운영자의 입장을 듣기 위해 메신저로 연락을 시도했지만, 연락이 닿지 않고 있다. \n" +
                " <br> &nbsp; \n" +
                " <br> \n" +
                " <div class=\"ab_sub_heading\">\n" +
                "  <div class=\"ab_sub_headingline\">\n" +
                "   <h2> 일하는전공의 측, \"전공의 맞다\" 주장&nbsp; </h2>\n" +
                "  </div>\n" +
                " </div> 이날 오후 연합뉴스는 자신을 ‘일하는전공의’ 운영자로 소개한 전공의와 화상 인터뷰를 했다. 이 전공의는 ‘V/S’(바이털 사인)와 관련해 엉뚱한 대답을 한 이유로&nbsp;“의사를 사칭하려고 마음만 먹으면 검색으로도 알 수 있는 내용”이라며 “신상털이 등 공격 의도를 갖고 메시지를 보내오는 사람을 반박하려는 의도였다”고 설명했다. \n" +
                " <br> &nbsp; \n" +
                " <br> 또 이 전공의는 중국식 표현을 쓴 것에 대해서는 “전공의 사회가 워낙 좁아 말투가 티 나지 않게 하려고 번역기처럼 답했다”고&nbsp;해명했다. \n" +
                " <br> &nbsp; \n" +
                " <br> 세종=김민욱 기자 kim.minwook@joongang.co.kr&nbsp;&nbsp; \n" +
                " <br> <!-- 아티클 공통 : DA 250 --> \n" +
                " <div class=\"\" style=\"width:250px;height:250px;float:right;clear:both;\" id=\"criteo_network\"></div> <!-- 아티클 공통 : 관련기사 --> <!-- //아티클 공통 : 관련기사 --> \n" +
                "</div>";
        addComponent(text);
    }

    private void addComponent(String t){
        LinearLayout.LayoutParams params = null;
        StringBuilder sb = new StringBuilder();
        this.text = t;
        removeNbsp();

       while(idx < text.length()){
            char current = text.charAt(idx);

            if (current == '<') { //열린 괄호일 경우
                char next = text.charAt(idx + 1);

                //태크 닫힘 및 주석처리
                if (next == '/' || next == '!') {
                    while (true) {
                        current = text.charAt(idx++);
                        if (current == '>') {
                            break;
                        }
                    }
                    continue;
                }


                if (!sb.toString().isEmpty()) {
                    //현재 문자열이 존재하는 경우
                    String temp = sb.toString().trim();
                    if (!temp.isEmpty()) {
                        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 5, 0, 10);

                        TextView view = new TextView(this);
                        view.setText(temp);
                        view.setLayoutParams(params);
                        view.setTextAppearance(this, android.R.style.TextAppearance_Widget);
                        linearLayout.addView(view);
                    }

                    sb.delete(0, sb.length());
                }

                StringBuilder temp = new StringBuilder();

                while (true) { //닫힌 괄호가 나올 때까지 붙인다.
                    current = text.charAt(idx++);
                    if (current == '>') {
                        --idx;
                        temp.append(current);
                        break;
                    }
                    temp.append(current);
                }

                View view = makeComponent(this, temp.toString()); //컴포넌트 반환

                if (view != null) {
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 5, 0, 8);
                    view.setLayoutParams(params);
                    linearLayout.addView(view);
                }
            }  else
                sb.append(current);

            idx++;
        }
    }

    //크롤링 된 nbsp를 제거거
    private void removeNbsp(){
        text = text.replaceAll("&nbsp","");
        text = text.replaceAll("<br>","");
        text = text.replaceAll(";","");

    }

    //컴포넌트를 만든다.
    private View makeComponent(Context context, String temp) {
        if (temp.contains("img")) {
            //현재 만들어진 스트링에 이미지 태그가 있는 경우
            int start = temp.indexOf("src");
            start += 5;

            StringBuilder src = new StringBuilder("");
            while (true) {
                char cur = temp.charAt(start++);
                if (cur == '\"') {
                    break;
                }
                src.append(cur);
            }
            ImageView view = new ImageView(context);
            view.setAdjustViewBounds(true);
            Glide.with(context).load(src.toString()).fitCenter().placeholder(R.drawable.ic_launcher_foreground).into(view);
            return view;
        } else if (temp.indexOf("p") == 1){ //p 태그인 경우
            if(temp.contains("caption")){
                SpannableStringBuilder sbb =new SpannableStringBuilder();

                idx++;
                char current = ' ';
                while(true){
                    current = text.charAt(idx++);
                    if(current == '<') {
                        idx -=2;
                        break;
                    }

                    sbb.append(current);
                }

                TextView view =new TextView(this);
                view.setTextColor(Color.parseColor("#aaaaaa"));
                view.setText(sbb.toString());
                view.setGravity(Gravity.CENTER_HORIZONTAL);

                return view;
            }
        }
        return null;
    }

}
