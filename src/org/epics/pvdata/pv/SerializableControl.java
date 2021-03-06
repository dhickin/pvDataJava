/*
 * Copyright information and license terms for this software can be
 * found in the file LICENSE that is included with the distribution
 */
package org.epics.pvdata.pv;

import java.nio.ByteBuffer;


/**
 * Flush control interface.
 * @author mse
 */
public interface SerializableControl {

    /**
     * Request to flush serialization buffer.
     * This call will block until buffer is not flushed.
     * To be called when buffer is out of space.
     */
    void flushSerializeBuffer();
    
    
    /**
     * Ensures that the specified number of bytes can be serialized,
     * flushing if necessary.
     * 
     * @param size the number of bytes
     */
    void ensureBuffer(int size);

    /**
     * Align buffer.
     * Note that this takes care only current buffer alignment. If streaming protocol is used,
     * care must be taken that entire stream is aligned.
     * 
     * @param alignment size in bytes, must be power of two
     */
    void alignBuffer(int alignment);
    
    /**
     * Serialize <i>Field</i> instance via cache.
     * 
     * @param field <i>Field</i> instance to be serialized
     * @param buffer the buffer to be serialized to
     */
    void cachedSerialize(Field field, ByteBuffer buffer);
    
}
