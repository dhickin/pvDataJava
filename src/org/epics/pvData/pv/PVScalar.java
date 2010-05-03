/**
 * Copyright - See the COPYRIGHT that is included with this distribution.
 * EPICS JavaIOC is distributed subject to a Software License Agreement found
 * in file LICENSE that is included with this distribution.
 */
package org.epics.pvData.pv;

/**
 * PVScalar extends PVField for a scalar field.
 * @author mrk
 *
 */
public interface PVScalar extends PVField{
    /**
     * Get the Scalar reflection interface.
     * @return The Scalar interface.
     */
    Scalar getScalar();
}