/**
 * Copyright - See the COPYRIGHT that is included with this distribution.
 * EPICS JavaIOC is distributed subject to a Software License Agreement found
 * in file LICENSE that is included with this distribution.
 */
package org.epics.pvData.property;


/**
 * AlarmStatus definitions.
 * @author mrk
 *
 */
public enum AlarmStatus {
    /**
     * No alarm.
     */
    NONE,
    /**
     * An error conditioned generated by the hardware.
     */
    DEVICE,

    /**
     * An error conditioned raised by the driver (e.g. the device is not responding,
     * cannot write to it, ...)
     */
    DRIVER,

    /**
     * An error generated as part of the record calculation (e.g. alarm limits,
     * state alarm, error in the calculation)
     */
    RECORD,

    /**
     * An error generated by the interaction of multiple records.
     */
    DB,

    /**
     * An error generated by an error in configuration of one or multiple records.
     */
    CONF,

    /**
     * The status for a record that was never processed.
     */
    UNDEFINED,

    /**
     * An error generated by the client (e.g channel not found, disconnected, ...)
     */
    CLIENT;
    
    /**
     * get the alarm status.
     * @param value the integer value.
     * @return The alarm status.
     */
    public static AlarmStatus getStatus(int value) {
        switch(value) {
        case 0: return AlarmStatus.NONE;
        case 1: return AlarmStatus.DEVICE;
        case 2: return AlarmStatus.DRIVER;
        case 3: return AlarmStatus.RECORD;
        case 4: return AlarmStatus.DB;
        case 5: return AlarmStatus.CONF;
        case 6: return AlarmStatus.UNDEFINED;
        case 7: return AlarmStatus.CLIENT;
        }
        throw new IllegalArgumentException("AlarmStatus.getStatus("
            + ((Integer)value).toString() + ") is not a valid AlarmStatus");
    }
    
    private static final String[] alarmStatusNames = {
        "NONE","DEVICE","DRIVER","RECORD","DB","CONF","UNDEFINED","CLIENT"
    };
    public static String[] getStatusNames() { return alarmStatusNames;}
}
