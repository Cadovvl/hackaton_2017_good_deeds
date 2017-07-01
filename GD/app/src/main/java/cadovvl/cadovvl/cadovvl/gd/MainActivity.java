package cadovvl.cadovvl.cadovvl.gd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {

    private final static StorageClient storageClient = new StorageClientImpl();
    private final static ObjectMapper mapper = new ObjectMapper();


    private void processException(Exception e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        printWriter.flush();
        TextView textView = (TextView) findViewById(R.id.OutputText);
        textView.setText("An error occured: " + e.getMessage()
                + "\n\nDetails: " + writer.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View posterView = findViewById(R.id.poster);
        View cameraView = findViewById(R.id.shot);


        posterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final TextView textView = (TextView) findViewById(R.id.OutputText);

                    Deed obj = new Deed()
                            .setName("test name")
                            .setPos(new Pos()
                                    .setLon(0.3)
                                    .setLat(13.54));

                    ResultConsumer consumer = new ResultConsumer() {
                        @Override
                        public void consume(final String res) {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            textView.setText(res);
                                        }
                                    }
                            );
                        }
                    };

                    storageClient.post(obj, consumer);
                } catch (Exception e) {
                    processException(e);
                }
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
