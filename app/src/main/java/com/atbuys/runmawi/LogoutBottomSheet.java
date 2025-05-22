package com.atbuys.runmawi;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutBottomSheet extends BottomSheetDialogFragment {

    CardView cancel, logout;
    TextView title, desc, btn_text;
    String page_text,user_id;

    public LogoutBottomSheet(String page_text) {
        this.page_text = page_text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_logout_bottom_sheet, container, false);

        SharedPreferences prefs = this.getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);


        cancel = (CardView) root.findViewById(R.id.cancel);
        logout = (CardView) root.findViewById(R.id.logout);
        title = (TextView) root.findViewById(R.id.title);
        desc = (TextView) root.findViewById(R.id.desc);
        btn_text = (TextView) root.findViewById(R.id.btn_text);
        title.setText(page_text);
        //btn_text.setText(page_text);

        if (page_text.equalsIgnoreCase("Logout")) {
            desc.setText("Are you sure you want to log out?");

        }


        else if (page_text.equalsIgnoreCase("Cancel Premium")) {
            desc.setText("Are you sure you want to cancel premium?");
        }

        else {
            desc.setText("Are you sure you want to delete account?");
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 //   AdaptiveExoplayer.getInstance().getDownloadManager().removeAllDownloads();

                    if (page_text.equalsIgnoreCase("Logout")) {

                        // Show a logout toast message
                        Toast.makeText(getContext(), "You have been logged out", Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();  // Clears all data from SharedPreferences
                        editor.apply();   // Ensure data is written to disk
                        // Redirect to OTPLoginActivity
                       Intent in = new Intent(getContext(), MailandOtpLoginActivity.class);
                        startActivity(in);
                        getActivity().finish();

                        // Optional: Close the current dialog or activity
                        dismiss();

                    }

                    else if (page_text.equalsIgnoreCase("Cancel Premium")) {
                    Call<JSONResponse> calcel_api=ApiClient.getInstance1().getApi().cancelSubscrption(user_id);
                    calcel_api.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                            JSONResponse jsonResponse= response.body();
                            user user=jsonResponse.getUser();
                            if (jsonResponse.getStatus().equalsIgnoreCase("true")){
                                Toast.makeText(getActivity(), ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putBoolean(sharedpreferences.login, true);
                                editor.putString(sharedpreferences.user_id, user_id);
                                editor.putString(sharedpreferences.role, user.getRole());
                                editor.apply();
                                editor.commit();
                                Intent in = new Intent(getActivity(), HomePageActivitywithFragments.class);
                                startActivity(in);
                                dismiss();
                            }else {
                                Toast.makeText(getActivity(), ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            dismiss();
                        }
                    });
                }

                    else {
                    Call<JSONResponse> api = ApiClient.getInstance1().getApi().deleteAccount(user_id);
                    api.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                              //  AdaptiveExoplayer.getInstance().getDownloadManager().removeAllDownloads();

                                Toast.makeText(getContext(), " "+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                SharedPreferences prefs = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();  // Clears all data from SharedPreferences
                                editor.apply();   // Ensure data is written to disk
                                // Redirect to OTPLoginActivity
                                Intent in = new Intent(getContext(), MailandOtpLoginActivity.class);
                                startActivity(in);
                                getActivity().finish();

                                // Optional: Close the current dialog or activity
                                dismiss();
                            } else {
                                Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            dismiss();
                        }
                    });
                }

            }
        });


        return root;
    }
}