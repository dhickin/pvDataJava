/**
 * Copyright - See the COPYRIGHT that is included with this distribution.
 * EPICS JavaIOC is distributed subject to a Software License Agreement found
 * in file LICENSE that is included with this distribution.
 */
package org.epics.pvData;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

import org.epics.pvData.factory.StatusFactory;
import org.epics.pvData.pv.DeserializableControl;
import org.epics.pvData.pv.SerializableControl;
import org.epics.pvData.pv.Status;
import org.epics.pvData.pv.Status.StatusType;
import org.epics.pvData.pv.StatusCreate;

/**
 * JUnit test for Status.
 * @author mse
 *
 */
public class StatusTest extends TestCase {

	private static class SerializableFlushImpl implements SerializableControl {

		@Override
		public void ensureBuffer(int size) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void flushSerializeBuffer() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void alignBuffer(int alignment) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private static SerializableControl flusher = new SerializableFlushImpl();
	

	private static class DeserializableControlImpl implements DeserializableControl {

		@Override
		public void ensureData(int size) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void alignData(int alignment) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private static DeserializableControl control = new DeserializableControlImpl();
	
	private static final StatusCreate statusCreate = StatusFactory.getStatusCreate();

	public void testStatusOK()
	{
		assertSame(statusCreate.getStatusOK(), statusCreate.getStatusOK());
	}

	public void testStatusPrints()
	{
		System.out.println(statusCreate.getStatusOK());
		System.out.println(statusCreate.createStatus(StatusType.OK, null, null));
		System.out.println(statusCreate.createStatus(StatusType.WARNING, "warning", null));
		System.out.println(statusCreate.createStatus(StatusType.ERROR, "error", new RuntimeException("simple exception")));
		
		try {
			throw new RuntimeException("simulated cause");
		} catch (Throwable cause) {
			try {
				throw new RuntimeException("simulated exc", cause);
			} catch (Throwable th) {
				System.out.println(statusCreate.createStatus(StatusType.FATAL, "fatal", th));
			}
		}
	}

	public void testSerializationOKStatus() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		Status okStatus = statusCreate.getStatusOK();
		okStatus.serialize(buffer, flusher);
		
		buffer.flip();
		
		Status deserializedStatus = statusCreate.deserializeStatus(buffer, control);
		
		assertSame(okStatus, deserializedStatus);
	}
	
	public void testSerialization() {
		ByteBuffer buffer = ByteBuffer.allocate(1024*2);
		
		Status status = statusCreate.createStatus(StatusType.ERROR, "error", new RuntimeException("simple exception"));

		status.serialize(buffer, flusher);
		
		buffer.flip();
		
		Status deserializedStatus = statusCreate.deserializeStatus(buffer, control);
		
		assertEquals(status, deserializedStatus);
	}
	
}
