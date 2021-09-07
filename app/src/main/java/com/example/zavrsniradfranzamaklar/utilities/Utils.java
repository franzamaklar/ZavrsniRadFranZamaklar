package com.example.zavrsniradfranzamaklar.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;

public class Utils {

    public static void switchActivity(Activity oldActivity, Class<? extends Activity> newActivityClass, boolean killOld) {
        Intent intent = new Intent(oldActivity, newActivityClass);

        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(oldActivity, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        oldActivity.startActivity(intent, bundle);

        if (killOld) oldActivity.finish();
    }
}
