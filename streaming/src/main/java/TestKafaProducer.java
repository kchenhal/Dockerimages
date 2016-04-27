import com.lgc.distarch.kafka.CmdProducer;

public class TestKafaProducer {

	public static void main(String[] args) throws InterruptedException {
		CmdProducer producer = new CmdProducer("34.36.105.44:9092");
		String text1= "{\"requestId\":\"request-3\",\"gateWayUrl\":\"ws://localhost:8000\",\"cmd\":\"getFile\",\"cmdArguments\":\"e:\\\\tmp\\\\sd.xml\"}";
		producer.publish("topic", null);
		Thread.sleep(60000);
		System.out.println("finished publishing");
	}

}
