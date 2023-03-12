package com.tech.statussaver.Insta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tech.statussaver.R;
import com.tech.statussaver.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class InstagramVideo {
    private static Context cntx;
    public static void downloadVideo(Context context, String stringData) {

        cntx = context;
        ArrayList<String> arrayList = new ArrayList<>();

        if (stringData.matches("https://www.instagram.com/(.*)")) {
            Log.d("@@@@", stringData);
            String[] data = stringData.split(Pattern.quote("?"));
            String string = data[0];
            Log.d("@@@@", string);

            if (isNetworkAvailable()) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("Accept", "application/json");
                client.addHeader("Content-Type", "application/json;charset=UTF-8");
                client.addHeader("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
                client.addHeader("x-requested-with", "XMLHttpRequest");
                client.get(string + "?__a=1&__d=dis", null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String res = new String(responseBody);
                        Log.d("@@@@","res "+ res);

                        try {
                            JSONObject jsonObject = new JSONObject(res);

                            Log.d("@@@@","jsonObject "+jsonObject);
                            String singleLink = null;
                            JSONObject objectGraphql = jsonObject.getJSONObject("graphql");
                            JSONObject objectMedia = objectGraphql.getJSONObject("shortcode_media");
                            boolean isVideo = objectMedia.getBoolean("is_video");
                            if (isVideo) {
                                singleLink = objectMedia.getString("video_url");
                            } else {
                                singleLink = objectMedia.getString("display_url");
                            }
                            Log.d("@@@@","link "+ singleLink);


                            if (singleLink.contains("jpg")|| singleLink.contains("png")) {
                                Util.Companion.download(singleLink, Util.RootDirectoryInstagram, context, "instagram"+System.currentTimeMillis() + ".jpg");
                            } else if (singleLink.contains(".mp4")) {
                                Util.Companion.download(singleLink, Util.RootDirectoryInstagram, context, "instagram"+System.currentTimeMillis() + ".mp4");
                            }


                            try {
                                JSONObject objectSidecar = objectMedia.getJSONObject("edge_sidecar_to_children");
                                JSONArray jsonArray = objectSidecar.getJSONArray("edges");

                                arrayList.clear();

                                String edgeSidecar = null;

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    JSONObject node = object.getJSONObject("node");
                                    boolean is_video_group = node.getBoolean("is_video");
                                    if (is_video_group) {
                                        edgeSidecar = node.getString("video_url");
                                    } else {
                                        edgeSidecar = node.getString("display_url");
                                    }
                                    arrayList.add(edgeSidecar);
                                    Log.d("@@@@","edgesidecar "+ edgeSidecar);

                                }
                                for(int i = 0;i<arrayList.size();i++){
                                    String groupLink = arrayList.get(i);
                                    if (groupLink.contains(".jpg")||singleLink.contains("png")) {
                                        Util.Companion.download(groupLink, Util.RootDirectoryInstagram, context, "instagram"+System.currentTimeMillis() + ".jpg");
                                    } else if (groupLink.contains(".mp4")) {
                                        Util.Companion.download(groupLink, Util.RootDirectoryInstagram, context, "instagram"+System.currentTimeMillis() + ".mp4");
                                    }
                                }

                            } catch (Exception e) {
                                Log.e("error_show", e.toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, context.getResources().getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("@@@@", "OnFailure");
                    }


                });
            } else {
                Toast.makeText(context,context.getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        }
    }
    //network check
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) cntx.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

