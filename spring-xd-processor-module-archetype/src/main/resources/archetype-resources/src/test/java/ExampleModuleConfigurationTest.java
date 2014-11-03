/*
 * Copyright 2014 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package $package;

import static org.junit.Assert.*;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

/**
 * Tests a Spring XD processor module's XML configuration prior to deploying it to an Spring XD container.
 *
 * @author David Turanski
 */
public class ExampleModuleConfigurationTest {
	private ClassPathXmlApplicationContext context;

	@Before
	public void setUp() {
		context = new ClassPathXmlApplicationContext();
		context.setConfigLocation("module-configuration.xml");
	}

	@Test
	public void testPrefixAndSuffix() {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();

		Properties moduleOptions = new Properties();
		moduleOptions.put("prefix", "foo");
		moduleOptions.put("suffix", "bar");
		configureTestContext(moduleOptions, "use-both");
		runTest("hello", "foohellobar");
	}

	@Test
	public void testPrefixOnly() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		Properties moduleOptions = new Properties();
		moduleOptions.put("prefix", "foo");
		configureTestContext(moduleOptions, "use-prefix");
		runTest("hello", "foohello");
	}

	/**
	 * Configure the application context.
	 *
	 * @param moduleOptions property values used to configure the module instance
	 * @param activeProfiles the active profiles to use
	 */
	private void configureTestContext(Properties moduleOptions, String... activeProfiles) {
		context.getEnvironment().getPropertySources().addLast(new PropertiesPropertySource("options", moduleOptions));
		context.getEnvironment().setActiveProfiles(activeProfiles);
		context.refresh();
	}

	/**
	 * Send a message to the module's input channel and verify the ouput.
	 *
	 * @param payload the payload sent to the input
	 * @param expectedResult the payload at the output channel
	 */
	private void runTest(Object payload, final Object expectedResult) {
		MessageChannel input = context.getBean("input", MessageChannel.class);
		SubscribableChannel output = context.getBean("output", SubscribableChannel.class);

		final AtomicBoolean handled = new AtomicBoolean();
		output.subscribe(new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				handled.set(true);
				assertEquals(expectedResult, message.getPayload());
			}
		});
		input.send(new GenericMessage<Object>(payload));
		assertTrue(handled.get());
	}

}
