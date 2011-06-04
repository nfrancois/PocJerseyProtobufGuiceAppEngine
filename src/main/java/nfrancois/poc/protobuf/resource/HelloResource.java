package nfrancois.poc.protobuf.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nfrancois.poc.protobuf.model.HelloProto.Hello;
import nfrancois.poc.protobuf.service.HelloService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Path("hello")
@Singleton
@Produces("application/x-protobuf")
public class HelloResource {
	
	@Inject
	private HelloService helloService;
	
	@GET
	@Path("/{name}")
	public Hello reply(@PathParam("name") String name){
		return helloService.saysHelloToSomeone(name);
	}
	
	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}
	
}
