package com.ajinkya.prettymeal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.ajinkya.prettymeal.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_page);

        Element versionElement = new Element();
        versionElement.setTitle("Version 6.2");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.app_logo)
                .setDescription("PrettyMeal is a food app that offers numerous mess options in a single monthly subscription. PrettyMeal is an application that helps a mess owner gain more customers while also providing excellent meals to customers on a daily basis. Instead of going to the same mess, users can access all of the messes in their specific region.")
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("kardileajinkya@gmail.com")
                .addTwitter("ajinkya_kardile")
                .addPlayStore("com.ajinkya.prettymeal")
                .addGitHub("Ajinkya-Kardile")
                .addInstagram("__ajinkya_kardile__")
                .addItem(getCopyRightsElement())
                .create();
        setContentView(aboutPage);

    }


    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setAutoApplyIconTint(true);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUsPage.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;

    }
}