package codecademy.com.solitairedice;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static codecademy.com.solitairedice.R.color.black;
import static codecademy.com.solitairedice.R.color.gray_99;

public class SolitaireWindow{
    List<Integer> aDicesInt;
    ImageView[] aDices;
    ImageView[] aChosenDices;
    TextView[] aChosenText;
    TextView[] aNumbersText;
    TextView[] aThrowsText;
    int[] chosenInt;
    boolean freeThrow = false;

    public void CleanScoring(int color){
        for (TextView score : aNumbersText){
            score.setText("");
            score.setTextColor(color);
        }
    }
    @SuppressLint("ResourceAsColor")
    public void CleanThrowAway(){
        int i = 1;
        for(TextView t_away : aThrowsText){
            t_away.setTextColor(gray_99);
            t_away.setText(String.format("Throw away %d", i));
            t_away.setTag(0);
            i++;
        }
    }
    public void CleanDices(Drawable background){
        Clean(aDices, background);
    }
    public void CleanChoices(Drawable background){
        Clean(aChosenDices, background);
        aDicesInt.clear();
    }
    private void Clean(ImageView[] diceImage, Drawable background){
        for (ImageView image : diceImage) {
            image.setTag(0);
            image.setImageResource(android.R.drawable.gallery_thumb);
            image.setBackground(background);
        }
    }
    public void CleanChosenText(){
        aChosenText[0].setText("0");
        aChosenText[1].setText("0");
    }
    public void AddChosenDicesSum(){
        int numOne = 0;
        int numTwo = 0;

        for(int i=0; i < aChosenDices.length - 1; i++){
            if((Integer)aChosenDices[i].getTag() != 0) {
                int diceValue = (Integer)aChosenDices[i].getTag();
                if (i == 1 || i == 0) {
                    numOne = numOne + diceValue;
                } else if (i == 2 || i == 3) {
                    numTwo += diceValue;
                }
            }
        }
        aChosenText[0].setText(String.valueOf(numOne));
        aChosenText[1].setText(String.valueOf(numTwo));

        chosenInt[0] = numOne;
        chosenInt[1] = numTwo;
    }

    public boolean ValidateThrowAway(int throwAway, Scoring score){

        boolean isValid = true;

        if(freeThrow){
            freeThrow = false;
        } else{
            isValid = score.AddThrowAway(throwAway);
        }

        return isValid;
    }


    @SuppressLint("ResourceAsColor")
    public boolean FillNumbers(SolitaireWindow sWindow, Scoring score, TextView total) {

        int throwAway = (Integer) sWindow.aChosenDices[4].getTag();
        boolean isValid = sWindow.ValidateThrowAway(throwAway, score);

        // Check if throw away is valid
        if (isValid) {
            score.AddNewNumber(sWindow.chosenInt[0]);
            score.AddNewNumber(sWindow.chosenInt[1]);

            aNumbersText[sWindow.chosenInt[0] - 2].setText(String.valueOf(
                     score.GetCount(sWindow.chosenInt[0])));
            aNumbersText[sWindow.chosenInt[1] - 2].setText(String.valueOf(
                     score.GetCount(sWindow.chosenInt[1])));

            aNumbersText[sWindow.chosenInt[0] - 2].setTextColor(black);
            aNumbersText[sWindow.chosenInt[1] - 2].setTextColor(black);

            total.setText("Total : " + score.TotalScore());

            FillThrowAway((Integer) sWindow.aChosenDices[4].getTag(),
                    sWindow.aThrowsText, score);
        }

        return isValid;
    }
    @SuppressLint("ResourceAsColor")
    public void FillThrowAway(int num, TextView[] txtThrows, Scoring score){

        for(TextView throwA: txtThrows){
            int tag = (Integer)throwA.getTag();

            if( tag == num){
                throwA.setText(Html.fromHtml( score.GetThrowAway(num)));
                break;
            }else if( tag == 0){
                throwA.setTag(num);
                throwA.setText(score.GetThrowAway(num));
                throwA.setTextColor(black);
                break;
            }
        }
    }
}
