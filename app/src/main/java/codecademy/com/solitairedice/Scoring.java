package codecademy.com.solitairedice;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Objects;

public class Scoring {
    private static class Score {
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
        public void PlusOne(){
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
        }
    }
    private static class Throw{
        public int count = 0;
        public String text;
    }

    public enum ScoreState{
        Starting,
        ChoosingThrowAway,
        ThreeThrowAway,
        Finish
    }

    Hashtable<Integer, Score> hasScore = new Hashtable<>();
    Hashtable<Integer, Throw> hashThrowAway = new Hashtable<>();
    ScoreState state;

    public void NewScore(){
        ClearScore();
        state = ScoreState.Starting;

        hasScore.put( 2, new Score(2, 100));
        hasScore.put( 3, new Score(3, 70));
        hasScore.put( 4, new Score(4, 60));
        hasScore.put( 5, new Score(5, 50));
        hasScore.put( 6, new Score(6, 40));
        hasScore.put( 7, new Score(7, 30));
        hasScore.put( 8, new Score(8, 40));
        hasScore.put( 9, new Score(9, 50));
        hasScore.put( 10, new Score(10, 60));
        hasScore.put( 11, new Score(11, 70));
        hasScore.put( 12, new Score(12, 100));
    }
    public String TotalScore(){
        return String.valueOf(IntTotalScore());
    }
    public int IntTotalScore(){
        int total = 0;

        Enumeration<Integer> keys = hasScore.keys();

        while(keys.hasMoreElements()){
            total += Objects.requireNonNull(hasScore.get(keys.nextElement())).Sum;
        }

        return total;
    }
    private void ClearScore(){
        hashThrowAway.clear();
        hasScore.clear();
    }

    public String GetThrowAway(int num){
        Throw aux_throw = hashThrowAway.get(num);
        assert aux_throw != null;
        return aux_throw.text;
    }
    public int GetCount(int num){
        return (Objects.requireNonNull(hasScore.get(num))).Count;
    }

    public boolean IsThrowAway(int num){
        return hashThrowAway.containsKey(num);
    }

    public boolean AddThrowAway(int num){
        boolean added = true;

        if(hashThrowAway.size() < 3){
            if(hashThrowAway.containsKey(num)) {
                Throw aux_throw = hashThrowAway.get(num);
                Objects.requireNonNull(aux_throw).count++;
                aux_throw.text = aux_throw.text.concat("I");
                hashThrowAway.put(num, aux_throw);
                    if(aux_throw.count == 8 ){
                        state = ScoreState.Finish;
                    }

            } else {
                Throw new_throw = new Throw();
                new_throw.count ++;
                new_throw.text = "(" + num + "): I";

                hashThrowAway.put(num, new_throw);
            }
        }
        else if (hashThrowAway.size() == 3){
            if(hashThrowAway.containsKey(num)) {
                Throw aux_throw = hashThrowAway.get(num);
                Objects.requireNonNull(aux_throw).count ++;
                if(aux_throw.count == 5){
                    StringBuilder newScore =
                            new StringBuilder( Objects.requireNonNull(aux_throw.text));
                    newScore.insert( 5, "<del>");
                    newScore.append("</del> ");
                    aux_throw.text = newScore.toString();
                }
                else{
                    aux_throw.text = aux_throw.text.concat("I");
                }
                hashThrowAway.put(num, aux_throw);
            } else {
                added = false;
            }
        }
        DefineState();

        return added;
    }
    public void AddNewNumber(int num){
        if( 2 <= num && num <= 12 ) {
            Score auxScore = hasScore.get(num);
            Objects.requireNonNull(auxScore).PlusOne();
        }
    }

    private void DefineState(){
        switch (state){
            case Starting:
                if(hashThrowAway.size() > 0){
                    state = ScoreState.ChoosingThrowAway;
                }
                break;
            case ChoosingThrowAway:
                if(hashThrowAway.size() == 3){
                    state = ScoreState.ThreeThrowAway;
                }
                break;
            case ThreeThrowAway:
                for(Throw throw_a : hashThrowAway.values()){
                    if(8 == throw_a.count){
                        state = ScoreState.Finish;
                        break;
                    }
                }
                break;
            case Finish:
            default:
                break;
        }
    }
}
