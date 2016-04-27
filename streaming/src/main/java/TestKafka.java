import com.lgc.distarch.kafka.CmdCallback;
import com.lgc.distarch.kafka.CmdConsumer;
import com.lgc.distarch.streaming.filetransfer.CommandItem;

public class TestKafka {

	public static void main(String[] args) {
		
		CmdConsumer consumer = new CmdConsumer("34.36.105.44:9092", "topic");
		consumer.consume(new CmdCallback() {
			
			@Override
			public void run(CommandItem item) {
				System.out.println("got: "+ item.cmd+"/"+item.cmdArguments+"/"+item.requestId);
			}
		});

	}

}
