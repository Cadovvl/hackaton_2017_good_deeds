package cadovvl.cadovvl.cadovvl.gd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.apache.commons.io.IOUtils.copy;

public class CompanyPoster extends AppCompatActivity {

    private StorageClient client = new StorageClientImpl();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_poster);
        try {
            final String lat = getIntent().getExtras().getString("lat");
            final String lon = getIntent().getExtras().getString("lon");
            ((EditText) findViewById(R.id.latField)).setText(lat);
            ((EditText) findViewById(R.id.lonField)).setText(lon);

            try {
                client.loadBitmap("http://www.freeiconspng.com/uploads/camera-icon-png--clipart-best-23.png",
                        new BitmapConsumer() {
                            @Override
                            public void consume(final Bitmap img) {
                                CompanyPoster.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ImageView iw = (ImageView) findViewById(R.id.posterView);
                                        iw.setImageBitmap(Bitmap.createScaledBitmap(img, iw.getWidth(), iw.getHeight(), false));
                                    }
                                });
                            }
                        });
            } catch (Exception e) {
                Log.e("Lol", e.getMessage());
            }

            final CompanyPoster poster = this;

            View button = findViewById(R.id.newCompanySender);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String latF = ((EditText) findViewById(R.id.latField)).getText().toString();
                        String lonF = ((EditText) findViewById(R.id.lonField)).getText().toString();

                        client.post(new Deed()
                                        .setName(((EditText) findViewById(R.id.nameField)).getText().toString())
                                        .setDescrition(((EditText) findViewById(R.id.descriptionField)).getText().toString())
                                        .setPos(new Pos()
                                                .setLat(latF.isEmpty() ? 0.0 : Double.parseDouble(latF))
                                                .setLon(lonF.isEmpty() ? 0.0 : Double.parseDouble(lonF))
                                        ),
                                new ResultConsumer() {
                                    @Override
                                    public void consume(String res) {
                                        poster.finish();
                                    }
                                });
                    } catch (Exception e) {
                        // todo: remove ignore
                        // Log.e("Sending", e.getMessage());
                    }
                }
            });


            View cameraView = findViewById(R.id.posterView);
            cameraView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(intent);
                }
            });


        } catch (Exception e) {
            final String message = String.format("Something really bad happens: %s", e.getMessage());
            Log.e("Lal", message);
            this.finish();
        }

    }
}
