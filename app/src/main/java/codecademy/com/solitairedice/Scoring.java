package codecademy.com.solitairedice;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class Scoring {
    private class Score {
        public int Num;
        public int Sum;
        public int Multiplier;
        public int Count;

        public Score(int num, int multiplier){
            Num = num;
            Multiplier = multiplier;
            Sum = 0;
            Count = 0;
        }
        public int PlusOne(){
            Count++;

            if(Count <= 4 ){
                Sum = -200;
            }
            else if ( Count == 5 ){
                Sum = 0;
            }
            else{
                Sum = (Count - 5 ) * Multiplier;
            }
            return Count;
        }
    }

    public enum ScoreState{
        Starting,
        ChoosingThrowAway,
        ThreeThrowAway,
        Finish
    };

    Dictionary<Integer, Score> dicScore  = new Hashtable<Integer, Score>();
    ScoreState state;

    public void NewScore(){
        state = ScoreState.Starting;

        dicScore.put( 2, new Score(2, 100));
        dicScore.put( 3, new Score(3, 70));
        dicScore.put( 4, new Score(4, 60));
        dicScore.put( 5, new Score(5, 50));
        dicScore.put( 6, new Score(6, 40));
        dicScore.put( 7, new Score(7, 30));
        dicScore.put( 8, new Score(8, 40));
        dicScore.put( 9, new Score(9, 50));
        dicScore.put( 10, new Score(10, 60));
        dicScore.put( 11, new Score(11, 70));
        dicScore.put( 12, new Score(12, 100));
    }
    public String TotalScore(){
        int total = 0;

        Enumeration keys = dicScore.keys();

        while(keys.hasMoreElements()){
            total += dicScore.get(keys.nextElement()).Sum;
        }

        return String.valueOf(total);
    }
    public void AddNewNumber(int num){
        if( 2 <= num && num <= 12 ) {
            Score auxScore = dicScore.get(num);
            auxScore.PlusOne();
        }
    }
    public int GetCount(int num){
        return (dicScore.get(num)).Count;
    }

    private void DefineState(){
        switch (state){
            case Starting:
                // todo
                break;
            case ChoosingThrowAway:
                state = state;
                break;
            case ThreeThrowAway:
                break;
            case Finish:
            default:
                break;
        }
    }
}
