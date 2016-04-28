package com.lgc.distarch.streaming.filetransfer;

import java.io.Serializable;

import com.google.gson.Gson;

public class CommandItem implements Serializable{
	private static final long serialVersionUID = 3998104983410654462L;
	
	public String requestId;
	public String gateWayUrl;
	public String cmd;
	public String cmdArguments; 
	
	public CommandItem(String requestId, String gateWayUrl, String cmd, String args) {
		this.requestId = requestId;
		this.cmd = cmd;
		this.cmdArguments = args;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static CommandItem fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, CommandItem.class);
	}
	
	public static void main(String[] args) {
		CommandItem item = new CommandItem("requestid", "ws://localhost:8000", "getFile", "e:\\tmp\\sd.xml");
		String json = item.toJson();
		
		System.out.println("the json string is: " + json);
		
		CommandItem newItem = CommandItem.fromJson(json);
		System.out.println("the new object is: "+newItem.requestId+","+newItem.gateWayUrl+":"+ newItem.cmd+":"+newItem.cmdArguments);
		
		String text1= "{\"requestId\":\"request-0\",\"gateWayUrl\":\"ws://localhost:8000\",\"cmd\":\"getFile\",\"cmdArguments\":\"e:\\\\tmp\\\\sd.xml\"}";
		newItem = CommandItem.fromJson(text1);
		System.out.println("the new object is: "+newItem.requestId+","+newItem.gateWayUrl+":"+ newItem.cmd+":"+newItem.cmdArguments);
		
	}
}
