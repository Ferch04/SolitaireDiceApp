package myFirstApp.com.solitairedice;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class HighScore extends Activity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.highscorewindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*0.6), (int)(height*0.55));

        String highestScoreData = getIntent().getStringExtra("SomeData");
        assert highestScoreData != null;

        // ToDo: Change this to a regex
        String newString = highestScoreData.replace(",","\n  ");
        newString = newString.replace('"',' ');
        newString = newString.replace("{","");
        newString = newString.replace("}","");

        final TextView highScore = findViewById(R.id.highScoreText);
        highScore.setText(newString);
    }
}
