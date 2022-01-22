package codecademy.com.solitairedice;

import android.util.Log;

import java.util.Dictionary;
import java.util.Hashtable;

public class Scoring {
    private class Score {
        public int Num;
        public int Sum;
        public int Multiplier;
        public int count;

        public Score(int num, int multiplier){
            Num = num;
            Multiplier = multiplier;
            Sum = 0;
            count = 0;
        }
        public int PlusOne(){
            count++;
            return count;
        }
    }

    Dictionary<Integer, Score> dicScore
            = new Hashtable<Integer, Score>();

    public void NewScore(){
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
    public void AddNewNumber(int num){
        if( 2 <= num && num <= 12 ) {
            Score auxScore = dicScore.get(num);
            auxScore.PlusOne();
        }
    }
    public int GetCount(int num){
        return (dicScore.get(num)).count;
    }
}
