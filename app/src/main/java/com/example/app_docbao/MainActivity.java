package com.example.app_docbao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ListView lvTieuDe;
    ArrayList<String> arrayTitle,arrayLink,arrayPubDate;
    ArrayList<DocBao> mangdocbao;
    ArrayAdapter adapter;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTieuDe=(ListView) findViewById(R.id.listviewTieuDe);
        arrayTitle =new ArrayList<>();
        arrayLink=new ArrayList<>();
        arrayPubDate=new ArrayList<>();
        mangdocbao=new ArrayList<DocBao>();
        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayTitle);
        lvTieuDe.setAdapter(adapter);

        new ReadRSS().execute("https://vnexpress.net/rss/the-gioi.rss");
        lvTieuDe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>adapterView, View view, int i, long l) {

                Intent intent=new Intent(MainActivity.this,NewsActivity.class);
                intent.putExtra("linkTinTuc",arrayLink.get(i));
                startActivity(intent);

                // Toast.makeText(MainActivity.this,arrayLink.get(i),Toast.LENGTH_SHORT).show();;
            }
        });
    }
    private class ReadRSS extends AsyncTask<String,Void,String>{
        StringBuilder content = new StringBuilder();
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);

                InputStreamReader inputStreamReader= new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader buffereredReader= new BufferedReader(inputStreamReader);
                String line ="";
                while((line=buffereredReader.readLine())!=null){
                    content.append(line);
                }

    buffereredReader.close();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser=new XMLDOMParser();
            Document document= parser.getDocument(s);
            NodeList nodeList=document.getElementsByTagName("item");
            NodeList nodeListdescription=document.getElementsByTagName("description");
            NodeList nodeListpub=document.getElementsByTagName("pubdate");
            String hinhanh="";
            String tieuDe="";
            String link="";
            String ngaydang="";



            for (int i=0;i<nodeList.getLength();i++){
                String cdata=nodeListdescription.item(i+1).getTextContent();
                Pattern p=Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher=p.matcher(cdata);
                if(matcher.find()){
                    hinhanh=matcher.group(1);
                }
                Element element= (Element) nodeList.item(i);
                tieuDe=parser.getValue(element,"title");
                arrayTitle.add(tieuDe);
                link=(parser.getValue(element,"link"));
                arrayLink.add(link);
                ngaydang=parser.getValue(element,"pubDate");
                arrayPubDate.add(ngaydang);
                mangdocbao.add(new DocBao(tieuDe,link,hinhanh,ngaydang));

            }
            customAdapter = new CustomAdapter(MainActivity.this, android.R.layout.simple_list_item_1,mangdocbao);
            lvTieuDe.setAdapter(customAdapter);
            super.onPostExecute(s);
          //  adapter.notifyDataSetChanged();

           // Toast.makeText(MainActivity.this,tieuDe,Toast.LENGTH_SHORT).show();;

          //  Toast.makeText(MainActivity.this,"Item: "+nodeList.getLength(),Toast.LENGTH_SHORT).show();
        }
    }
}