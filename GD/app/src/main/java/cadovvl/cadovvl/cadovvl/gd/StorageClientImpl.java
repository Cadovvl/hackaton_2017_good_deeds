package cadovvl.cadovvl.cadovvl.gd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

import static org.apache.commons.io.IOUtils.copy;

public class StorageClientImpl implements StorageClient {

    private final static String host = "194.87.94.65";
    private final static String port = "4000";
    private final static String uri = "/deed";
    private final static String url = "http://" + host + ":" + port + uri;
    private static final int IO_BUFFER_SIZE = 4000;

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
                             HttpClient client = HttpClientBuilder.create().build();
                             HttpPost post = new HttpPost(url);
                             post.addHeader("Content-Type", "application/json");

                             try {
                                 post.setEntity(new ByteArrayEntity(
                                         mapper.writeValueAsBytes(deed)
                                 ));

                                 HttpResponse response = client.execute(post);

                                 if (response.getStatusLine().getStatusCode() != 200) {
                                     throw new RuntimeException("Das is ERROR!!!\n Response code: " + response.getStatusLine());
                                 }

                                 callback.consume(
                                         IOUtils.toString(response.getEntity().getContent())
                                 );
                             } catch (Exception e) {
                                 final String message = String.format("Something really bad happens: %s", e.getMessage());
                                 Log.e("Lal", message);
                             }
                         }
                     }
        );
    }

    @Override
    public void put(final Deed deed, final ResultConsumer callback) {
        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             HttpClient client = HttpClientBuilder.create().build();
                             HttpPut put = new HttpPut(url + "/" + deed.getId());
                             put.addHeader("Content-Type", "application/json");

                             try {
                                 put.setEntity(new ByteArrayEntity(
                                         mapper.writeValueAsBytes(deed)
                                 ));

                                 HttpResponse response = client.execute(put);

                                 if (response.getStatusLine().getStatusCode() != 200) {
                                     throw new RuntimeException("Das is ERROR!!!\n Response code: " + response.getStatusLine());
                                 }

                                 callback.consume(
                                         IOUtils.toString(response.getEntity().getContent())
                                 );
                             } catch (Exception e) {
                                 final String message = String.format("Something really bad happens: %s", e.getMessage());
                                 Log.e("Lal", message);
                             }
                         }
                     }
        );
    }

    @Override
    public void post(final Deed deed, final DeedConsumer callback) {
        post(deed, new ResultConsumer() {
            @Override
            public void consume(String res) {
                try {
                    callback.consume(mapper.readValue(res, Deed.class));
                } catch (Exception e) {
                    final String message = String.format("Something really bad happens: %s", e.getMessage());
                    Log.e("Lal", message);
                }
            }
        });
    }

    @Override
    public void put(final Deed deed, final DeedConsumer callback) {
        put(deed, new ResultConsumer() {
            @Override
            public void consume(String res) {
                try {
                    callback.consume(mapper.readValue(res, Deed.class));
                } catch (Exception e) {
                    final String message = String.format("Something really bad happens: %s", e.getMessage());
                    Log.e("Lal", message);
                }
            }
        });
    }

    @Override
    public void get(final String id, final DeedConsumer consumer) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HttpClient client = HttpClientBuilder.create().build();
                HttpGet get = new HttpGet(url + "/" + id);
                try {
                    HttpResponse response = client.execute(get);

                    if (response.getStatusLine().getStatusCode() != 200) {
                        throw new RuntimeException("Das is ERROR!!!\n Response code: " + response.getStatusLine());
                    }

                    consumer.consume(
                            mapper.readValue(response.getEntity().getContent(), Deed.class).setId(id)
                    );
                } catch (Exception e) {
                    final String message = String.format("Something really bad happens: %s", e.getMessage());
                    Log.e("Lal", message);
                }
            }
        });
    }

    @Override
    public void find(final SearchParams params, final DeedsConsumer consumer) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HttpClient client = HttpClientBuilder.create().build();
                URIBuilder builder = new URIBuilder()
                        .setScheme("http")
                        .setHost(host)
                        .setPort(Integer.parseInt(port))
                        .setPath(uri)
                        .setParameter(SearchParams.LAT_PARAM_NAME, params.getLat().toString())
                        .setParameter(SearchParams.LON_PARAM_NAME, params.getLon().toString())
                        .setParameter(SearchParams.R_PARAM_NAME, params.getR().toString());

                List<String> statuses = new ArrayList<String>();
                for (final Deed.Status s: params.getStatuses()) {
                    statuses.add(s.name());
                }

                if (!statuses.isEmpty()) {
                    builder.setParameter(SearchParams.STATUSES_PARAM_NAME, StringUtils.join(statuses));
                }

                try {

                    HttpGet get = new HttpGet(builder.build());
                    HttpResponse response = client.execute(get);

                    if (response.getStatusLine().getStatusCode() != 200) {
                        throw new RuntimeException("Das is ERROR!!!\n Response code: " + response.getStatusLine());
                    }

                    Deeds res = mapper.readValue(response.getEntity().getContent(), Deeds.class);
                    for (Map.Entry<String, Deed> e: res.entrySet()) {
                        e.setValue(e.getValue().setId(e.getKey()));
                    }
                    consumer.consume(
                            res
                    );
                } catch (Exception e) {
                    final String message = String.format("Something really bad happens: %s", e.getMessage());
                    Log.e("Lal", message);
                }
            }
        });
    }

    @Override
    public void loadBitmap(final String url, final BitmapConsumer consumer) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                InputStream in = null;
                BufferedOutputStream out = null;

                try {
                    in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

                    final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                    out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                    copy(in, out);
                    out.flush();

                    final byte[] data = dataStream.toByteArray();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inSampleSize = 1;

                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                } catch (IOException e) {
                    Log.e("TAG", "Could not load Bitmap from: " + url);
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }

                consumer.consume(bitmap);
            }
        });
    }
}
