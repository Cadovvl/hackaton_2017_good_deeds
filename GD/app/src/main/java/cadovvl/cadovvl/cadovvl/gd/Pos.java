package cadovvl.cadovvl.cadovvl.gd;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Pos {


    private final static String LAT_KEY = "lat";
    private final static String LON_KEY = "lon";

    @JsonProperty(LAT_KEY)
    private double lat;
    @JsonProperty(LON_KEY)
    private double lon;


    public Pos(@JsonProperty(LAT_KEY) double lat,
               @JsonProperty(LON_KEY) double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Pos() { }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Pos setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public Pos setLon(double lon) {
        this.lon = lon;
        return this;
    }
}
