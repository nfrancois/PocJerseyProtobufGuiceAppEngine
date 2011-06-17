package nfrancois.poc.protobuf;

import nfrancois.poc.protobuf.io.ProtobufMessageBodyReader;
import nfrancois.poc.protobuf.io.ProtobufMessageBodyWriter;
import nfrancois.poc.protobuf.resource.HelloResource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceServletConfig extends GuiceServletContextListener {
	
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {

			@Override
			protected void configureServlets() {
				bind(ProtobufMessageBodyReader.class);
				bind(ProtobufMessageBodyWriter.class);
				bind(HelloResource.class);
				serve("/*").with(GuiceContainer.class);
			}
		});
	}
}