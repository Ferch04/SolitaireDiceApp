package codecademy.com.solitairedice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static codecademy.com.solitairedice.R.color.black_1;
import static codecademy.com.solitairedice.R.color.gray_99;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
        if (item.getItemId() == R.id.Score){
                SharedPreferences highScore =
                        getApplicationContext().getSharedPreferences("ScoreHistory", MODE_PRIVATE);
                Toast.makeText(this, "Highest score: " +
                                highScore.getInt("High", 0)
                        , Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        //Do whatever you want before the back button should trigger
        super.onBackPressed();  // call this only if you want to close the app
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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
        window.chosenDices = 0;
        window.dices = new int[] {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,  R.drawable.dice4,
                R.drawable.dice5, R.drawable.dice6 };
        window.purple = getResources().getDrawable(R.color.purple_700);
        window.bblack = getResources().getDrawable(R.color.black);
        window.teal = getResources().getDrawable(R.color.teal_700);
        window.white = getResources().getDrawable(R.color.white);

        scoring = new Scoring();
        scoring.NewScore();

        roll.setOnClickListener(this);
        diceOne.setOnClickListener(this);
        diceTwo.setOnClickListener(this);
        diceThree.setOnClickListener(this);
        diceFour.setOnClickListener(this);
        diceFive.setOnClickListener(this);
    }
    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
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
                if ( currentState == MainActivity.rollState.Rolled ||
                        currentState == MainActivity.rollState.Chosen ) {
                    window.SelectDice(findViewById(v.getId()), getResources().getDrawable(R.drawable.custom_shape));
                    SetEnableRoll();
                }
                break;
        }

    }
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void RollDicesMain(SolitaireWindow sWindow){

        switch (currentState){
            case Idle:
                sWindow.CleanChoices();
                RollDiceStatus(false);
                sWindow.RollDicesNow(scoring);
                roll.setText("Play & Roll");
                currentState = rollState.Rolled;
                break;
            case Chosen:
                if(sWindow.FillNumbers(scoring, totalScore)){
                    sWindow.CleanChoices();
                    RollDiceStatus(false);
                    if (scoring.state != Scoring.ScoreState.Finish){
                        sWindow.RollDicesNow(scoring);
                        currentState = rollState.Rolled;
                    }
                    else{
                        EndingGame(sWindow);
                    }
                }else
                {
                    ShowMessage("Invalid throw away," +
                            "\nplease select a valid one");
                }
                break;
            case Rolled:
                // do nothing and wait for all dices to be chosen
                break;
            case EndGame:
                sWindow.EndGame();
                scoring.NewScore();
                totalScore.setText("Total: ");
                roll.setText("Roll");
                currentState = rollState.Idle;
                break;
            case StartGame:
                sWindow.CleanScoring(getResources().getColor(R.color.gray_99));
                roll.setText("Roll");
                currentState = rollState.Idle;
                break;
            default:
                break;
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void EndingGame(SolitaireWindow sWindow){
        sWindow.EndingGame();
        RollDiceStatus(false);
        ShowMessage("End of Game" +
                "\nScore: " + scoring.TotalScore());
        roll.setText("Start");
        RollDiceStatus(true);
        SaveHighestScore();

        currentState = rollState.EndGame;
    }

    public void SetEnableRoll(){
        if (window.IsAllChosen()){
            RollDiceStatus(true);
            currentState = MainActivity.rollState.Chosen;
        }
        else{
            RollDiceStatus(false);
            currentState = MainActivity.rollState.Rolled;
        }
    }
    @SuppressLint("ResourceAsColor")
    public void RollDiceStatus(boolean state){
        Button rollButton  = findViewById(R.id.rollDices);
        rollButton.setEnabled(state);

        if(state){
            rollButton.setBackgroundColor(black_1);
        } else {
            rollButton.setBackgroundColor(gray_99);
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
}