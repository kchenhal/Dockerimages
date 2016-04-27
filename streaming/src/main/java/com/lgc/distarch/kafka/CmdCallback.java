package com.lgc.distarch.kafka;

import com.lgc.distarch.streaming.filetransfer.CommandItem;

public interface CmdCallback {
	void run(CommandItem item);
}
