package cadovvl.cadovvl.cadovvl.gd;

import java.util.ArrayList;
import java.util.List;

public class SearchParams {

    public final static String LAT_PARAM_NAME = "lat";
    public final static String LON_PARAM_NAME = "lon";
    public final static String R_PARAM_NAME = "r";
    public final static String STATUSES_PARAM_NAME = "statuses";

    private Double lat;
    private Double lon;
    private Double r;

    private List<Deed.Status> statuses;

    public SearchParams() {}

    public SearchParams setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public SearchParams setLon(Double lon) {
        this.lon = lon;
        return this;
    }

    public SearchParams setR(Double r) {
        this.r = r;
        return this;
    }

    public SearchParams setStatuses(List<Deed.Status> statuses) {
        this.statuses = statuses;
        return this;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Double getR() {
        return r != null ? r : 500;
    }

    public List<Deed.Status> getStatuses() {
        return statuses == null ? new ArrayList<Deed.Status>() : statuses;
    }
}
