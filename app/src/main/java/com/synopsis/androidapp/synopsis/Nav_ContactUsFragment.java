package com.synopsis.androidapp.synopsis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Kumar on 8/23/2016.
 */
public class Nav_ContactUsFragment  extends Fragment{
    LinearLayout email_us_layout,callus_layout,visit_website_layout;
    @Nullable
    @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=  inflater.inflate(R.layout.contact_us,container,false);
        email_us_layout=(LinearLayout)view.findViewById(R.id.email_us_layout);
        callus_layout=(LinearLayout)view.findViewById(R.id.callus_layout);
        visit_website_layout=(LinearLayout)view.findViewById(R.id.visit_website_layout);


        email_us_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@synopsissolutions.com"});
                Intent mailer = Intent.createChooser(intent, null);
                startActivity(mailer);
            }
        });

callus_layout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918885556321"));
        startActivity(intent);
    }
});
        visit_website_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.synopsissolutions.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });





        return view;
    }
}
