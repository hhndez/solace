/**
 * Copyright 2004-2015 Solace Systems, Inc.  All rights reserved.
 *
 */
package com.solacesystems.solclientj.core.samples;

import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.SolclientException;
import com.solacesystems.solclientj.core.event.FlowEventCallback;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.ContextHandle;
import com.solacesystems.solclientj.core.handle.FlowHandle;
import com.solacesystems.solclientj.core.handle.MessageHandle;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import com.solacesystems.solclientj.core.resource.Queue;
import com.solacesystems.solclientj.core.resource.Topic;
import com.solacesystems.solclientj.core.resource.TopicEndpoint;
import com.solacesystems.solclientj.core.samples.common.*;

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
public class SimplePersistenceFlowToTopic extends AbstractSample {

	private ContextHandle contextHandle = Solclient.Allocator
			.newContextHandle();
	private SessionHandle sessionHandle = Solclient.Allocator
			.newSessionHandle();
	private MessageHandle txMessageHandle = Solclient.Allocator
			.newMessageHandle();
	private FlowHandle flowHandle = Solclient.Allocator.newFlowHandle();

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

			/*************************************************************************
			 * Enable Topic Dispatch on the Session.
			 *************************************************************************/
			sessionProps[sessionPropsIndex++] = SessionHandle.PROPERTIES.TOPIC_DISPATCH;
			sessionProps[sessionPropsIndex++] = SolEnum.BooleanValue.ENABLE;

			SessionEventCallback sessionEventCallback = getDefaultSessionEventCallback();
			MessageCallbackSample messageCallback = getMessageCallback(false);

			rc = contextHandle.createSessionForHandle(sessionHandle,
					sessionProps, messageCallback, sessionEventCallback);
			assertReturnCode("contextHandle.createSession()", rc,
					SolEnum.ReturnCode.OK);

			// Connect
			print(" Connecting session ...");
			rc = sessionHandle.connect();
			assertReturnCode("sessionHandle.connect()", rc,
					SolEnum.ReturnCode.OK);

			/*************************************************************************
			 * Create a Flow
			 *************************************************************************/

			// Flow Properties
			int flowProps = 0;
			String[] flowProperties = new String[10];

			flowProperties[flowProps++] = FlowHandle.PROPERTIES.BIND_BLOCKING;
			flowProperties[flowProps++] = SolEnum.BooleanValue.ENABLE;

			/*
			 * Send an ack when the message has been received. The default value
			 * is to automatically acknowledge on return from the message
			 * receive callback but it is recommended to use client
			 * aknowledgement when using flows.
			 */
			flowProperties[flowProps++] = FlowHandle.PROPERTIES.ACKMODE;
			flowProperties[flowProps++] = SolEnum.AckMode.CLIENT;

			FlowEventCallback flowEventCallback = getDefaultFlowEventCallback();
			MessageCallbackSample flowMessageCallback = new MessageCallbackSample(
					"Flow");


			Queue queue = null;

			//if (isDurable) {
				queue = Solclient.Allocator
						.newQueue(SampleUtils.SAMPLE_QUEUE);

			//}

			print("Creating flow using queue Name ["
					+ queue.getName() + "]");

			rc = sessionHandle.createFlowForHandle(flowHandle, flowProperties,
					queue, null, flowMessageCallback, new FlowEventCallback() {
						@Override
						public void onEvent(FlowHandle flowHandle) {

							print("Message: " + flowHandle.getRxMessage().dump(SolEnum.MessageDumpMode.FULL));
						}
					});
			
			print("Created flow for queue Name ["
					+ queue.getName() + "]");

			assertReturnCode("sessionHandle.createFlowForHandle", rc,
					SolEnum.ReturnCode.OK);

			/*************************************************************************
			 * Publish
			 *************************************************************************/

			int maxMessages = 1000;


			

			for (int publishCount = 0; publishCount < maxMessages; publishCount++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				print("Sending message " + publishCount);
				common_publishMessage(sessionHandle, txMessageHandle, queue,
						SolEnum.MessageDeliveryMode.PERSISTENT);

			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (flowMessageCallback.getMessageCount() != maxMessages) {
				throw new IllegalStateException(maxMessages
						+ " messages were expected, got ["
						+ flowMessageCallback.getMessageCount() + "] instead");
			} else
				print("Test Passed");

			// Clean up
			try {
				flowHandle.destroy();
				print("flowHandle.destroy");
			} catch (Throwable t) {
				error("Unable to destroy a flowHandle ", t);
			}



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
		SimplePersistenceFlowToTopic sample = new SimplePersistenceFlowToTopic();
		sample.run(args);
	}

}
