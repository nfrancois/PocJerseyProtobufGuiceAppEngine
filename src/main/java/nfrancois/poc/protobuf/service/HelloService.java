package nfrancois.poc.protobuf.service;

import nfrancois.poc.protobuf.model.HelloProto;
import nfrancois.poc.protobuf.model.HelloProto.Hello;

import com.google.inject.Singleton;

@Singleton
public class HelloService {
	
	public Hello saysHelloToSomeone(String name){
		return getHello(name);
	}
	
	public Hello sendHello(String name) {
		return getHello(name);
	}
	
	private Hello getHello(String name){
		return HelloProto.Hello.newBuilder().setName(name).setMessage("Hello").build();
	}
	
}
