package com.weaponoid.allinone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.weaponoid.allinone.databinding.ActivityFacebookBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FacebookActivity extends AppCompatActivity {

    private ActivityFacebookBinding binding;
    private FacebookActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_facebook);
        activity = this;
        binding.downloadBtn.setOnClickListener(v -> {
            getFacebookData();
        });
    }

    private void getFacebookData() {
        URL url = null;
        try {
            url = new URL(binding.fbUrl.getText().toString());

            String host = url.getHost();

            new CallGetData().execute(binding.fbUrl.getText().toString());

               // Toast.makeText(activity,"Url is invalid",Toast.LENGTH_LONG).show();


        } catch (MalformedURLException e) {
           //customize this code 13:27
            Log.v("Facebook Acitivty","Error message"+e.getMessage());
        }



    }
        class CallGetData extends AsyncTask<String, Void, Document>{

            Document fbDoc;

            @Override
            protected Document doInBackground(String... strings) {
              try {
                  fbDoc = Jsoup.connect(strings[0]).get();
              }catch (IOException e){
                  e.printStackTrace();
              }
              return fbDoc;
            }

            @Override
            protected void onPostExecute(Document document) {
               String videoUrl = document.select("meta[property=\"og:video\"]")
                       .last().attr("content");

               if(!videoUrl.equals(""))
                   Util.download(videoUrl,Util.RootDirectoryForFacebook,FacebookActivity.this,"facebook "+System.currentTimeMillis()+".mp4");
            }
        }

}