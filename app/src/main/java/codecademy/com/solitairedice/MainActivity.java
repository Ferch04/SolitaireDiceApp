package codecademy.com.solitairedice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Random random = new Random();
    int chosenDices = 0;
    int[] dices = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,  R.drawable.dice4,
            R.drawable.dice5, R.drawable.dice6 };

    int[] chosen;
    ImageView[] imDices ;
    ImageView[] imChosenDices;
    TextView[] txtChosen;
    TextView[] txtNumbers;
    Scoring scoring;

    enum rollState{
        // todo: new state called start game
        Idle,
        Rolled,
        Chosen,
        EndGame
    }
    rollState currentState = rollState.Idle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // region final definition
        final Button roll = findViewById(R.id.rollDices);
        final ImageView diceOne = findViewById(R.id.DiceOne);
        final ImageView diceTwo = findViewById(R.id.DiceTwo);
        final ImageView diceThree = findViewById(R.id.DiceThree);
        final ImageView diceFour = findViewById(R.id.DiceFour);
        final ImageView diceFive = findViewById(R.id.DiceFive);
        final ImageView choseOneOne = findViewById(R.id.numOneOne);
        final ImageView choseOneTwo = findViewById(R.id.numOneTwo);
        final ImageView choseTwoOne = findViewById(R.id.numTwoOne);
        final ImageView choseTwoTwo = findViewById(R.id.numTwoTwo);
        final ImageView choseThrowaway = findViewById(R.id.numThrowaway);

        final TextView numberTwo = findViewById(R.id.textNumberTwo);
        final TextView numberThree = findViewById(R.id.textNumberThree);
        final TextView numberFour = findViewById(R.id.textNumberFour);
        final TextView numberFive = findViewById(R.id.textNumberFive);
        final TextView numberSix = findViewById(R.id.textNumberSix);
        final TextView numberSeven = findViewById(R.id.textNumberSeven);
        final TextView numberEight = findViewById(R.id.textNumberEight);
        final TextView numberNine = findViewById(R.id.textNumberNine);
        final TextView numberTen = findViewById(R.id.textNumberTen);
        final TextView numberEleven = findViewById(R.id.textNumberEleven);
        final TextView numberTwelve = findViewById(R.id.textNumberTwelve);

        final TextView choseOne = findViewById(R.id.textChoseOne);
        final TextView choseTwo = findViewById(R.id.textChoseTwo);

        // endregion
        chosen = new int[] {0, 0};
        txtChosen = new TextView[] {choseOne, choseTwo};
        txtNumbers = new TextView[] {numberTwo, numberThree, numberFour, numberFive, numberSix,
                numberSeven, numberEight, numberNine, numberTen, numberEleven, numberTwelve};
        imDices = new ImageView[] {diceOne, diceTwo, diceThree, diceFour, diceFive};
        imChosenDices = new ImageView[]
                {choseOneOne, choseOneTwo, choseTwoOne, choseTwoTwo, choseThrowaway};
        scoring = new Scoring();
        scoring.NewScore();

        for (ImageView imDice : imDices) {
            imDice.setTag((int)0);
        }

        roll.setOnClickListener(this);
        diceOne.setOnClickListener(this);
        diceTwo.setOnClickListener(this);
        diceThree.setOnClickListener(this);
        diceFour.setOnClickListener(this);
        diceFive.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rollDices:
                rollDicesMain();
                break;
            case R.id.DiceOne:
            case R.id.DiceTwo:
            case R.id.DiceThree:
            case R.id.DiceFour:
            case R.id.DiceFive:
                selectDice(findViewById(v.getId()));
                break;
        }

    }
    @SuppressLint("SetTextI18n")
    public void rollDicesMain(){
        Button roll = findViewById(R.id.rollDices);
        switch (currentState){
            case Idle:
                cleanChoices();
                rollDicesNow();
                roll.setText("Select & roll");
                currentState = rollState.Rolled;
                break;
            case Chosen:
                fillNumbers();
                cleanChoices();
                addChosenDicesSum();
                rollDicesNow();
                currentState = rollState.Rolled;
                break;
            case Rolled: // do nothing and wait for all dices to be chosen
                break;
            case EndGame:
            default:
                break;
        }
    }
    public void fillNumbers(){
        scoring.AddNewNumber(chosen[0]);
        scoring.AddNewNumber(chosen[1]);

        txtNumbers[chosen[0] - 2].setText(String.valueOf(
                scoring.GetCount(chosen[0])));
        txtNumbers[chosen[1] - 2].setText(String.valueOf(
                scoring.GetCount(chosen[1])));

        TextView totalScore = findViewById(R.id.textTotalScore);
        totalScore.setText(new StringBuilder().append("Total : ").append(scoring.TotalScore()).toString());
    }
    public void rollDicesNow(){
        int randDice = 0;
        // removing selection of dices
        for (ImageView imDice : imDices) {
            randDice = random.nextInt(6);
            imDice.setImageResource(dices[randDice]);
            imDice.setBackground(getResources().getDrawable(R.color.white));
            imDice.setTag(randDice + 1);
        }
    }
    public void cleanChoices(){
        Button roll = findViewById(R.id.rollDices);
        // removing selection of choices
        for (ImageView imChosenDice : imChosenDices) {
            imChosenDice.setTag((int)0);
            imChosenDice.setImageResource(android.R.drawable.gallery_thumb);
        }
        chosenDices = 0;
        roll.setEnabled(false);
    }


    public void selectDice(ImageView dice){
        int diceTag = (Integer)dice.getTag();
        if(diceTag < 9)
        {
            dice.setBackground(getResources().getDrawable(R.color.gray_5));
            dice.setTag(diceTag + 10);
            // diceFive.setBackground(getResources().getDrawable(android.R.drawable.gallery_thumb))
            choseDice(dice.getDrawable(), diceTag);
        }
        else
        {
            dice.setTag(diceTag - 10);
            dice.setBackground(getResources().getDrawable(R.color.white));
            removeDice(dice.getDrawable(), diceTag);
        }
        addChosenDicesSum();
    }
    public void choseDice(Drawable diceImage, int diceNum){
        for (ImageView imChosenDice : imChosenDices) {
            if ((Integer)imChosenDice.getTag() == 0) {
                imChosenDice.setImageDrawable(diceImage);
                imChosenDice.setTag(diceNum);
                chosenDices++;
                setEnableRoll();
                break;
            }
        }
    }
    public void removeDice(Drawable diceImage, int diceNum){
        for (ImageView imChosenDice : imChosenDices) {
            if (imChosenDice.getDrawable() == diceImage) {
                imChosenDice.setImageResource(android.R.drawable.gallery_thumb);
                imChosenDice.setTag(0);
                chosenDices--;
                setEnableRoll();
                break;
            }
        }
    }


    public void setEnableRoll(){
        Button roll = findViewById(R.id.rollDices);
        if (chosenDices == imChosenDices.length){
            roll.setEnabled(true);
            currentState = rollState.Chosen;
        }
        else{
            roll.setEnabled(false);
            currentState = rollState.Rolled;
        }
    }

    public void addChosenDicesSum(){
        int numOne = 0;
        int numTwo = 0;

        for(int i=0; i < imChosenDices.length - 1; i++){
            if((Integer)imChosenDices[i].getTag() != 0) {
                int diceValue = (Integer)imChosenDices[i].getTag();
                if (i == 1 || i == 0) {
                    numOne = numOne + diceValue;
                } else if (i == 2 || i == 3) {
                    numTwo += diceValue;
                }
            }
        }
        txtChosen[0].setText(String.valueOf(numOne));
        txtChosen[1].setText(String.valueOf(numTwo));

        chosen[0] = numOne;
        chosen[1] = numTwo;

    }

}