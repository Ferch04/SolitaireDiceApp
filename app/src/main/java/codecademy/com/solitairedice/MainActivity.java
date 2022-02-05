package codecademy.com.solitairedice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Random random = new Random();
    boolean freeThrow = false;
    int chosenDices = 0;
    int[] dices = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,  R.drawable.dice4,
            R.drawable.dice5, R.drawable.dice6 };

    int[] chosen;
    List<Integer> iDices;
    ImageView[] imDices ;
    ImageView[] imChosenDices;
    TextView[] txtChosen;
    TextView[] txtNumbers;
    TextView[] txtThrows;
    Scoring scoring;

    enum rollState{
        Starting,// todo: new state called start game
        Idle,
        Rolled,
        Chosen,
        EndGame
    }
    rollState currentState = rollState.Starting;

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

        final TextView throwOne = findViewById(R.id.textThrowAwOne);
        final TextView throwTwo = findViewById(R.id.textThrowAwTwo);
        final TextView throwThree = findViewById(R.id.textThrowAwThree);
        // endregion
        chosen = new int[] {0, 0};
        txtChosen = new TextView[] {choseOne, choseTwo};
        txtNumbers = new TextView[] {numberTwo, numberThree, numberFour, numberFive, numberSix,
                numberSeven, numberEight, numberNine, numberTen, numberEleven, numberTwelve};
        txtThrows = new TextView[] {throwOne, throwTwo, throwThree};
        imDices = new ImageView[] {diceOne, diceTwo, diceThree, diceFour, diceFive};
        imChosenDices = new ImageView[]
                {choseOneOne, choseOneTwo, choseTwoOne, choseTwoTwo, choseThrowaway};
        scoring = new Scoring();
        scoring.NewScore();
        iDices = new ArrayList<Integer>();

        for (ImageView imDice : imDices) {
            imDice.setTag((int)0);
        }
        for(TextView txThrow : txtThrows){
            txThrow.setTag((int)0);
        }

        Button getScore = findViewById(R.id.getScore);
        getScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences highScore =
                        getApplicationContext().getSharedPreferences("ScoreHistory", MODE_PRIVATE);
                ShowMessage(String.valueOf(highScore.getInt("High", 0)));
            }
        });

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

        switch (currentState){
            case Starting:
                CleanScoring();
                DicesClickable(true);
                RollDiceText("Roll");
                currentState = rollState.Idle;
                break;
            case Idle:
                cleanChoices();
                rollDicesNow();
                RollDiceText("Play & Roll");
                currentState = rollState.Rolled;
                break;
            case Chosen:
                if(fillNumbers()){
                    cleanChoices();
                    addChosenDicesSum();
                    if (scoring.state != Scoring.ScoreState.Finish){
                        rollDicesNow();
                        currentState = rollState.Rolled;
                    }
                    else{
                        EndGame();
                    }
                }
                break;
            case Rolled: // do nothing and wait for all dices to be chosen
                break;
            case EndGame:
                DicesClickable(false);
                CleanScoring();
                CleanThrowAway();
                scoring.NewScore();
                chosenDices = 0;
                freeThrow = false;
                TotalScore("Total: ");
                currentState = rollState.Starting;
            default:
                break;
        }
    }
    public void DicesClickable(boolean state){
        for (ImageView imDice : imDices) {
            imDice.setEnabled(state);
        }
    }
    public void RollDiceText(String text){
        Button roll = findViewById(R.id.rollDices);
        roll.setText(text);
    }
    public void RollDiceStatus(boolean state){
        Button roll = findViewById(R.id.rollDices);
        roll.setEnabled(state);

        if(state){
            roll.setBackgroundColor(getResources().getColor(R.color.black_1));
        } else {
            roll.setBackgroundColor(getResources().getColor(R.color.gray_99));
        }
    }
    public void TotalScore(String score){
        TextView totalScore = findViewById(R.id.textTotalScore);
        totalScore.setText(score);
    }
    public void ShowMessage(String message){
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }
    public void EndGame(){
        cleanChoices();
        cleanDices();
        ShowMessage("End of Game" +
                "\nScore: " + scoring.TotalScore());
        RollDiceText("Start");
        RollDiceStatus(true);
        SaveHighestScore();

        currentState = rollState.EndGame;
    }
    public void SaveHighestScore(){
        // shared preferences. todo: encryptedSharedPreferences
        SharedPreferences highScore =
                getApplicationContext().getSharedPreferences("ScoreHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = highScore.edit();

        int last_high_score = highScore.getInt("High", 0);
        if(scoring.IntTotalScore() > last_high_score ) {
            editor.putInt("High", scoring.IntTotalScore());
            editor.apply();
        }
    }
    public boolean ValidateThrowAway(int throwAway){

        boolean isValid = true;

        if(freeThrow){
            freeThrow = false;
        } else{
            isValid = scoring.AddThrowAway(throwAway);
        }

        return isValid;
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public boolean fillNumbers() {

        int throwAway = (Integer) imChosenDices[4].getTag();
        boolean isValid = ValidateThrowAway(throwAway);

        // Check if throw away is valid
        if (isValid) {
            scoring.AddNewNumber(chosen[0]);
            scoring.AddNewNumber(chosen[1]);

            txtNumbers[chosen[0] - 2].setText(String.valueOf(
                    scoring.GetCount(chosen[0])));
            txtNumbers[chosen[1] - 2].setText(String.valueOf(
                    scoring.GetCount(chosen[1])));

            txtNumbers[chosen[0] - 2].setTextColor(getResources().getColor(R.color.black));
            txtNumbers[chosen[1] - 2].setTextColor(getResources().getColor(R.color.black));

            TotalScore("Total : " + scoring.TotalScore());

            FillThrowAway((Integer) imChosenDices[4].getTag());
        } else
        {
            ShowMessage("Invalid throw away," +
                    "\nplease select a valid one");
        }

        return isValid;
    }
    public void FillThrowAway(int num){

        for(TextView throwA: txtThrows){
            int tag = (Integer)throwA.getTag();

            if( tag == num){
                throwA.setText(Html.fromHtml( scoring.GetThrowAway(num)));
                break;
            }else if( tag == 0){
                throwA.setTag(num);
                throwA.setText(scoring.GetThrowAway(num));
                throwA.setTextColor(getResources().getColor(R.color.black));
                break;
            }
        }
    }
    public void IsFreeThrow(){
        boolean throwFounded = false;

        Log.d("fercho", "state " + scoring.state);
        if(scoring.state == Scoring.ScoreState.ThreeThrowAway){
            for(int dice : iDices){
                throwFounded |= scoring.IsThrowAway(dice);
            }
            // if at lease on throw away its founded
            // free throw should be false, otherwise it will be true
            if(!throwFounded) {
                freeThrow = true;
                Log.d("fercho", "free throw");
                // todo: show that its a free throw
            }
        }
    }
    public void rollDicesNow(){
        int randDice = 0;
        // removing selection of dices
        for (ImageView imDice : imDices) {
            randDice = random.nextInt(6);
            iDices.add(randDice + 1);

            imDice.setImageResource(dices[randDice]);
            imDice.setBackground(getResources().getDrawable(R.color.white));
            imDice.setTag(randDice + 1);
        }
        IsFreeThrow();
    }
    public void CleanScoring(){
        for (TextView score : txtNumbers){
            score.setText("");
            score.setTextColor(getResources().getColor(R.color.gray_99));
        }
    }
    public void CleanThrowAway(){
        int i = 1;
        for(TextView t_away : txtThrows){
            t_away.setTextColor(getResources().getColor(R.color.gray_99));
            t_away.setText("Throw Away " + i);
            t_away.setTag((int)0);
            i++;
        }
    }
    public void cleanDices(){
        for (ImageView imDice : imDices) {
            imDice.setImageResource(android.R.drawable.gallery_thumb);
            imDice.setBackground(getResources().getDrawable(R.color.white));
            imDice.setTag((int)0);
        }
    }
    public void cleanChoices(){
        // removing selection of choices
        for (ImageView imChosenDice : imChosenDices) {
            imChosenDice.setTag((int)0);
            imChosenDice.setImageResource(android.R.drawable.gallery_thumb);
            imChosenDice.setBackground(getResources().getDrawable(R.color.white));
        }
        iDices.clear();
        chosenDices = 0;
        RollDiceStatus(false);
    }


    public void selectDice(ImageView dice){
        int diceTag = (Integer)dice.getTag();
        if(diceTag < 9)
        {
            dice.setBackground(getResources().getDrawable(R.color.gray_5));
            dice.setTag(diceTag + 10);
            // diceFive.setBackground(getResources().getDrawable(android.R.drawable.gallery_thumb))
            choseDice(dice, diceTag);
        }
        else
        {
            dice.setTag(diceTag - 10);
            dice.setBackground(getResources().getDrawable(R.color.white));
            removeDice(dice.getDrawable(), diceTag);
        }
        addChosenDicesSum();
    }
    public void choseDice(ImageView dice, int diceNum){
        int index = 0;
        for (ImageView imChosenDice : imChosenDices) {
            if ((Integer)imChosenDice.getTag() == 0) {
                int color = DefineColor(index);
                imChosenDice.setBackground(getResources().getDrawable(color));
                dice.setBackground(getResources().getDrawable(color));

                imChosenDice.setImageDrawable(dice.getDrawable());
                imChosenDice.setTag(diceNum);
                chosenDices++;
                setEnableRoll();
                break;
            }
            index++;
        }
    }
    private int DefineColor(int index){
        int color = R.color.black;

        if(index < 2 ){
            color = R.color.teal_700;
        } else if (index < 4){
            color = R.color.purple_700;
        }
        return color;
    }
    public void removeDice(Drawable diceImage, int diceNum){
        for (ImageView imChosenDice : imChosenDices) {
            if (imChosenDice.getDrawable() == diceImage) {
                imChosenDice.setImageResource(android.R.drawable.gallery_thumb);
                imChosenDice.setTag(0);
                imChosenDice.setBackground(getResources().getDrawable(R.color.white));
                chosenDices--;
                setEnableRoll();
                break;
            }
        }
    }


    public void setEnableRoll(){
        if (chosenDices == imChosenDices.length){
            RollDiceStatus(true);
            currentState = rollState.Chosen;
        }
        else{
            RollDiceStatus(false);
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