package com.synopsis.androidapp.synopsis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by Kumar on 9/20/2016.
 */
public class Verify_Benefits_Fragment  extends Fragment{
    Button verify_tutorial_OkBtn;
    CheckBox verify_tutorial_end_of_visitsCB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.why_verify,container,false);
        verify_tutorial_OkBtn=(Button)view.findViewById(R.id.verify_tutorial_OkBtn);
        verify_tutorial_end_of_visitsCB=(CheckBox)view.findViewById(R.id.verify_tutorial_end_of_visitsCB);
        verify_tutorial_OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(  verify_tutorial_end_of_visitsCB.isChecked())
              {
                  SharedPreferences prefs = getActivity().getSharedPreferences("Login_details", getActivity().MODE_PRIVATE);
                  SharedPreferences.Editor editor = prefs.edit();
                  editor.remove("Verify_tutorial_visited");
                  editor.putBoolean("Verify_tutorial_visited",true);// this value is reset to true on visiting the verify tutorial fragment to avoid further visits of the same.
                  editor.commit();
              }
                android.support.v4.app.Fragment fragment = new VerifyClass();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }
                fragmentTransaction.remove(fragment);
                fragmentTransaction.replace(R.id.dashboard_content_layout, fragment).addToBackStack("tag").commit();
            }
        });
        return view;
    }
}
