package nfrancois.poc.protobuf;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nfrancois.poc.protobuf.io.ProtobufMessageBodyReader;
import nfrancois.poc.protobuf.io.ProtobufMessageBodyWriter;
import nfrancois.poc.protobuf.model.HelloProto;
import nfrancois.poc.protobuf.model.HelloProto.Hello;
import nfrancois.poc.protobuf.resource.HelloResource;
import nfrancois.poc.protobuf.service.HelloService;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class HelloResourceTest extends JerseyTest {
	
	private static Injector injector;	
	private HelloService helloServiceMock;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		HelloResource helloResource =  injector.getInstance(HelloResource.class);
		helloServiceMock = mock(HelloService.class);
		helloResource.setHelloService(helloServiceMock);
	}	
	

	protected AppDescriptor configure() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(ProtobufMessageBodyReader.class);
		clientConfig.getClasses().add(ProtobufMessageBodyWriter.class);
		injector = Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				bind(ProtobufMessageBodyReader.class);
				bind(ProtobufMessageBodyWriter.class);
				bind(HelloResource.class);
				serve("/*").with(GuiceContainer.class);
			}
		});	
		return new WebAppDescriptor.Builder()
			        .contextListenerClass(GuiceTestConfig.class)
			        .filterClass(GuiceFilter.class)
			        .clientConfig(clientConfig)
			        .servletPath("/")
			        .build();
	}	
	
	
	@Test
	public void shoulReplyHello(){
		// Given
		String message = "Hello";
		String name ="Nicolas";
		Hello hello = HelloProto.Hello.newBuilder().setName(name).setMessage(message).build();
		when(helloServiceMock.saysHelloToSomeone("Nicolas")).thenReturn(hello);
		// When
		ClientResponse response = resource().path("hello").path(name).get(ClientResponse.class);
		// Then
		verify(helloServiceMock).saysHelloToSomeone(name);
		assertThat(response.getClientResponseStatus()).isEqualTo(Status.OK);
		assertThat(response.getType().toString()).isEqualTo("application/x-protobuf");
		Hello entity = response.getEntity(Hello.class);
		assertThat(entity).isNotNull().isEqualTo(hello);
	}
	
	private class GuiceTestConfig extends GuiceServletContextListener {
		@Override
		public Injector getInjector() {
			return injector;
		}
	}	

}
