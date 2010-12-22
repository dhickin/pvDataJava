/**
 * Copyright - See the COPYRIGHT that is included with this distribution.
 * EPICS pvData is distributed subject to a Software License Agreement found
 * in file LICENSE that is included with this distribution.
 */
package org.epics.pvData.property;

import org.epics.pvData.pv.MessageType;
import org.epics.pvData.pv.PVField;
import org.epics.pvData.pv.PVInt;
import org.epics.pvData.pv.PVScalarArray;
import org.epics.pvData.pv.PVStringArray;
import org.epics.pvData.pv.PVStructure;
import org.epics.pvData.pv.ScalarType;
import org.epics.pvData.pv.StringArrayData;
import org.epics.pvData.pv.Type;

public final class PVEnumeratedFactory implements PVEnumerated{
    private PVInt pvIndex = null;
    private PVStringArray pvChoices = null;
    private StringArrayData data = new StringArrayData();
    private static final String notStructure = "field is not a structure";
    private static final String notEnumerated = "field is not an enumerated structure";
    private static final String notAttached = "Not attached to an enumerated structure";

    /**
     * Create a PVControl.
     * @return The newly created PVControl.
     */
    public static PVEnumerated create() { return new PVEnumeratedFactory();} 
    /* (non-Javadoc)
     * @see org.epics.pvData.property.PVControl#attach(org.epics.pvData.pv.PVField)
     */
    @Override
    public boolean attach(PVField pvField) {
        if(pvField.getField().getType()!=Type.structure) {
            pvField.message(notStructure,MessageType.error);
            return false;
        }
        PVStructure pvStructure = (PVStructure)pvField;
        PVInt pvInt = pvStructure.getIntField("index");
        if(pvInt==null) {
            pvField.message(notEnumerated,MessageType.error);
            return false;
        }
        PVScalarArray pvScalarArray = pvStructure.getScalarArrayField("choices",ScalarType.pvString);
        if(pvScalarArray==null) {
            pvField.message(notEnumerated,MessageType.error);
            return false;
        }
        pvIndex = pvInt;
        pvChoices = (PVStringArray)pvScalarArray;
        return true;

    }
    /* (non-Javadoc)
     * @see org.epics.pvData.property.PVControl#detach()
     */
    @Override
    public void detach() {
        pvIndex = null;
        pvChoices = null;

    }
    /* (non-Javadoc)
     * @see org.epics.pvData.property.PVControl#isAttached()
     */
    @Override
    public boolean isAttached() {
        if(pvIndex==null || pvChoices==null) return false;
        return true;

    }
    @Override
    public boolean choicesMutable() {
        if(pvIndex==null || pvChoices==null) {
            throw new IllegalStateException(notAttached);
       }
       return pvChoices.isImmutable();
    }
    @Override
    public String getChoice() {
        if(pvIndex==null || pvChoices==null) {
            throw new IllegalStateException(notAttached);
       }
       int index = pvIndex.get();
       pvChoices.get(0,pvChoices.getLength(),data);
       return data.data[index];

    }
    @Override
    public String[] getChoices() {
        if(pvIndex==null || pvChoices==null) {
            throw new IllegalStateException(notAttached);
       }
       pvChoices.get(0,pvChoices.getLength(),data);
       return data.data;

    }
    @Override
    public int getIndex() {
        if(pvIndex==null || pvChoices==null) {
            throw new IllegalStateException(notAttached);
       }
       return pvIndex.get();

    }
    @Override
    public boolean setChoices(String[] choices) {
        if(pvIndex==null || pvChoices==null) {
            throw new IllegalStateException(notAttached);
       }
       if(pvChoices.isImmutable()) return false;
       pvChoices.put(0,choices.length,choices,0);
       return true;

    }
    @Override
    public boolean setIndex(int index) {
        if(pvIndex==null || pvChoices==null) {
            throw new IllegalStateException(notAttached);
       }
       if(pvIndex.isImmutable()) return false;
       pvIndex.put(index);
       return true;

    }
    
}
