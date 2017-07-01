package cadovvl.cadovvl.cadovvl.gd;

import java.util.List;

public interface StorageClient {

    public void post(final Deed deed, ResultConsumer callback);
    public void put(final Deed deed, ResultConsumer callback);

    public void post(final Deed deed, DeedConsumer callback);
    public void put(final Deed deed, DeedConsumer callback);

    public void get(final String id, DeedConsumer consumer);
    public void find(final SearchParams params, DeedsConsumer consumer);
}
