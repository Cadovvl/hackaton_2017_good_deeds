package cadovvl.cadovvl.cadovvl.gd;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonSerializationTest {
    private ObjectMapper mapper = new ObjectMapper();

    private final static double DELTA = 1.e-07;

    @Test
    public void testPosSerialization() throws Exception {
        assertEquals("{\"lat\":13.12,\"lon\":25.67}",
                mapper.writeValueAsString(new Pos(13.12, 25.67)));
        assertEquals("{\"lat\":13.12,\"lon\":25.67}",
                mapper.writeValueAsString(new Pos().setLat(13.12).setLon(25.67)));
    }

    @Test
    public void testDeedSerialization() throws Exception {
        assertEquals("{\"name\":\"asd\"}", mapper.writeValueAsString(new Deed().setName("asd")));
        assertEquals("{\"id\":1234,\"name\":\"asd\"}",
                mapper.writeValueAsString(new Deed()
                .setName("asd")
                .setId(1234L)));
    }


    @Test
    public void testPosParse() throws Exception {
        Pos expected = new Pos(13.12, 25.67);
        Pos parsed = mapper.readValue("{\"lat\":13.12,\"lon\":25.67}", Pos.class);

        assertEquals(expected.getLat(), parsed.getLat(), DELTA);
        assertEquals(expected.getLon(), parsed.getLon(), DELTA);
    }

}
