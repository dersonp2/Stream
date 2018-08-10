package StartBroker;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;


public class StartBroker {
    //Mqtt
    private static String brokerUrl = "tcp://lsdi.ufma.br:1883";
    private static MqttClient client = null;
    private String tmpDir = System.getProperty("java.io.tmpdir");
    private MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
    //Logger

    //Variável Singleton
    public static StartBroker instance = null;

    //Método Singleton
    public static StartBroker getInstance() {
        if (instance == null) {
            instance = new StartBroker();
        }
        return instance;
    }

    public MqttClient Connection(String clientId) {
        if (client == null) {
            try {
                client = new MqttClient(brokerUrl, clientId, dataStore);
                client.connect();
                System.out.println("Conectado");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return client;
    }
}
