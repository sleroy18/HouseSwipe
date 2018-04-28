package com.seancs445.houseswipe;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

public class Navigation {
    private static final String USER = "User";
    private User user;

    public Navigation(){

    }
    public boolean Navigate(MenuItem item, User user, Context ctx){
        this.user = user;
        switch (item.getItemId()) {
            case R.id.landingTab:
                Landing(ctx);
                return true;
            case R.id.settingsTab:
                Settings(ctx);
                return true;
//            case R.id.housesTab:
//                Houses(ctx);
//                return true;
            default:
                return false;
        }
    }

    public void Landing(Context ctx){
        Intent intent = new Intent(ctx, LandingPageActivity.class);
        intent.putExtra(USER, user);
        ctx.startActivity(intent);
    }

    public void Settings(Context ctx){
        Intent intent = new Intent(ctx, SettingsActivity.class);
        intent.putExtra(USER, user);
        ctx.startActivity(intent);
    }

    public void Houses(Context ctx){
//        Intent intent = new Intent(ctx, SettingsActivity.class);
//        ctx.startActivity(intent);
    }



}
