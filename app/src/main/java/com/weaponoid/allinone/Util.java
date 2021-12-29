package com.weaponoid.allinone;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class Util {
    public static String RootDirectoryForFacebook = "/My Story Saver/facebook";
    public static String RootDirectoryForShareChat = "/My Story Saver/sharechat";


    public static File RootDirectoryWhatsapp =
            new File(Environment.getExternalStorageDirectory()
            +"/Download/MyStorySaver/Whatsapp");

    public static void createFileFolder(){
        if(!RootDirectoryWhatsapp.exists())
            RootDirectoryWhatsapp.mkdirs();
    }

    public static void download(String downloadPath, String destinationPath, Context context,String filename){
        Toast.makeText(context,"Downloading Started",Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(downloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(filename);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,destinationPath+filename);
        ((DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }
}
