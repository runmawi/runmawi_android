package com.atbuys.runmawi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class CommentFragment extends Fragment {

    private RecyclerView usercommentrecycler;

    CommentsAdopter commentsAdopter;
    private RecyclerView.LayoutManager layoutManager1;
    private ArrayList<comment> usercommentslist;
    String videoId,user_id,type="episode";
    TextView nocomment,comment_count,seeall;
    ImageView commentsend;
    EditText commenttext;
    ProgressBar commentprogress;

    public CommentFragment() {
    }

    public CommentFragment(String videoId, String user_id, String type) {
        this.videoId=videoId;
        this.user_id=user_id;
        this.type=type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_comment, container, false);

        usercommentrecycler = (RecyclerView) root.findViewById(R.id.usercomments);
        usercommentslist = new ArrayList<comment>();
        nocomment=(TextView)root.findViewById(R.id.nocomment);
        comment_count=(TextView)root.findViewById(R.id.count_comment);
        seeall=(TextView)root.findViewById(R.id.see_all);
        commentsend=(ImageView)root.findViewById(R.id.sendcomment);
        commenttext=(EditText)root.findViewById(R.id.commenttext);
        commentprogress=(ProgressBar)root.findViewById(R.id.comment_progress) ;

        commentsAdopter = new CommentsAdopter(usercommentslist, getActivity());
        layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);


        usercommentrecycler.setHasFixedSize(true);
        usercommentrecycler.setLayoutManager(layoutManager1);
        usercommentrecycler.setAdapter(commentsAdopter);


        if (type.equalsIgnoreCase("video")){
            Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId,"play_videos ");
            movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();

                    if (jsonResponse.getComment().length == 0) {
                        nocomment.setVisibility(View.VISIBLE);
                        comment_count.setVisibility(View.GONE);
                        seeall.setVisibility(View.GONE);

                    } else {

                        comment_count.setText(jsonResponse.getComment().length+" Comments");
                        nocomment.setVisibility(View.GONE);
                        usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                        commentsAdopter = new CommentsAdopter(usercommentslist);
                        usercommentrecycler.setAdapter(commentsAdopter);

                    }

                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        }else if (type.equalsIgnoreCase("live")){
            Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId,"LiveStream_play ");
            movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();

                    if (jsonResponse.getComment().length == 0) {
                        nocomment.setVisibility(View.VISIBLE);
                        comment_count.setVisibility(View.GONE);
                        seeall.setVisibility(View.GONE);

                    } else {

                        comment_count.setText(jsonResponse.getComment().length+" Comments");
                        nocomment.setVisibility(View.GONE);
                        usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                        commentsAdopter = new CommentsAdopter(usercommentslist);
                        usercommentrecycler.setAdapter(commentsAdopter);

                    }

                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        }



        commentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentprogress.setVisibility(View.VISIBLE);
                commentsend.setVisibility(View.GONE);

                String comment = commenttext.getText().toString();

                if(commenttext.getText().toString().isEmpty())
                {
                    commentprogress.setVisibility(View.GONE);
                    commentsend.setVisibility(View.VISIBLE);

                    Toast.makeText(getActivity(),"Please enter your Comment", Toast.LENGTH_LONG).show();
                }

                else {

                    if (type.equalsIgnoreCase("video")){
                        Api.getClient().getAddLivecomment(user_id, videoId, comment,"play_videos ", new Callback<AddComment>() {

                            @Override
                            public void success(AddComment addComment, Response response) {

                                addComment = addComment;

                                if (addComment.getStatus().equalsIgnoreCase("true")) {

                                    Toast.makeText(getActivity(),""+addComment.getMessage(),Toast.LENGTH_LONG).show();

                                    Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId,"play_videos");
                                    movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();

                                            if (jsonResponse.getComment().length == 0) {
                                                nocomment.setVisibility(View.VISIBLE);
                                                comment_count.setVisibility(View.GONE);
                                                seeall.setVisibility(View.GONE);

                                            } else {
                                                comment_count.setText(jsonResponse.getComment().length+" Comments");
                                                comment_count.setVisibility(View.VISIBLE);
                                                nocomment.setVisibility(View.GONE);
                                                commentprogress.setVisibility(View.GONE);
                                                commentsend.setVisibility(View.VISIBLE);
                                                commenttext.setText("");
                                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                commentsAdopter = new CommentsAdopter(usercommentslist);
                                                usercommentrecycler.setAdapter(commentsAdopter);
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                } else {

                                    Toast.makeText(getActivity(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    } else if (type.equalsIgnoreCase("live")) {
                        Api.getClient().getAddLivecomment(user_id, videoId, comment,"LiveStream_play ", new Callback<AddComment>() {

                            @Override
                            public void success(AddComment addComment, Response response) {

                                addComment = addComment;

                                if (addComment.getStatus().equalsIgnoreCase("true")) {

                                    Toast.makeText(getActivity(),""+addComment.getMessage(),Toast.LENGTH_LONG).show();

                                    Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId,"LiveStream_play");
                                    movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                            JSONResponse jsonResponse = response.body();

                                            if (jsonResponse.getComment().length == 0) {
                                                nocomment.setVisibility(View.VISIBLE);
                                                comment_count.setVisibility(View.GONE);
                                                seeall.setVisibility(View.GONE);

                                            } else {

                                                comment_count.setText(jsonResponse.getComment().length + " Comments");
                                                comment_count.setVisibility(View.VISIBLE);
                                                commentprogress.setVisibility(View.GONE);
                                                commentsend.setVisibility(View.VISIBLE);
                                                commenttext.setText("");
                                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                commentsAdopter = new CommentsAdopter(usercommentslist);
                                                usercommentrecycler.setAdapter(commentsAdopter);
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            Log.d("Error", t.getMessage());
                                        }
                                    });

                                } else {

                                    Toast.makeText(getActivity(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }


                }
            }
        });



        usercommentrecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (usercommentslist.size() > position) {
                            if (usercommentslist.get(position) != null) {

                                if(!usercommentslist.get(position).getUser_id().equalsIgnoreCase(user_id))
                                {
                                    Toast.makeText(getActivity(),"Only your comments will be editable",Toast.LENGTH_LONG).show();

                                }
                                else {


                                    View view1 = getLayoutInflater().inflate(R.layout.comments_edit, null);
                                    BottomSheetDialog dialog1 = new BottomSheetDialog(getActivity());
                                    dialog1.setContentView(view1);
                                    dialog1.show();


                                    TextView tx1 = (TextView) view1.findViewById(R.id.edit);
                                    TextView tx2 = (TextView) view1.findViewById(R.id.delete);
                                    LinearLayout txt1 = (LinearLayout) view1.findViewById(R.id.edittxt1);
                                    EditText edit1 = (EditText) view1.findViewById(R.id.edittextcomment);
                                    ImageView img = (ImageView) view1.findViewById(R.id.editecoment);
                                    ProgressBar pro = (ProgressBar) view1.findViewById(R.id.comment_progress1);


                                    tx1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            txt1.setVisibility(View.VISIBLE);
                                            edit1.setText(usercommentslist.get(position).getComment());

                                            img.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {


                                                    pro.setVisibility(View.VISIBLE);
                                                    img.setVisibility(View.GONE);

                                                    String comment = edit1.getText().toString();

                                                    if (edit1.getText().toString().isEmpty()) {
                                                        pro.setVisibility(View.GONE);
                                                        img.setVisibility(View.VISIBLE);

                                                        Toast.makeText(getActivity(), "Please enter your Comment", Toast.LENGTH_LONG).show();
                                                    } else {


                                                        if (type.equalsIgnoreCase("video")) {
                                                            Api.getClient().getupdateLivecomment(user_id, videoId, comment, "play_videos", usercommentslist.get(position).getId(), new Callback<AddComment>() {

                                                                @Override
                                                                public void success(AddComment addComment, Response response) {

                                                                    addComment = addComment;

                                                                    if (addComment.getStatus().equalsIgnoreCase("true")) {

                                                                        Toast.makeText(getActivity(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                                                        dialog1.cancel();
                                                                        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_videos");
                                                                        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                                JSONResponse jsonResponse = response.body();

                                                                                pro.setVisibility(View.GONE);
                                                                                img.setVisibility(View.VISIBLE);
                                                                                edit1.setText("");

                                                                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                                                commentsAdopter = new CommentsAdopter(usercommentslist);
                                                                                usercommentrecycler.setAdapter(commentsAdopter);
                                                                                comment_count.setText(usercommentslist.size() + " Comments");


                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                                Log.d("Error", t.getMessage());
                                                                            }
                                                                        });

                                                                    } else {

                                                                        Toast.makeText(getActivity(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                                                    }
                                                                }

                                                                @Override
                                                                public void failure(RetrofitError error) {

                                                                }
                                                            });
                                                        } else if (type.equalsIgnoreCase("live")) {
                                                            Api.getClient().getupdateLivecomment(user_id, videoId, comment, "LiveStream_play", usercommentslist.get(position).getId(), new Callback<AddComment>() {

                                                                @Override
                                                                public void success(AddComment addComment, Response response) {

                                                                    addComment = addComment;

                                                                    if (addComment.getStatus().equalsIgnoreCase("true")) {

                                                                        Toast.makeText(getActivity(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                                                        dialog1.cancel();
                                                                        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "LiveStream_play");
                                                                        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                                JSONResponse jsonResponse = response.body();

                                                                                pro.setVisibility(View.GONE);
                                                                                img.setVisibility(View.VISIBLE);
                                                                                edit1.setText("");

                                                                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                                                commentsAdopter = new CommentsAdopter(usercommentslist);
                                                                                usercommentrecycler.setAdapter(commentsAdopter);
                                                                                comment_count.setText(usercommentslist.size() + " Comments");


                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                                Log.d("Error", t.getMessage());
                                                                            }
                                                                        });

                                                                    } else {

                                                                        Toast.makeText(getActivity(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();

                                                                    }
                                                                }

                                                                @Override
                                                                public void failure(RetrofitError error) {

                                                                }
                                                            });
                                                        }


                                                    }
                                                }
                                            });

                                        }
                                    });


                                    tx2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            Api.getClient().getDeleteComment(usercommentslist.get(position).getId(), new Callback<AddComment>() {

                                                @Override
                                                public void success(AddComment addComment, Response response) {

                                                    addComment = addComment;
                                                    Toast.makeText(getActivity(), "" + addComment.getMessage(), Toast.LENGTH_LONG).show();
                                                    dialog1.cancel();

                                                    if (type.equalsIgnoreCase("video")){
                                                        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "play_videos");
                                                        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                            @Override
                                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                JSONResponse jsonResponse = response.body();


                                                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                                commentsAdopter = new CommentsAdopter(usercommentslist);
                                                                usercommentrecycler.setAdapter(commentsAdopter);
                                                                if (usercommentslist.size()==0){
                                                                    comment_count.setVisibility(View.GONE);
                                                                    nocomment.setVisibility(View.VISIBLE);
                                                                }else{
                                                                    comment_count.setText(usercommentslist.size() + " Comments");
                                                                    comment_count.setVisibility(View.VISIBLE);
                                                                    nocomment.setVisibility(View.GONE);
                                                                }


                                                            }

                                                            @Override
                                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                Log.d("Error", t.getMessage());
                                                            }
                                                        });
                                                    } else if (type.equalsIgnoreCase("live")) {
                                                        Call<JSONResponse> movieres = ApiClient.getInstance1().getApi().getUsercommentslive(videoId, "LiveStream_play");
                                                        movieres.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                            @Override
                                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                JSONResponse jsonResponse = response.body();


                                                                usercommentslist = new ArrayList<>(Arrays.asList(jsonResponse.getComment()));
                                                                commentsAdopter = new CommentsAdopter(usercommentslist);
                                                                usercommentrecycler.setAdapter(commentsAdopter);
                                                                if (usercommentslist.size()==0){
                                                                    comment_count.setVisibility(View.GONE);
                                                                    nocomment.setVisibility(View.VISIBLE);
                                                                }else{
                                                                    comment_count.setText(usercommentslist.size() + " Comments");
                                                                    comment_count.setVisibility(View.VISIBLE);
                                                                    nocomment.setVisibility(View.GONE);
                                                                }


                                                            }

                                                            @Override
                                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                Log.d("Error", t.getMessage());
                                                            }
                                                        });
                                                    }

                                                }

                                                @Override
                                                public void failure(RetrofitError error) {

                                                }
                                            });
                                        }
                                    });


                                }

                            }
                        }
                    }
                }));


        return root;
    }
}