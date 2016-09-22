package com.synopsis.androidapp.synopsis;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Kumar on 9/22/2016.
 */
public class Nav_shareFragment extends Fragment {
    ImageView fbIconIV,tw_icon,ln_icon,google_icon,whatsapIconIV;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.share_layout,container,false);

        whatsapIconIV=(ImageView)view.findViewById(R.id.whatsapIconIV);
        whatsapIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
                try {
                    getActivity().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(),"Whatsapp have not been installed.",Toast.LENGTH_LONG).show();
                }
            }
        });
        fbIconIV =(ImageView)view.findViewById(R.id.fbIconIV);
        fbIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setComponent(new ComponentName("com.facebook.katana",
                        "com.facebook.katana.activity.composer.ImplicitShareIntentHandler"));

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,  "hai, the text here");
            }
        });
        tw_icon=(ImageView)view.findViewById(R.id.twitterIconIV);
        tw_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Text I want to share.";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
            }
        });
        ln_icon =(ImageView)view.findViewById(R.id.inIconIV);
        ln_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://you"));
                final PackageManager packageManager = getContext().getPackageManager();
                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=you"));
                }
                startActivity(intent);
            }
        });
        google_icon =(ImageView)view.findViewById(R.id.googleIconIV);
        google_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "https://plus.google.com/communities/107847486351510098159" ) );
                intent.setPackage( "com.google.android.apps.plus" );
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity( intent );
                }
            }
        });


        return view;

    }
}
