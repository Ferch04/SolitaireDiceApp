package codecademy.com.solitairedice;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SolitaireWindow{
    List<Integer> aDicesInt;
    ImageView[] aDices;
    ImageView[] aChosenDices;
    TextView[] aChosenText;
    TextView[] aNumbersText;
    TextView[] aThrowsText;
    int[] chosenInt;

    public void CleanScoring(int color){
        for (TextView score : aNumbersText){
            score.setText("");
            score.setTextColor(color);
        }
    }
    @SuppressLint("DefaultLocale")
    public void CleanThrowAway(int color){
        int i = 1;
        for(TextView t_away : aThrowsText){
            t_away.setTextColor(color);
            t_away.setText(String.format("Throw away %d", i));
            t_away.setTag(0);
            i++;
        }
    }
    public void CleanDices(Drawable background){
        for (ImageView imDice : aDices) {
            imDice.setImageResource(android.R.drawable.gallery_thumb);
            imDice.setBackground(background);
            imDice.setTag(0);
        }
    }
    public void CleanChoices(Drawable background){
        // removing selection of choices
        for (ImageView imChosenDice : aChosenDices) {
            imChosenDice.setTag(0);
            imChosenDice.setImageResource(android.R.drawable.gallery_thumb);
            imChosenDice.setBackground(background);
        }
        aDicesInt.clear();

    }
}
