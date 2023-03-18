package com.tech.alldownloadersaver;

import static com.tech.alldownloadersaver.util.Util.RootDirectoryTwitter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tech.alldownloadersaver.api.CommonClassForAPI;
import com.tech.alldownloadersaver.databinding.ActivityTwitterBinding;
import com.tech.alldownloadersaver.model.TwitterResponse;
import com.tech.alldownloadersaver.util.InternetConnection;
import com.tech.alldownloadersaver.util.Util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.observers.DisposableObserver;

public class TwitterActivity extends AppCompatActivity {
    private ActivityTwitterBinding binding;
    TwitterActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwitterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterActivity.this.finish();
                startActivity(new Intent(TwitterActivity.this, MainActivity.class));
            }
        });

        binding.pasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData.Item item =  clipboardManager.getPrimaryClip().getItemAt(0);
                String textItem = item.getText().toString();

                binding.tweetUrl.setText(textItem);
            }
        });

        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnection.Companion.isNetworkAvailable(TwitterActivity.this)) {

                    binding.progressBar.setProgress(0);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    GetTwitterData();
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           int dummy = 0;
                           int progressBarStatus = 0;

                           while (progressBarStatus < 100){
                               try {
                                   dummy +=25;
                                   Thread.sleep(1000);

                               }catch (InterruptedException e){
                                   e.printStackTrace();
                               }
                               progressBarStatus = dummy;
                               binding.progressBar.setProgress(progressBarStatus);
                           }
                       }
                   }).start();
                }
            }
        });
    }

    private void GetTwitterData() {
        try {

            URL url = new URL(binding.tweetUrl.getText().toString());
            String host = url.getHost();
            if (host.contains("twitter.com")) {
                Long id = getTweetId(binding.tweetUrl.getText().toString());
                if (id != null) {
                    callGetTwitterData(String.valueOf(id));
                }
            } else {
                Toast.makeText(activity, "Enter twitter url only", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long getTweetId(String s) {
        try {
            String[] split = s.split("\\/");
            String id = split[5].split("\\?")[0];
            return Long.parseLong(id);
        } catch (Exception e) {
            Log.d("TAG", "getTweetId: " + e.getLocalizedMessage());
            return null;
        }
    }


    private void callGetTwitterData(String id) {
        String URL = "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php";
        try {
                if (commonClassForAPI != null) {
                    commonClassForAPI.callTwitterApi(observer,URL,id);
                }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private DisposableObserver<TwitterResponse> observer = new DisposableObserver<TwitterResponse>() {
        @Override
        public void onNext(TwitterResponse twitterResponse) {
            try {
                VideoUrl = twitterResponse.getVideos().get(0).getUrl();
                Log.d("@@@@", VideoUrl.toString());
                if (twitterResponse.getVideos().get(0).getType().equals("image")){
                    Log.d("@@@@",twitterResponse.getVideos().get(0).getType());
                    Toast.makeText(activity, "This url is not contain video.", Toast.LENGTH_SHORT).show();

                }else {
                    VideoUrl = twitterResponse.getVideos().get(twitterResponse.getVideos().size()-1).getUrl();
                    Util.Companion.download(VideoUrl, RootDirectoryTwitter, activity, getFilenameFromURL(VideoUrl,"mp4"));
                    binding.tweetUrl.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

        }

        @Override
        public void onComplete() {
        }
    };


    public String getFilenameFromURL(String url, String type) {
        if (type.equals("image")){
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".jpg";
            }
        }else {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".mp4";
            }
        }
    }

    @Override
    public void onBackPressed() {
            this.finishAffinity();
            startActivity(new Intent(this, MainActivity.class));
    }
}