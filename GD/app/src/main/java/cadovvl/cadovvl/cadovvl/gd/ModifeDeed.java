package cadovvl.cadovvl.cadovvl.gd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Arrays;

public class ModifeDeed extends AppCompatActivity {

    StorageClient storage = new StorageClientImpl();

    private void updateImage(final String photoUrl) {
        if(photoUrl == null)
            return;
        storage.loadBitmap(photoUrl,
                new BitmapConsumer() {
                    @Override
                    public void consume(final Bitmap img) {
                        ModifeDeed.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView iw = (ImageView) findViewById(R.id.modifyView);
                                iw.setImageBitmap(Bitmap.createScaledBitmap(img, iw.getWidth(), iw.getHeight(), false));
                            }
                        });
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modife_deed);
        try {
            final Spinner spinner = (Spinner) findViewById(R.id.statusSpinner);
            final EditText name = (EditText) findViewById(R.id.nameField);
            final EditText description = (EditText) findViewById(R.id.descriptionField);


            final String id = getIntent().getStringExtra("id");

            final Deed d = new Deed();

            final ModifeDeed modifier = this;

            storage.get(id, new DeedConsumer() {
                @Override
                public void consume(final Deed deed) {
                    d
                            .setId(id)
                            .setPos(deed.getPos())
                            .setDescrition(deed.getDescrition())
                            .setName(deed.getName())
                            .setPhoto(deed.getPhoto())
                            .setStatus(deed.getStatus());

                    modifier.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    spinner.setSelection(deed.getStatus().ordinal());
                                    name.setText(deed.getName());
                                    description.setText(deed.getDescrition());
                                    updateImage(deed.getPhoto());
                                }
                            }
                    );
                }
            });


            if (d.getPhoto() != null) {
                updateImage(d.getPhoto());
            }

            View button = findViewById(R.id.modifyCompanyButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        storage.put(d
                                        .setName(name.getText().toString())
                                        .setDescrition(description.getText().toString())
                                        .setStatus(Deed.Status.valueOf(spinner.getSelectedItem().toString())),
                                new ResultConsumer() {
                                    @Override
                                    public void consume(String res) {
                                        Log.i("Put resp", res);
                                        modifier.finish();
                                    }
                                });
                    } catch (Exception e) {
                        // todo: remove ignore
                        // Log.e("Sending", e.getMessage());
                    }
                }
            });


            View cameraView = findViewById(R.id.modifyView);
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
