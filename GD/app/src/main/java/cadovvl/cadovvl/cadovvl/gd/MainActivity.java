package cadovvl.cadovvl.cadovvl.gd;

import android.content.Intent;
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
        View cameraView = findViewById(R.id.shot);

        buttonView.setOnClickListener(new View.OnClickListener(){
            int i = 0;
            @Override
            public void onClick(View view) {
                i += 1;
                TextView textView = (TextView)findViewById(R.id.OutputText);
                textView.setText("Pressed: " + i);
            }
        });
        cameraView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(view.getContext(), CameraActivity.class);
                startActivity(cameraIntent);
            }
        }));

    }
}
