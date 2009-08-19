/**
 * Copyright - See the COPYRIGHT that is included with this distribution.
 * EPICS JavaIOC is distributed subject to a Software License Agreement found
 * in file LICENSE that is included with this distribution.
 */
package org.epics.pvData.monitor;


import org.epics.pvData.misc.BitSet;
import org.epics.pvData.misc.Executor;
import org.epics.pvData.pv.MessageType;
import org.epics.pvData.pv.PVDouble;
import org.epics.pvData.pv.PVField;
import org.epics.pvData.pv.PVRecord;
import org.epics.pvData.pv.PVScalar;
import org.epics.pvData.pv.PVStructure;
import org.epics.pvData.pv.Scalar;
import org.epics.pvData.pv.Type;
import org.epics.pvData.pvCopy.PVCopy;

/**
 * @author mrk
 *
 */
public class MonitorOnAbsoluteChangeFactory {
    private static final String name = "onAbsoluteChange";
    private static final MonitorOnAbsoluteChange monitorOnAbsoluteChange = new MonitorOnAbsoluteChange();

    public static MonitorCreate getMonitorCreate() {
        return monitorOnAbsoluteChange;
    }

    private static class MonitorOnAbsoluteChange implements MonitorCreate {
    
        /* (non-Javadoc)
         * @see org.epics.pvData.monitor.MonitorCreate#create(org.epics.pvData.pv.PVRecord, org.epics.pvData.monitor.MonitorRequester, org.epics.pvData.pv.PVStructure, org.epics.pvData.pvCopy.PVCopy, int)
         */
        @Override
        public Monitor create(PVRecord pvRecord,
                MonitorRequester monitorRequester, PVStructure pvOption,
                PVCopy pvCopy, int queueSize)
        {
            PVStructure pvStructure = pvCopy.createPVStructure();
            PVDouble pvDeadband = pvOption.getDoubleField("deadband");
            if(pvDeadband==null) {
                monitorRequester.message("deadband field not defined", MessageType.error);
                return null;
            }
            PVField pvField = pvStructure.getSubField("value");
            if(pvField==null) {
                monitorRequester.message("value field not defined", MessageType.error);
                return null;
            }
            if(pvField.getField().getType()!=Type.scalar) {
                monitorRequester.message("value is not a scalar", MessageType.error);
                return null;
            }
            Scalar scalar = (Scalar)pvField.getField();
            if(!scalar.getScalarType().isNumeric()) {
                monitorRequester.message("value is not a numeric scalar", MessageType.error);
                return null;
            }
            pvField = pvCopy.getRecordPVField(pvField.getFieldOffset());
            return new Monitor(pvRecord,monitorRequester,pvCopy,queueSize,pvDeadband.get(),(PVScalar)pvField);
        }
        /* (non-Javadoc)
         * @see org.epics.ioc.channelAccess.MonitorCreate#getName()
         */
        @Override
        public String getName() {
            return name;
        }
    }
    
    private static class Monitor extends BaseMonitor {
        private Monitor(
                PVRecord pvRecord,
                MonitorRequester monitorRequester,
                PVCopy pvCopy,
                int queueSize,
                double deadband,
                PVScalar valuePVField)
        {
            super(pvRecord,monitorRequester,pvCopy,queueSize);
            this.deadband = deadband;
            this.valuePVField = valuePVField;
            prevValue = convert.toDouble(valuePVField);
        }
        
        private double deadband = 0.0;
        private PVScalar valuePVField = null;
        private double prevValue;
        /* (non-Javadoc)
         * @see org.epics.ioc.channelAccess.BaseMonitor#generateMonitor(org.epics.pvData.misc.BitSet)
         */
        @Override
        protected boolean generateMonitor(BitSet changeBitSet) {
            double value = convert.toDouble(valuePVField);
            double diff = value - prevValue;
            if(Math.abs(diff) < deadband) return false;
            prevValue = value;
            return true;
        }
    }
}