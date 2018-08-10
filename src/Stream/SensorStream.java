package Stream;

import Model.OntologyPrefix;
import Model.SemanticSensorData;
import StartBroker.StartBroker;
import com.google.gson.Gson;
import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SensorStream extends RdfStream implements Runnable {
    RdfQuadruple q1, q2, q3, q4;
    OntologyPrefix onto = new OntologyPrefix();
    private static MqttClient client;
    private boolean keepRunning;
    long tempTS;
    int result = 0;

    public SensorStream(String iri) {
        super(iri);
        connect();
    }

    @Override
    public void run() {
        keepRunning = true;
        while (keepRunning == true) {
            try {

                Thread.sleep(1000);

                result+=13;
                //Primeira Observação
                tempTS = System.currentTimeMillis();
                q1 = new RdfQuadruple(onto.getIotlite() + "FFE1-0000-1000-8000-00805F9B34FB", onto.getRdf()
                        + ":Type", onto.getSsn() + "SensingDevice", tempTS);
                q2 = new RdfQuadruple(onto.getIotlite() + "FFE1-0000-1000-8000-00805F9B34FB", onto.getIotlite()
                        + ":hasUnit", onto.getIotlite() + "Celsius", tempTS);
                q3 = new RdfQuadruple(onto.getIotlite() + "FFE1-0000-1000-8000-00805F9B34FB", onto.getIotlite()
                        + ":hasQuatityKind", onto.getIotlite() + "Temperature", tempTS);
                q4 = new RdfQuadruple(onto.getIotlite() + "FFE1-0000-1000-8000-00805F9B34FB", onto.getSosa()
                        + ":hasResult", onto.getSosa() + "Tempera", tempTS);

                RdfQuadruple[] rdfQuadruple = new RdfQuadruple[]{q1, q2, q3, q4};
                pubResult(rdfQuadruple);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pubResult(RdfQuadruple[] q) {
          connect();
        SemanticSensorData data = new SemanticSensorData();
        data.setRdfQuadruple(q);
        Gson gson = new Gson();
        String result = gson.toJson(data);
        MqttMessage msg = new MqttMessage((result).getBytes());
        msg.setQos(1);
        msg.setRetained(false);
        try {
            client.publish("Stream", msg);
            System.out.println("\n" + "Publicou: "+tempTS);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        client = StartBroker.getInstance().Connection("SSD");
    }
}
