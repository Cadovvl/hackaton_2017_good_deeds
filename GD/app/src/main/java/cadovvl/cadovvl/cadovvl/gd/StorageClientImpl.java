package cadovvl.cadovvl.cadovvl.gd;

import android.os.Handler;
import android.os.HandlerThread;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class StorageClientImpl implements StorageClient {

    private final static String host = "172.17.0.1";
    private final static String port = "8888";

    private ObjectMapper mapper = new ObjectMapper();

    private Handler handler;

    public StorageClientImpl() {
        HandlerThread thread = new HandlerThread("poster");
        thread.start();
        this.handler = new Handler(thread.getLooper());
    }


    @Override
    public void post(final Deed deed, final ResultConsumer callback) {
        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             final String url = "http://" + host + ":" + port + "/";
                             HttpClient client = HttpClientBuilder.create().build();
                             HttpPost post = new HttpPost(url);

                             try {
                                 post.setEntity(new ByteArrayEntity(
                                         mapper.writeValueAsBytes(deed)
                                 ));

                                 HttpResponse response = client.execute(post);

                             if (response.getStatusLine().getStatusCode() != 200) {
                                     throw new RuntimeException("Das is ERROR!!!");
                                 }

                                 callback.consume(
                                         IOUtils.toString(response.getEntity().getContent())
                                 );
                             } catch (Exception e) {
                                 throw new RuntimeException(e);
                             }
                         }
                     }
        );
    }

    @Override
    public void put(Deed deed, ResultConsumer callback) {

    }

    @Override
    public void post(Deed deed, DeedConsumer callback) {

    }

    @Override
    public void put(Deed deed, DeedConsumer callback) {

    }

    @Override
    public void get(long id, DeedConsumer consumer) {

    }

    @Override
    public void find(DeedsConsumer consumer) {

    }
}
