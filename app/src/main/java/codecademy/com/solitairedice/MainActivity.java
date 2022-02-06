package codecademy.com.solitairedice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Random random = new Random();
    boolean freeThrow = false;
    int chosenDices = 0;
    int[] dices = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,  R.drawable.dice4,
            R.drawable.dice5, R.drawable.dice6 };

    public class SolitaireWindow{
        List<Integer> aDicesInt;
        ImageView[] aDices;
        ImageView[] aChosenDices;
        TextView[] aChosenText;
        TextView[] aNumbersText;
        TextView[] aThrowsText;
        int[] chosenInt;

        public void CleanScoring(){
            for (TextView score : aNumbersText){
                score.setText("");
                score.setTextColor(getResources().getColor(R.color.gray_99));
            }
        }
        @SuppressLint("DefaultLocale")
        public void CleanThrowAway(){
            int i = 1;
            for(TextView t_away : aThrowsText){
                t_away.setTextColor(getResources().getColor(R.color.gray_99));
                t_away.setText(String.format("Throw away %d", i));
                t_away.setTag(0);
                i++;
            }
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        public void CleanDices(){
            for (ImageView imDice : aDices) {
                imDice.setImageResource(android.R.drawable.gallery_thumb);
                imDice.setBackground(getResources().getDrawable(R.color.white));
                imDice.setTag(0);
            }
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        public void CleanChoices(){
            // removing selection of choices
            for (ImageView imChosenDice : aChosenDices) {
                imChosenDice.setTag(0);
                imChosenDice.setImageResource(android.R.drawable.gallery_thumb);
                imChosenDice.setBackground(getResources().getDrawable(R.color.white));
            }
            aDicesInt.clear();
            chosenDices = 0;
            RollDiceStatus(false);
        }
    }


    Scoring scoring;

    enum rollState{
        StartGame,
        Idle,
        Rolled,
        Chosen,
        EndGame
    }
    rollState currentState = rollState.StartGame;

    TextView totalScore ;
    Button roll;

    SolitaireWindow window;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Score:
                SharedPreferences highScore =
                        getApplicationContext().getSharedPreferences("ScoreHistory", MODE_PRIVATE);
                Toast.makeText(this, "Highest score: " +
                                highScore.getInt("High", 0)
                        , Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // region final definition
        totalScore = findViewById(R.id.textTotalScore);
        roll  = findViewById(R.id.rollDices);
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

        window = new SolitaireWindow();
        window.aDices = new ImageView[] {diceOne, diceTwo, diceThree, diceFour, diceFive};
        window.aChosenDices = new ImageView[]
                {choseOneOne, choseOneTwo, choseTwoOne, choseTwoTwo, choseThrowaway};
        window.aChosenText = new TextView[] {choseOne, choseTwo};
        window.aNumbersText = new TextView[] {numberTwo, numberThree, numberFour, numberFive, numberSix,
                numberSeven, numberEight, numberNine, numberTen, numberEleven, numberTwelve};
        window.aThrowsText = new TextView[] {throwOne, throwTwo, throwThree};
        window.chosenInt = new int[] {0, 0};
        window.aDicesInt = new ArrayList<>();
        window.CleanThrowAway();
        window.CleanDices();

        scoring = new Scoring();
        scoring.NewScore();

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
                RollDicesMain(window);
                break;
            case R.id.DiceOne:
            case R.id.DiceTwo:
            case R.id.DiceThree:
            case R.id.DiceFour:
            case R.id.DiceFive:
                SelectDice(findViewById(v.getId()), window);
                break;
        }

    }
    @SuppressLint("SetTextI18n")
    public void RollDicesMain(SolitaireWindow sWindow){

        switch (currentState){
            case Idle:
                sWindow.CleanChoices();
                RollDicesNow(sWindow);
                roll.setText("Play & Roll");
                currentState = rollState.Rolled;
                break;
            case Chosen:
                if(FillNumbers(sWindow)){
                    sWindow.CleanChoices();
                    if (scoring.state != Scoring.ScoreState.Finish){
                        RollDicesNow(sWindow);
                        currentState = rollState.Rolled;
                    }
                    else{
                        EndGame(sWindow);
                    }
                }
                break;
            case Rolled:
                // do nothing and wait for all dices to be chosen
                break;
            case EndGame:
                sWindow.CleanScoring();
                sWindow.CleanThrowAway();
                scoring.NewScore();
                chosenDices = 0;
                freeThrow = false;
                totalScore.setText("Total: ");
                roll.setText("Roll");
                currentState = rollState.Idle;
                break;
            case StartGame:
                sWindow.CleanScoring();
                roll.setText("Roll");
                currentState = rollState.Idle;
                break;
            default:
                break;
        }
    }
    public void EndGame(SolitaireWindow sWindow){
        sWindow.CleanChoices();
        sWindow.CleanDices();
        ShowMessage("End of Game" +
                "\nScore: " + scoring.TotalScore());
        roll.setText("Start");
        RollDiceStatus(true);
        SaveHighestScore();

        currentState = rollState.EndGame;
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void RollDicesNow(SolitaireWindow window){
        int randDice;
        // removing selection of dices
        for (ImageView imDice : window.aDices) {
            randDice = random.nextInt(6);
            window.aDicesInt.add(randDice + 1);

            imDice.setImageResource(dices[randDice]);
            imDice.setBackground(getResources().getDrawable(R.color.white));
            imDice.setTag(randDice + 1);
        }
        IsFreeThrow(window.aDicesInt);
    }
    public void AddChosenDicesSum(SolitaireWindow window){
        int numOne = 0;
        int numTwo = 0;

        for(int i=0; i < window.aChosenDices.length - 1; i++){
            if((Integer)window.aChosenDices[i].getTag() != 0) {
                int diceValue = (Integer)window.aChosenDices[i].getTag();
                if (i == 1 || i == 0) {
                    numOne = numOne + diceValue;
                } else if (i == 2 || i == 3) {
                    numTwo += diceValue;
                }
            }
        }
        window.aChosenText[0].setText(String.valueOf(numOne));
        window.aChosenText[1].setText(String.valueOf(numTwo));

        window.chosenInt[0] = numOne;
        window.chosenInt[1] = numTwo;
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
    public boolean FillNumbers(SolitaireWindow sWindow) {

        int throwAway = (Integer) sWindow.aChosenDices[4].getTag();
        boolean isValid = ValidateThrowAway(throwAway);

        // Check if throw away is valid
        if (isValid) {
            scoring.AddNewNumber(sWindow.chosenInt[0]);
            scoring.AddNewNumber(sWindow.chosenInt[1]);

            window.aNumbersText[sWindow.chosenInt[0] - 2].setText(String.valueOf(
                    scoring.GetCount(sWindow.chosenInt[0])));
            window.aNumbersText[sWindow.chosenInt[1] - 2].setText(String.valueOf(
                    scoring.GetCount(sWindow.chosenInt[1])));

            window.aNumbersText[sWindow.chosenInt[0] - 2].
                    setTextColor(getResources().getColor(R.color.black));
            window.aNumbersText[sWindow.chosenInt[1] - 2]
                    .setTextColor(getResources().getColor(R.color.black));

            totalScore.setText("Total : " + scoring.TotalScore());

            FillThrowAway((Integer) sWindow.aChosenDices[4].getTag(),
                    sWindow.aThrowsText);
        } else
        {
            ShowMessage("Invalid throw away," +
                    "\nplease select a valid one");
        }

        return isValid;
    }
    public void FillThrowAway(int num, TextView[] txtThrows){

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
    public void IsFreeThrow(List<Integer> iDices){
        boolean throwFounded = false;

        if(scoring.state == Scoring.ScoreState.ThreeThrowAway){
            for(int dice : iDices){
                throwFounded |= scoring.IsThrowAway(dice);
            }
            // if at lease on throw away its founded
            // free throw should be false, otherwise it will be true
            if(!throwFounded) {
                freeThrow = true;
                Log.d("fercho", "free throw");
                Toast.makeText(this, "Free throw", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void SelectDice(ImageView dice, SolitaireWindow sWindow){
        if ( currentState == rollState.Rolled ||
            currentState == rollState.Chosen ) {
            int diceTag = (Integer) dice.getTag();
            if (diceTag < 9) {
                dice.setBackground(getResources().getDrawable(R.color.gray_5));
                dice.setTag(diceTag + 10);
                // diceFive.setBackground(getResources().getDrawable(android.R.drawable.gallery_thumb))
                ChoseDice(dice, diceTag, sWindow.aChosenDices);
            } else {
                dice.setTag(diceTag - 10);
                dice.setBackground(getResources().getDrawable(R.color.white));
                RemoveDice(dice.getDrawable(), sWindow.aChosenDices);
            }
            AddChosenDicesSum(sWindow);
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void ChoseDice(ImageView dice, int diceNum, ImageView[] imChosenDices){
        int index = 0;
        for (ImageView imChosenDice : imChosenDices) {
            if ((Integer)imChosenDice.getTag() == 0) {
                int color = DefineColor(index);
                imChosenDice.setBackground(getResources().getDrawable(color));
                dice.setBackground(getResources().getDrawable(color));

                imChosenDice.setImageDrawable(dice.getDrawable());
                imChosenDice.setTag(diceNum);
                chosenDices++;
                SetEnableRoll(imChosenDices);
                break;
            }
            index++;
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void RemoveDice(Drawable diceImage, ImageView[] imChosenDices){
        for (ImageView imChosenDice : imChosenDices) {
            if (imChosenDice.getDrawable() == diceImage) {
                imChosenDice.setImageResource(android.R.drawable.gallery_thumb);
                imChosenDice.setTag(0);
                imChosenDice.setBackground(getResources().getDrawable(R.color.white));
                chosenDices--;
                SetEnableRoll(imChosenDices);
                break;
            }
        }
    }

    public void SetEnableRoll(ImageView[] imChosenDices){
        if (chosenDices == imChosenDices.length){
            RollDiceStatus(true);
            currentState = rollState.Chosen;
        }
        else{
            RollDiceStatus(false);
            currentState = rollState.Rolled;
        }
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


    public void ShowMessage(String message){
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alert.show();
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
    private int DefineColor(int index){
        int color = R.color.black;

        if(index < 2 ){
            color = R.color.teal_700;
        } else if (index < 4){
            color = R.color.purple_700;
        }
        return color;
    }
}