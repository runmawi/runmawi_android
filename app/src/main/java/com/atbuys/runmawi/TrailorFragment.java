package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

public class TrailorFragment extends Fragment {

    ImageView trailorimage;
    TextView trailoename, trailorduration;
    String videoId, user_id;
    private ArrayList<videodetail> movie_detaildata;
    private ArrayList<season> season;

    public TrailorFragment(String videoId, String user_id) {
        this.videoId = videoId;
        this.user_id = user_id;
    }

    public TrailorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_trailor, container, false);

        trailorimage = root.findViewById(R.id.trailerimage);
        trailoename = root.findViewById(R.id.trailorname);
        trailorduration = root.findViewById(R.id.trailorduration);

        movie_detaildata = new ArrayList<>();
        season = new ArrayList<>();

        // Making the API call
        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getMovieDetails(user_id, videoId);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JSONResponse jsonResponse = response.body();

                    // Check if video detail is not null before processing
                    if (jsonResponse.getVideodetail() != null) {
                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getVideodetail()));

                        if (!movie_detaildata.isEmpty() && movie_detaildata.get(0) != null) {
                            // Safely load data
                            Picasso.get().load(movie_detaildata.get(0).getImage_url()).into(trailorimage);
                            trailoename.setText(movie_detaildata.get(0).getTitle());
                            // trailorduration.setText(movie_detaildata.get(0).getMovie_duration()); // Uncomment if needed
                        } else {
                            Toast.makeText(getActivity(), "No movie details found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Video details are null.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch data. Response not successful.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for the trailer image
        trailorimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if movie details exist and the trailer URL is valid
                if (!movie_detaildata.isEmpty() && movie_detaildata.get(0).getTrailer() != null && !movie_detaildata.get(0).getTrailer().isEmpty()) {
                /*    Intent in = new Intent(getActivity(), TrailerPlayerActivity.class);
                    in.putExtra("id", movie_detaildata.get(0).getId());
                    in.putExtra("url", movie_detaildata.get(0).getTrailer());
                    in.putExtra("type", "video");
                    startActivity(in);*/

                    Intent intent1=new Intent(getActivity(),EncodedWebViewActivity.class);
                    intent1.putExtra("url",movie_detaildata.get(0).getTrailer());
                    startActivity(intent1);
                } else {
                    Toast.makeText(getActivity(), "No Trailer Found", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }
}
