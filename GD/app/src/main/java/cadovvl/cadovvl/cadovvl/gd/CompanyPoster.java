package cadovvl.cadovvl.cadovvl.gd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class CompanyPoster extends AppCompatActivity {

    private StorageClient client = new StorageClientImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_poster);

        View button = findViewById(R.id.newCompanySender);

        final CompanyPoster poster = this;

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
                                            .setLat(latF.isEmpty() ? 0.0: Double.parseDouble(latF))
                                            .setLon(lonF.isEmpty() ? 0.0: Double.parseDouble(lonF))
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
    }
}
