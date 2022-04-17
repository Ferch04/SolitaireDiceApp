package codecademy.com.solitairedice;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Random;

import static codecademy.com.solitairedice.R.color.gray_99;
import static codecademy.com.solitairedice.R.color.black;

public class SolitaireWindow{
    List<Integer> aDicesInt;
    ImageView[] aDices;
    ImageView[] aChosenDices;
    TextView[] aChosenText;
    TextView[] aNumbersText;
    TextView[] aThrowsText;
    int[] chosenInt;
    int[] dices;
    boolean freeThrow = false;
    int chosenDices;
    Random random = new Random();

    // colors
    Drawable purple;
    Drawable bblack;
    Drawable teal;
    Drawable white;

    public void CleanScoring(int color){
        for (TextView score : aNumbersText){
            score.setText("");
            score.setTextColor(color);
        }
    }
    @SuppressLint({"ResourceAsColor", "DefaultLocale"})
    public void CleanThrowAway(){
        int i = 1;
        for(TextView t_away : aThrowsText){
            t_away.setTextColor(gray_99);
            t_away.setText(String.format("Throw away %d", i));
            t_away.setTag(0);
            i++;
        }
    }
    public void CleanDices(){
        Clean(aDices);
    }
    public void CleanChoices(){
        Clean(aChosenDices);
        CleanChosenText();
        aDicesInt.clear();
        chosenDices = 0;
    }
    private void Clean(ImageView[] diceImage){
        for (ImageView image : diceImage) {
            image.setTag(0);
            image.setImageResource(android.R.drawable.gallery_thumb);
            image.setBackground(white);
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


    public void RollDicesNow( Scoring score){
        int randDice;
        // removing selection of dices
        for (ImageView imDice : aDices) {
            randDice = random.nextInt(6);
            aDicesInt.add(randDice + 1);

            imDice.setImageResource(dices[randDice]);
            imDice.setBackground(white);
            imDice.setTag(randDice + 1);
        }
        IsFreeThrow(aDicesInt, score);
    }
    public void IsFreeThrow(List<Integer> iDices, Scoring score){
        boolean throwFounded = false;

        if(score.state == Scoring.ScoreState.ThreeThrowAway){
            for(int dice : iDices){
                throwFounded |= score.IsThrowAway(dice);
            }
            // if at lease on throw away its founded
            // free throw should be false, otherwise it will be true
            if(!throwFounded) {
                freeThrow = true;
                Log.d("fercho", "free throw");
                // todo: Toast.makeText(this, "Free throw", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    public void SelectDice(ImageView dice){
        int diceTag = (Integer) dice.getTag();
        if (diceTag < 9) {
            dice.setTag(diceTag + 10);
            ChoseDice(dice, diceTag);
        } else {
            dice.setTag(diceTag - 10);
            dice.setBackground(white);
            RemoveDice(dice.getDrawable());
        }
        AddChosenDicesSum();
    }
    public boolean IsAllChosen(){
        return chosenDices == aChosenDices.length;
    }
    public void ChoseDice(ImageView dice, int diceNum){
        int index = 0;
        for (ImageView imChosenDice : aChosenDices) {
            if ((Integer)imChosenDice.getTag() == 0) {
                Drawable color = DefineColor(index);
                imChosenDice.setBackground(color);
                dice.setBackground(color);

                imChosenDice.setImageDrawable(dice.getDrawable());
                imChosenDice.setTag(diceNum);
                chosenDices++;
                break;
            }
            index++;
        }
    }
    @SuppressLint("ResourceAsColor")
    public void RemoveDice(Drawable diceImage){
        for (ImageView imChosenDice : aChosenDices) {
            if (imChosenDice.getDrawable() == diceImage) {
                imChosenDice.setImageResource(android.R.drawable.gallery_thumb);
                imChosenDice.setTag(0);

                imChosenDice.setBackground(white);
                chosenDices--;
                break;
            }
        }
    }


    private Drawable DefineColor(int index){
        Drawable color = bblack;

        if(index < 2 ){
            color = teal;
        } else if (index < 4){
            color = purple;
        }
        return color;
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    public boolean FillNumbers( Scoring score, TextView total) {

        int throwAway = (Integer) aChosenDices[4].getTag();
        boolean isValid = ValidateThrowAway(throwAway, score);

        // Check if throw away is valid
        if (isValid) {
            score.AddNewNumber(chosenInt[0]);
            score.AddNewNumber(chosenInt[1]);

            aNumbersText[chosenInt[0] - 2].setText(String.valueOf(
                     score.GetCount(chosenInt[0])));
            aNumbersText[chosenInt[1] - 2].setText(String.valueOf(
                     score.GetCount(chosenInt[1])));

            aNumbersText[chosenInt[0] - 2].setTextColor(black);
            aNumbersText[chosenInt[1] - 2].setTextColor(black);

            total.setText("Total : " + score.TotalScore());

            FillThrowAway(score);
        }

        return isValid;
    }
    @SuppressLint("ResourceAsColor")
    private void FillThrowAway( Scoring score){
        int num = (Integer) aChosenDices[4].getTag();

        for(TextView throwA: aThrowsText){
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
