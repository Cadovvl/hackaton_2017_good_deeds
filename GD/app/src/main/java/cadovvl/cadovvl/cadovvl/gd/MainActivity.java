package cadovvl.cadovvl.cadovvl.gd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View buttonView = findViewById(R.id.button);

        buttonView.setOnClickListener(new View.OnClickListener(){
            int i = 0;
            @Override
            public void onClick(View view) {
                i += 1;
                TextView textView = (TextView)findViewById(R.id.OutputText);
                textView.setText("Pressed: " + i);
            }
        });

    }
}
