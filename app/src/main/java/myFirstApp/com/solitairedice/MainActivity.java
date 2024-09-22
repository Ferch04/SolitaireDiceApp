package myFirstApp.com.solitairedice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import static myFirstApp.com.solitairedice.R.color.black_1;
import static myFirstApp.com.solitairedice.R.color.gray_99;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Scoring scorePlayerOne;
    Scoring scorePlayerTwo;
    private boolean newHighScore;

    enum player{
        One,
        Two
    }
    enum rollState{
        StartGame,
        Idle,
        Rolled,
        Chosen,
        NextPlayer,
        EndGame
    }
    rollState currentState = rollState.StartGame;

    TextView totalScore, totalScore2, scoreInt1, scoreInt2;
    Button roll;
    Button clearDices;
    SolitaireWindow window;
    boolean TwoPlayers = false;
    player currentPlayer;
    Scoring currentScore;

    // todo: improve this
    String SinglePlayerScoreText = "Total: ";
    String playerOneScoreText = "Player 1: ";
    String playerTwoScoreText = "Player 2: ";

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences file =
                        getApplicationContext().getSharedPreferences("ScoreHistory", MODE_PRIVATE);

        Intent highScore = new Intent(MainActivity.this,HighScore.class);
        highScore.putExtra("SomeData", file.getString("high_score", ""));
        startActivity(highScore);

        return true;
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
        totalScore2 = findViewById(R.id.textTotalScore2);
        scoreInt1 = findViewById(R.id.count_score);
        scoreInt2 = findViewById(R.id.count_score2);

        roll  = findViewById(R.id.rollDices);
        clearDices = findViewById(R.id.clearDices);
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

        // region Window item
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
        window.dices = new int[] {R.drawable.dice_one, R.drawable.dice_two, R.drawable.dice_three,  R.drawable.dice_four,
                R.drawable.dice_five, R.drawable.dice_six};
        window.white = getResources().getDrawable(R.color.white);
        window.backgroundBlue = getResources().getDrawable(R.drawable.rounded_blue);
        window.backgroundPurple = getResources().getDrawable(R.drawable.rounded_purple);
        window.backgroundBlack = getResources().getDrawable(R.drawable.rounded_black);
        // endregion

        scorePlayerOne = new Scoring();
        scorePlayerOne.NewScore();

        clearDices.setVisibility(View.INVISIBLE);
        clearDices.setOnClickListener(this);
        roll.setOnClickListener(this);
        diceOne.setOnClickListener(this);
        diceTwo.setOnClickListener(this);
        diceThree.setOnClickListener(this);
        diceFour.setOnClickListener(this);
        diceFive.setOnClickListener(this);

        Players();
    }
    @SuppressLint("SetTextI18n")
    private void Players(){
        TwoPlayers = "TwoPlayers".equals(MenuHomeActivity.getValue());
        if(TwoPlayers){
            totalScore.setText(playerOneScoreText);
            totalScore2.setText(playerTwoScoreText);
            totalScore2.setVisibility(TextView.VISIBLE);
            currentPlayer = player.One;
            scorePlayerTwo = new Scoring();
            scorePlayerTwo.NewScore();
            TextView textCurPlayer = findViewById(R.id.textCurrentPlayer);
            textCurPlayer.setText("Player One");
        }
        else {
            totalScore2.setEnabled(false);
            totalScore2.setVisibility(TextView.INVISIBLE);
            totalScore.setText(SinglePlayerScoreText);
        }
    }
    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rollDices:
                RollDicesMain(window);
                break;
            case R.id.clearDices:
                window.ClearAllDices();
                currentState = rollState.Rolled;
                break;
            case R.id.DiceOne:
            case R.id.DiceTwo:
            case R.id.DiceThree:
            case R.id.DiceFour:
            case R.id.DiceFive:
                if ( currentState == MainActivity.rollState.Rolled ||
                        currentState == MainActivity.rollState.Chosen ) {
                    window.SelectDice(findViewById(v.getId()));
                    SetEnableRoll();
                    clearDices.setEnabled(true);
                }
                break;
        }

    }
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void StateMachineOnePlayer(SolitaireWindow sWindow){
        switch (currentState){
            case Idle:
                sWindow.CleanChoices();
                RollDiceStatus(false);
                sWindow.RollDicesNow(scorePlayerOne);
                roll.setText("Play & Roll");
                clearDices.setVisibility(View.VISIBLE);
                clearDices.setEnabled(false);
                currentState = rollState.Rolled;
                break;
            case Chosen:
                int current_score = sWindow.GetTotalScore(scorePlayerOne);
                if(sWindow.FillNumbers(this, scorePlayerOne)){

                    UpdateTotalScore(current_score, sWindow, scoreInt1);

                    sWindow.CleanChoices();
                    RollDiceStatus(false);
                    if (scorePlayerOne.state != Scoring.ScoreState.Finish){
                        sWindow.RollDicesNow(scorePlayerOne);
                        currentState = rollState.Rolled;
                        if (sWindow.freeThrow)
                        {
                            freeThrowAwayPopup();
                        }
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
                scorePlayerOne.NewScore();
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
    private void UpdateTotalScore(int current_score, SolitaireWindow sWindow, TextView countScore){
        ValueAnimator animator = ValueAnimator.ofInt(current_score, sWindow.GetTotalScore(scorePlayerOne) );
        animator.setDuration(500);
        animator.addUpdateListener(animation -> countScore.setText(animation.getAnimatedValue().toString()));
        animator.start();
    }
    private void freeThrowAwayPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.free_throw_menu);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(null);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnim);

        dialog.show();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})

    public void SwapPlayer(Scoring scorePlayer, SolitaireWindow sWindow){
        sWindow.CleanThrowAway();
        sWindow.CleanDices();
        sWindow.CleanChoices();
        sWindow.CleanScoring();

        sWindow.InsertPlayerScore(scorePlayer);
        sWindow.InsertThrowAway(scorePlayer);
    }
    public Scoring NextPlayer(){
        Scoring nextPlayer;
        String playerName;
        if (currentPlayer == player.One){
            nextPlayer = scorePlayerTwo;
            currentPlayer = player.Two;
            playerName = "Player Two";
        }else{
            nextPlayer = scorePlayerOne;
            currentPlayer = player.One;
            playerName = "Player One";
        }
        TextView textCurPlayer = findViewById(R.id.textCurrentPlayer);
        textCurPlayer.setText(playerName);

        return nextPlayer;
    }
    public Scoring GetNextPlayer(){
        Scoring nextPlayer = scorePlayerOne;
        if (currentPlayer == player.One){
            nextPlayer = scorePlayerTwo;
        }
        return nextPlayer;
    }
    public Scoring CurrentPlayer(){
        Scoring curPlayer;
        if (currentPlayer == player.One){
            curPlayer = scorePlayerOne;
        }else{
            curPlayer = scorePlayerTwo;
            currentPlayer = player.Two;
        }
        return curPlayer;
    }

    @SuppressLint("SetTextI18n")
    public void StateMachineTwoPlayers(SolitaireWindow sWindow){

         switch (currentState){
            case Idle:
                sWindow.CleanChoices();
                RollDiceStatus(false);
                sWindow.RollDicesNow(CurrentPlayer());
                roll.setText("Play & Roll");
                clearDices.setVisibility(View.VISIBLE);
                currentState = rollState.Rolled;
                break;
            case Chosen:
                currentScore = CurrentPlayer();

                // todo: improve this
                TextView scoreText = scoreInt2;
                if (currentPlayer == player.One){
                    scoreText = scoreInt1;
                }

                int current_score = sWindow.GetTotalScore(currentScore);

                if(sWindow.FillNumbers(this, currentScore)){
                    UpdateTotalScore(current_score, sWindow, scoreText);

                    if ((GetNextPlayer()).state == Scoring.ScoreState.Finish){
                        if (currentScore.state != Scoring.ScoreState.Finish){
                            currentState = rollState.Idle;
                            roll.setText("Roll");
                            RollDiceStatus(true);
                        } else{
                            EndingGameTwoPlayers(sWindow);
                        }
                    }else{
                        roll.setText("Next Player");
                        currentState = rollState.NextPlayer;
                        RollDiceStatus(true);
                    }
                }else
                {
                    ShowMessage("Invalid throw away," +
                            "\nplease select a valid one");
                }
                break;
             case NextPlayer:
                 currentScore = CurrentPlayer();
                 roll.setText("Roll");
                 currentState = rollState.Idle;
                 // Todo: Swap players score
                 SwapPlayer(NextPlayer(), sWindow);
            case Rolled:
                // do nothing and wait for all dices to be chosen
                break;
            case EndGame:
                sWindow.EndGame();
                scorePlayerOne.NewScore();
                scorePlayerTwo.NewScore();
                totalScore.setText(playerOneScoreText);
                totalScore2.setText(playerTwoScoreText);
                roll.setText("Roll");
                currentState = rollState.StartGame;
                break;
            case StartGame:
                sWindow.CleanScoring();
                sWindow.CleanThrowAway();
                roll.setText("Roll");
                currentState = rollState.Idle;
                break;
            default:
                break;
        }
    }
    public void RollDicesMain(SolitaireWindow sWindow){
        if(TwoPlayers){
            StateMachineTwoPlayers(sWindow);
        }else {
            StateMachineOnePlayer(sWindow);
        }
    }
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void EndingGame(SolitaireWindow sWindow){
        sWindow.EndingGame();
        RollDiceStatus(false);
        roll.setText("Start");
        RollDiceStatus(true);
        SaveHighestScore();
        ShowEndOfGame();

        clearDices.setVisibility(View.INVISIBLE);
        currentState = rollState.EndGame;
    }
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void EndingGameTwoPlayers(SolitaireWindow sWindow){
        sWindow.EndingGame();
        RollDiceStatus(false);
        ShowMessage("End of Game" +
                "\nPlayer One: " + scorePlayerOne.TotalScore() +
                "\nPlayer Two: " + scorePlayerTwo.TotalScore());
        roll.setText("Start");
        RollDiceStatus(true);

        clearDices.setVisibility(View.INVISIBLE);
        currentState = rollState.EndGame;
    }

    public void SetEnableRoll(){
        clearDices.setEnabled(true);
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

        // todo: change the colors to make it more clear when its enable and when its not
        if(state){
            rollButton.setBackgroundColor(black_1);
        } else {
            rollButton.setBackgroundColor(gray_99);
        }
    }
    private void ShowEndOfGame(){
        String scoreText = "";
        if(newHighScore){
            scoreText = "\nNew best score!\n";
            newHighScore = false;
        }
        scoreText += "\nScore: " + scorePlayerOne.TotalScore();
        ShowMessage("End of Game" + scoreText);
    }
    public void ShowMessage(String message){
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        Objects.requireNonNull(alert.getWindow()).setWindowAnimations(R.style.DialogAnim);
        alert.show();
    }
    public Scoring DefineWinner(){
        if(TwoPlayers){
            if(scorePlayerOne.IntTotalScore() < scorePlayerTwo.IntTotalScore()){
                return scorePlayerTwo;
            }
        }
        return scorePlayerOne;
    }
    public void SaveHighestScore(){
        SharedPreferences highScore =
                getApplicationContext().getSharedPreferences("ScoreHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = highScore.edit();

        int last_high_score = highScore.getInt("High", 0);
        Scoring winner = DefineWinner();

        if(winner.IntTotalScore() > last_high_score ) {
            newHighScore = true;
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Highest Score",  String.valueOf(winner.IntTotalScore()));

                // Save all dices count
                for( int i = 2; i<= 12; i ++){
                    jsonObject.put(String.valueOf(i), String.valueOf(winner.GetCount(i)) );
                }
                // Save all throw away count
                for( int j = 1; j<= 6; j ++) {
                    if (winner.IsThrowAway(j)) {
                        String ValueNum = String.valueOf(j);
                        jsonObject.put("Throw Away " + ValueNum, String.valueOf(winner.GetThrowAwayNum(j)));
                    }
                }
                editor.putString("high_score", jsonObject.toString()).apply();
            }catch (JSONException json){
                // Something got wrong
            }

            editor.apply();
        }
    }
}