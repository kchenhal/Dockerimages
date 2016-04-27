/**
 * Copyright 2007-2016, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.lgc.distarch.kafka.CmdCallback;
import com.lgc.distarch.kafka.CmdConsumer;
import com.lgc.distarch.kafka.CmdProducer;
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
