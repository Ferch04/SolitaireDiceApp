package codecademy.com.solitairedice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MenuHomeActivity extends AppCompatActivity {
    private Button bOnePlayer;
    private Button bTwoPlayers;

    private static String value;
    public static String getValue() {
        return value;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);

        bOnePlayer = (Button)findViewById(R.id.onePlayer);
        bOnePlayer.setOnClickListener(view -> OnePlayer());

        bTwoPlayers = (Button)findViewById(R.id.TwoPlayers);
        bTwoPlayers.setOnClickListener(view -> TwoPlayers());
    }
    private void OnePlayer() {
        value = "OnePlayer";
        openGame();
    }
    private void TwoPlayers() {
        value = "TwoPlayers";
        openGame();
    }
    public void openGame()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}