package com.tech.statussaver.Insta;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tech.statussaver.model.FacebookModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FacebookVideo extends AsyncTask<Void,Integer, FacebookModel> {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36";
    private Context context;
    String url;
    private long startTime = 0L;
    private boolean showLogs = false;

    //For Errors
    private Exception exception = null;


    protected abstract void onExtractionComplete(FacebookModel facebookFile);
    protected abstract void onExtractionFail(Exception Error);

    private FacebookModel parseHtml(String url)
    {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();
            BufferedReader reader = null;
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            StringBuilder streamMap= new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line= reader.readLine()) != null) {
                    streamMap.append(line);
                }
            } catch (Exception E) {
                E.printStackTrace();
                if (reader != null)
                    reader.close();
                urlConnection.disconnect();
                onCancelled();
            } finally {
                if (reader != null)
                    reader.close();
                urlConnection.disconnect();
            }

            if (streamMap.toString().contains("You must log in to continue.")) {
                exception = new RuntimeException("You must log in to continue.");
                return null;
            }
            else
            {

                Pattern metaTAGTitle = Pattern.compile("<meta property=\"og:title\"(.+?)\" />");
                Matcher metaTAGTitleMatcher = metaTAGTitle.matcher(streamMap);

                Pattern metaTAGDescription = Pattern.compile("<meta property=\"og:description\"(.+?)\" />");
                Matcher metaTAGDescriptionMatcher = metaTAGDescription.matcher(streamMap);

                String authorName = "";
                String fileName = "";

                if(metaTAGTitleMatcher.find())
                {
                    String author = streamMap.substring(metaTAGTitleMatcher.start(),metaTAGTitleMatcher.end());
                    Log.e("Extractor","AUTHOR :: "+author);

                    author = author.replace("<meta property=\"og:title\" content=\"","").replace("\" />","");

                    authorName = author;
                }
                else
                {
                    authorName = "N/A";
                }

                if(metaTAGDescriptionMatcher.find())
                {
                    String name = streamMap.substring(metaTAGDescriptionMatcher.start(),metaTAGDescriptionMatcher.end());

                    Log.e("Extractor","FILENAME :: "+name);


                    name = name.replace("<meta property=\"og:description\" content=\"","").replace("\" />","");

                    fileName = name;
                }
                else
                {
                    fileName = "N/A";
                }

                Pattern sdVideo = Pattern.compile("(sd_src):\"(.+?)\"");
                Matcher sdVideoMatcher = sdVideo.matcher(streamMap);

                Pattern hdVideo = Pattern.compile("(hd_src):\"(.+?)\"");
                Matcher hdVideoMatcher = hdVideo.matcher(streamMap);


                FacebookModel facebookModel = new FacebookModel();
                facebookModel.setAuthor(authorName);
                facebookModel.setFilename(fileName);
                facebookModel.setExt("mp4");

                if(sdVideoMatcher.find())
                {
                    String vUrl = sdVideoMatcher.group();
                    vUrl = vUrl.substring(8,vUrl.length()-1); //sd_scr: 8 char
                    facebookModel.setSdUrl(vUrl);


                    if(showLogs){ Log.e("Extractor","SD_URL :: "+vUrl);}
                }
                else
                {
                    facebookModel.setSdUrl(null);
                }

                if(hdVideoMatcher.find())
                {
                    String vUrl = hdVideoMatcher.group();
                    vUrl = vUrl.substring(8,vUrl.length()-1); //hd_scr: 8 char
                    facebookModel.setHdUrl(vUrl);
                    if(showLogs){ Log.e("Extractor","HD_URL :: "+vUrl);}
                }
                else
                {
                    facebookModel.setHdUrl(null);
                }

                if(facebookModel.getSdUrl() == null && facebookModel.getHdUrl()==null)
                {
                    exception = new RuntimeException("Url Not Valid");
                    return null;
                }

                return facebookModel;
            }
        }
        catch (Exception E)
        {
            exception = E;
            return null;
        }
    }

    @Override
    protected FacebookModel doInBackground(Void... voids) {
        FacebookModel Ff = parseHtml(url);
        return Ff;
    }

    @Override
    protected void onPostExecute(FacebookModel facebookModel) {
        super.onPostExecute(facebookModel);
        if(showLogs){
            Log.e("Extractor","Extraction Time Taken "+(System.currentTimeMillis()-startTime)+" MS");
        }
        if(facebookModel!=null) {
            onExtractionComplete(facebookModel);
        }
        else
        {
            onExtractionFail(exception);
        }
    }



    public FacebookVideo(Context context, String url, boolean showLogs) {
        this.context = context;
        this.url = url;
        this.showLogs = showLogs;
        startTime = System.currentTimeMillis();

        if(showLogs){ Log.e("Extractor","Extraction Started "+startTime+" MS");}
        this.execute();
    }
}
