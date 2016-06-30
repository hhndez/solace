/**
 * Copyright 2004-2015 Solace Systems, Inc.  All rights reserved.
 *
 */
package com.solacesystems.solclientj.core.samples;

import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.SolclientException;
import com.solacesystems.solclientj.core.event.FlowEventCallback;
import com.solacesystems.solclientj.core.event.MessageCallback;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.*;
import com.solacesystems.solclientj.core.resource.Topic;
import com.solacesystems.solclientj.core.resource.TopicEndpoint;
import com.solacesystems.solclientj.core.samples.common.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * 
 * SimpleFlowToTopic.java
 * 
 * This sample shows the following:
 * <ul>
 * <li>Binding Flows to a Topic Endpoint (non-durable or durable)
 * <li>Auto-acknowledgement
 * </ul>
 * 
 * For the durable Topic Endpoint, a durable Topic Endpoint called
 * 'my_sample_topicendpoint' must be provisioned on the Appliance with at least
 * 'Modify Topic' permissions.
 * 
 * <strong>This sample illustrates the ease of use of concepts, and may not be
 * GC-free.<br>
 * See Perf* samples for GC-free examples. </strong>
 * 
 */
public class SimpleConsumerFlowTopic extends AbstractSample {

	private ContextHandle contextHandle = Solclient.Allocator
			.newContextHandle();
	private SessionHandle sessionHandle = Solclient.Allocator
			.newSessionHandle();
	private MessageHandle txMessageHandle = Solclient.Allocator
			.newMessageHandle();
	private Topic topic = Solclient.Allocator
			.newTopic(SampleUtils.SAMPLE_TOPIC);
	@Override
	protected void printUsage(boolean secureSession) {
		String usage = ArgumentsParser.getCommonUsage(secureSession);
		usage += "This sample:\n";
		usage += "\t[-d true|false]\t durable or non durable, default: false\n";
		System.out.println(usage);
		finish(1);
	}

	/**
	 * This is the main method of the sample
	 */
	@Override
	protected void run(String[] args, SessionConfiguration config,Level logLevel)
			throws SolclientException {

		try {

			// Default false
			boolean isDurable = false;

			String isDurableBoolStr = config.getArgBag().get("-d");
			if (isDurableBoolStr != null) {
				try {
					isDurable = Boolean.parseBoolean(isDurableBoolStr);
				} catch (Exception e) {
					printUsage(config instanceof SecureSessionConfiguration);
				}
			}

			// Init
			print(" Initializing the Java RTO Messaging API...");
			int rc = Solclient.init(new String[0]);
			assertReturnCode("Solclient.init()", rc, SolEnum.ReturnCode.OK);
			
			// Set a log level (not necessary as there is a default)
			Solclient.setLogLevel(logLevel);

			// Context
			print(" Creating a context ...");
			rc = Solclient.createContextForHandle(contextHandle, new String[0]);
			assertReturnCode("Solclient.createContext()", rc,
					SolEnum.ReturnCode.OK);

			// Session
			print(" Creating a session ...");
			int spareRoom = 2;
			String[] sessionProps = getSessionProps(config, spareRoom);
			int sessionPropsIndex = sessionProps.length - spareRoom;
			final AtomicInteger messageCount = new AtomicInteger(0);
			int maxMessages = 1000;

			rc = contextHandle.createSessionForHandle(sessionHandle,
					sessionProps, new MessageCallback() {
						@Override
						public void onMessage(Handle handle) {
							MessageSupport obj = (MessageSupport) handle;
							messageCount.incrementAndGet();
							print("A new message:" + obj.getRxMessage().dump(SolEnum.MessageDumpMode.FULL));
						}
					}, new SessionEventCallback() {
						@Override
						public void onEvent(SessionHandle sessionHandle) {
							print("Event: " + sessionHandle.getSessionEvent());
						}
					});
			assertReturnCode("contextHandle.createSession()", rc,
					SolEnum.ReturnCode.OK);

			// Connect
			print(" Connecting session ...");
			rc = sessionHandle.connect();
			assertReturnCode("sessionHandle.connect()", rc,
					SolEnum.ReturnCode.OK);

			rc = sessionHandle.subscribe(topic,
					SolEnum.SubscribeFlags.WAIT_FOR_CONFIRM, 0);
			assertReturnCode("sessionHandle.subscribe()", rc, SolEnum.ReturnCode.OK);

			while (messageCount.get() < maxMessages) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (messageCount.get() != maxMessages) {
				throw new IllegalStateException(maxMessages
						+ " messages were expected, got ["
						+ messageCount.get() + "] instead");
			} else
				print("Test Passed");



		} catch (Throwable t) {
			error("An error has occurred ", t);
		}
	}

	/**
	 * Invoked when the sample finishes
	 */
	@Override
	protected void finish(int status) {
		/*************************************************************************
		 * Cleanup
		 *************************************************************************/

		finish_DestroyHandle(txMessageHandle, "messageHandle");

		finish_Disconnect(sessionHandle);

		finish_DestroyHandle(sessionHandle, "sessionHandle");

		finish_DestroyHandle(contextHandle, "contextHandle");

		finish_Solclient();

	}

/**
     * Boilerplate, calls {@link #run(String[])
     * @param args
     */
	public static void main(String[] args) {
		SimpleConsumerFlowTopic sample = new SimpleConsumerFlowTopic();
		sample.run(args);
	}

}
