package com.atbuys.runmawi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("Our Netflix Clone script is a splendid Video Streaming script that empowers you to put your leg forward and lead the right direction on the path of building your Video Streaming website.");

        List<String> football = new ArrayList<String>();
        football.add("This Video Streaming website lets your users explore various TV shows, movies, video trailers, etc. from large stack of video categories and watch them at their convenience.");


        List<String> basketball = new ArrayList<String>();
        basketball.add("Users can search various videos, movies, video trailers etc. with auto suggestion options provided.");

        List<String> baseball = new ArrayList<String>();
        baseball.add("To assure high-quality video playback over multiple devices, we have integrated FFMPEG Player, video player software that lets users watch videos smoothly, fast across various browsers and media types.");

        List<String> tennis = new ArrayList<String>();
       tennis.add("On the upper hand, admin has all the rights to manage membership plans, videos, reported users, payment history, and video categories, export various details of videos, users, payment etc. with CSV and XLS seamlessly and many more.");

        expandableListDetail.put("What is Netflix clone?", cricket);
        expandableListDetail.put("Does the videos are categorized?", football);
        expandableListDetail.put("How does Search made easier?", basketball);
        expandableListDetail.put("What media format does player support?", baseball);
        expandableListDetail.put("What facinates the admin pannel?", tennis);
        return expandableListDetail;
    }
}
