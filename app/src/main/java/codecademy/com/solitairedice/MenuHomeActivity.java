package codecademy.com.solitairedice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuHomeActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);

        button = (Button)findViewById(R.id.startGame);
        button.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        openGame();
                  }
              });
    }
    public void openGame()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}