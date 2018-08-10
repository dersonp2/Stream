import Stream.SensorStream;

public class MainStream {

    public static void main(String[] args) {
        SensorStream sensorStream = new SensorStream("http://mycsparql.lsdi/stream");
        sensorStream.run();
    }
}
