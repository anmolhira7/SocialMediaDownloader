package com.weaponoid.allinone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.weaponoid.allinone.databinding.ActivityShareChatBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;


public class ShareChatActivity extends AppCompatActivity {

    private ActivityShareChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_chat);

        binding.downloadBtn.setOnClickListener(v -> {
            getShareChatData();
        });
    }

    private void getShareChatData() {
        URL url = null;
        try {
            url = new URL(binding.sharechatUrl.getText().toString());
            String host = url.getHost();
            if (host.contains("sharechat"))
                new CallGetShareChatData().execute(binding.sharechatUrl.getText().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    class CallGetShareChatData extends AsyncTask<String, Void, Document> {

        Document scDocument;

        @Override
        protected Document doInBackground(String... strings) {
            try {
                scDocument = Jsoup.connect(strings[0])
                        .header("Accept-Language","en-US")
                        .header("Connection","keep-alive")
                        .header("User-Agent",
                                "Mozilla/5.0")
                        .get();

                   //    .header("Accept-Language","en-US")
                      //
                     //   .header("Connection","keep-alive")
                    //    .header("Host","ksh")
                     //   .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Ma" +
                    //            "c OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396" +
                      //          ".99 Safari/537.36")
                     //   .header("Pragma","no-cache")

            } catch (IOException e) {
                e.printStackTrace();
            }
            return scDocument;
        }

        @Override
        protected void onPostExecute(Document document) {
            String videoUrl = document.select("meta[property=\"og:video:secure_url\"]")
                    .last().attr("content");

            if (!videoUrl.equals("")) {
              /*  Util.download(videoUrl, Util.RootDirectoryForShareChat, ShareChatActivity.this,
                        "Sharechat " + System.currentTimeMillis() + ".mp4");*/


                //downloadmanager start
                String fileName = "Sharechat" + System.currentTimeMillis() + ".mp4";

                Uri uri = Uri.parse(videoUrl);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(true).setTitle("Some name");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDescription("Downloading file");
                request.setTitle(fileName);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "Anmol Folder" + File.separator + fileName);
                // DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE).enqueue(request);
                DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                mgr.enqueue(request);

                Toast.makeText(ShareChatActivity.this, "Downloading successfully", Toast.LENGTH_SHORT).show();

                //downloadmanager ends
            }else{
                Toast.makeText(ShareChatActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
            }



        }
    }
}