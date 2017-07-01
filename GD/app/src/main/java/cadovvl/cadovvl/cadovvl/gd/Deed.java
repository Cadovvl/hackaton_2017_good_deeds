package cadovvl.cadovvl.cadovvl.gd;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Deed {

    public enum Status {
        created,
        done,
        closed
    }

    private final static String ID_KEY = "id";
    private final static String NAME_KEY = "name";
    private final static String DESCRIPTION_KEY = "description";
    private final static String CREATED_TS_KEY = "created_at";
    private final static String LAST_MODIFY_TS_KEY = "last_modify_ts";
    private final static String STATUS_KEY = "status";
    private final static String POS_KEY = "pos";
    private final static String PHOTO_KEY = "photo";
    private final static String PHOTO_AFTER_KEY = "photo_after";



    @JsonProperty(ID_KEY)
    private String id;

    @JsonProperty(NAME_KEY)
    private String name;

    @JsonProperty(DESCRIPTION_KEY)
    private String descrition;

    @JsonProperty(CREATED_TS_KEY)
    private Long createTs;

    @JsonProperty(LAST_MODIFY_TS_KEY)
    private Long lastModifyTs;

    @JsonProperty(STATUS_KEY)
    private Status status;

    @JsonProperty(POS_KEY)
    private Pos pos;

    @JsonProperty(PHOTO_KEY)
    private String photo;

    @JsonProperty(PHOTO_AFTER_KEY)
    private String photoAfter;

    public Deed(@JsonProperty(ID_KEY) String id,
                @JsonProperty(NAME_KEY) String name,
                @JsonProperty(DESCRIPTION_KEY) String descrition,
                @JsonProperty(CREATED_TS_KEY) Long createdTs,
                @JsonProperty(LAST_MODIFY_TS_KEY) Long lastModifyTs,
                @JsonProperty(STATUS_KEY) Status status,
                @JsonProperty(POS_KEY) Pos pos,
                @JsonProperty(PHOTO_KEY) String photo,
                @JsonProperty(PHOTO_AFTER_KEY) String photoAfter) {
        this.id = id;
        this.name = name;
        this.descrition = descrition;
        this.createTs = createdTs != null ? createdTs: System.currentTimeMillis();
        this.lastModifyTs = lastModifyTs != null ? lastModifyTs: System.currentTimeMillis();
        this.status = status;
        this.pos = pos;
        this.photo = photo;
        this.photoAfter = photoAfter;
    }

    public Deed() {

    }

    public Deed setId(String id) {
        this.id = id;
        return this;
    }

    public Deed setName(String name) {
        this.name = name;
        return this;
    }

    public Deed setDescrition(String descrition) {
        this.descrition = descrition;
        return this;
    }

    public Deed setCreateTs(long createTs) {
        this.createTs = createTs;
        return this;
    }

    public Deed setLastModifyTs(long lastModifyTs) {
        this.lastModifyTs = lastModifyTs;
        return this;
    }

    public Deed setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Deed setPos(Pos pos) {
        this.pos = pos;
        return this;
    }

    public Deed setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public Deed setPhotoAfter(String photoAfter) {
        this.photoAfter = photoAfter;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescrition() {
        return descrition;
    }

    public Long getCreateTs() {
        return createTs;
    }

    public Long getLastModifyTs() {
        return lastModifyTs;
    }

    public Status getStatus() {
        return status;
    }

    public Pos getPos() {
        return pos;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhotoAfter() {
        return photoAfter;
    }
}
