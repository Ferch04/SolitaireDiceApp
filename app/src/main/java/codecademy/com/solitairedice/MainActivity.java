package codecademy.com.solitairedice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import static codecademy.com.solitairedice.R.color.black_1;
import static codecademy.com.solitairedice.R.color.gray_99;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Scoring scorePlayerOne;
    Scoring scorePlayerTwo;

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

    TextView totalScore, totalScore2 ;
    Button roll;
    SolitaireWindow window;
    boolean TwoPlayers = false;
    player currentPlayer;

    // todo: improve this
    String SinglePlayerScoreText = "Total: ";
    String playerOneScoreText = "Player 1: ";
    String playerTwoScoreText = "Player 2: ";
    String ScoreText;

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
        totalScore2 = findViewById(R.id.textTotalScore2);

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
        window.dices = new int[] {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,  R.drawable.dice4,
                R.drawable.dice5, R.drawable.dice6 };
        window.purple = getResources().getDrawable(R.color.purple_700);
        window.bblack = getResources().getDrawable(R.color.black);
        window.teal = getResources().getDrawable(R.color.teal_700);
        window.white = getResources().getDrawable(R.color.white);
        // endregion

        scorePlayerOne = new Scoring();
        scorePlayerOne.NewScore();

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
            ScoreText = playerOneScoreText;
            TextView textCurPlayer = findViewById(R.id.textCurrentPlayer);
            textCurPlayer.setText("Player One");
        }
        else {
            totalScore2.setEnabled(false);
            totalScore2.setVisibility(TextView.INVISIBLE);
            totalScore.setText(ScoreText);
            ScoreText = SinglePlayerScoreText;
        }
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
                    window.SelectDice(findViewById(v.getId()));
                    SetEnableRoll();
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
                currentState = rollState.Rolled;
                break;
            case Chosen:
                if(sWindow.FillNumbers(scorePlayerOne, totalScore, ScoreText)){
                    sWindow.CleanChoices();
                    RollDiceStatus(false);
                    // Todo: Swap players score
                    if (scorePlayerOne.state != Scoring.ScoreState.Finish){
                        sWindow.RollDicesNow(scorePlayerOne);
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
                scorePlayerOne.NewScore();
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

    public void SwapPlayer(Scoring scorePlayer, SolitaireWindow sWindow){
        sWindow.CleanThrowAway();
        sWindow.CleanDices();
        sWindow.CleanChoices();
        sWindow.CleanScoring(getResources().getColor(R.color.gray_99));

        sWindow.InsertPlayerScore(scorePlayer);
        sWindow.InsertThrowAway(scorePlayer);
    }
    public Scoring NextPlayer(){
        Scoring nextPlayer;
        String playerName;
        if (currentPlayer == player.One){
            nextPlayer = scorePlayerTwo;
            currentPlayer = player.Two;
            ScoreText = playerTwoScoreText;
            playerName = "Player Two";
        }else{
            nextPlayer = scorePlayerOne;
            currentPlayer = player.One;
            ScoreText = playerOneScoreText;
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

    public void StateMachineTwoPlayers(SolitaireWindow sWindow){

         switch (currentState){
            case Idle:
                sWindow.CleanChoices();
                RollDiceStatus(false);
                sWindow.RollDicesNow(CurrentPlayer());
                roll.setText("Play & Roll");
                currentState = rollState.Rolled;
                break;
            case Chosen:
                Scoring currentScore = CurrentPlayer();

                // todo: improve this
                TextView scoreText = totalScore2;
                if (currentPlayer == player.One){
                    scoreText = totalScore;
                }

                if(sWindow.FillNumbers(currentScore, scoreText, ScoreText)){
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
                sWindow.CleanScoring(getResources().getColor(R.color.gray_99));
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
    @SuppressLint("UseCompatLoadingForDrawables")
    public void EndingGame(SolitaireWindow sWindow){
        sWindow.EndingGame();
        RollDiceStatus(false);
        ShowMessage("End of Game" +
                "\nScore: " + scorePlayerOne.TotalScore());
        roll.setText("Start");
        RollDiceStatus(true);
        SaveHighestScore();

        currentState = rollState.EndGame;
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void EndingGameTwoPlayers(SolitaireWindow sWindow){
        sWindow.EndingGame();
        RollDiceStatus(false);
        ShowMessage("End of Game" +
                "\nPlayer One: " + scorePlayerOne.TotalScore() +
                "\nPlayer Two: " + scorePlayerTwo.TotalScore());
        roll.setText("Start");
        RollDiceStatus(true);

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

        // todo: change the colors to make it more clear when its enable and when its not
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
        if(scorePlayerOne.IntTotalScore() > last_high_score ) {
            editor.putInt("High", scorePlayerOne.IntTotalScore());
            editor.apply();
        }
    }
}