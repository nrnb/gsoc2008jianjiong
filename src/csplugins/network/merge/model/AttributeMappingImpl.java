/* File: AttributeMappingImpl.java

 Copyright (c) 2006, 2007, The Cytoscape Consortium (www.cytoscape.org)

 The Cytoscape Consortium is:
 - Institute for Systems Biology
 - University of California San Diego
 - Memorial Sloan-Kettering Cancer Center
 - Institut Pasteur
 - Agilent Technologies

 This library is free software; you can redistribute it and/or modify it
 under the terms of the GNU Lesser General Public License as published
 by the Free Software Foundation; either version 2.1 of the License, or
 any later version.

 This library is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 documentation provided hereunder is on an "as is" basis, and the
 Institute for Systems Biology and the Whitehead Institute
 have no obligations to provide maintenance, support,
 updates, enhancements or modifications.  In no event shall the
 Institute for Systems Biology and the Whitehead Institute
 be liable to any party for direct, indirect, special,
 incidental or consequential damages, including lost profits, arising
 out of the use of this software and its documentation, even if the
 Institute for Systems Biology and the Whitehead Institute
 have been advised of the possibility of such damage.  See
 the GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */

package csplugins.network.merge.model;

import csplugins.network.merge.util.AttributeValueCastUtils;

import cytoscape.data.CyAttributes;
import cytoscape.data.Semantics;

import java.util.List;
import java.util.Vector;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

/**
 * Class to instore the information how to mapping the attributes 
 * in the original networks to those in the resulting networks
 * 
 * 
 */
public class AttributeMappingImpl implements AttributeMapping {
    private Map<String,List<String>> attributeMapping; //attribute mapping
    private List<String> attributeMerged;
    private CyAttributes cyAttributes;
    private final String nullAttr = ""; // to hold a position in vector standing that it's not a attribute

    public AttributeMappingImpl(final CyAttributes cyAttributes) {
        this.cyAttributes = cyAttributes;
        attributeMapping = new HashMap<String,List<String>>();
        attributeMerged = new Vector<String>();
    }

    @Override
    public CyAttributes getCyAttributes() {
        return cyAttributes;
    }
    
    /*
     * Get attributes' names in the merged network
     * 
     */
    @Override
    public String[] getMergedAttributes() {
        return (String[])attributeMerged.toArray(new String[0]);
    }
   
    /*
     * Get number of the attribute in the merged network
     * 
     */
    @Override
    public int getSizeMergedAttributes() {
        return attributeMerged.size();
    }
            
    /*
     * Get the ith attribute name in the merged network
     * 
     */
    @Override
    public String getMergedAttribute(final int index) {
        if (index<0 || index>=getSizeMergedAttributes()) {
            throw new java.lang.IndexOutOfBoundsException("Index out of boundary.");
        }
        
        //if (index>=attributeMerged.size()) return null;
        return attributeMerged.get(index);
    }
     
    /*
     * Set the ith attribute name in the merged network
     * 
     */
    @Override
    public String setMergedAttribute(final int index, final String attributeName) {
        if (attributeName==null) {
            throw new java.lang.NullPointerException("Attribute name is null.");
        }
        
        String attr = attributeName;
        
        if (attributeExistsInOriginalNetwork(attributeName)) {
            final Set<String> attrNames = new HashSet(getOriginalAttributeMap(index).values());
            final String attr_mc = AttributeValueCastUtils.getMostCompatibleAttribute(attrNames, cyAttributes);
            if (attr_mc==null) { // inconvertible
                if (cyAttributes.getType(attributeName)!=CyAttributes.TYPE_STRING) {
                    attr = this.getDefaultMergedAttrName(attr, true);
                    //return null;
                }
            } else { // convertible
                if (!AttributeValueCastUtils.isAttributeTypeConvertable(attr_mc, attributeName, cyAttributes)) {
                    attr = this.getDefaultMergedAttrName(attr, true);
                    //return null;
                }
            }
        }
        
        return attributeMerged.set(index, attr);
        
    }
            
    /*
     * Check if an attribute exists in the merged attributes
     * 
     */
    @Override
    public boolean containsMergedAttributes(final String attributeName) {
        if (attributeName==null) {
            throw new java.lang.NullPointerException("Attribute name is null.");
        }
        return attributeMerged.contains(attributeName);
    }
    
    /*
     * Get the original attribute name in the network before merged
     * 
     */
    @Override
    public String getOriginalAttribute(final String netID, final String mergedAttributeName) {
        if (netID==null||mergedAttributeName==null) {
            throw new java.lang.NullPointerException("Null netID or mergedAttributeName");
        }
        final int index = attributeMerged.indexOf(mergedAttributeName);
        if (index==-1) {
            throw new java.lang.IllegalArgumentException("No "+mergedAttributeName+" is contained in merged attributes");
        }
        return getOriginalAttribute(netID, index);
    }
    
    /*
     * Get the original attribute name before merged, corresponding to the ith merged attribute
     * 
     */
    @Override
    public String getOriginalAttribute(final String netID, final int index) {
        final List<String> attrs = attributeMapping.get(netID);
        if (attrs==null) {
            throw new java.lang.IllegalArgumentException(netID+" is not selected as merging network");
        }
        if (index>=attrs.size()||index<0)  {
            throw new java.lang.IndexOutOfBoundsException();
        }
        final String attr = attrs.get(index);
        if (attr.compareTo(nullAttr)==0) return null;
        return attr;
    }
        
    /*
     * Get the original attribute name in the network before merged, corresponding to the merged attribute
     * 
     * @return the original attribute if exist, null otherwise
     */
    @Override
    public Map<String,String> getOriginalAttributeMap(String mergedAttributeName) {
        if (mergedAttributeName==null) {
            throw new java.lang.NullPointerException("Null netID or mergedAttributeName");
        }
        final int index = attributeMerged.indexOf(mergedAttributeName);
        if (index==-1) {
            throw new java.lang.IllegalArgumentException("No "+mergedAttributeName+" is contained in merged attributes");
        }
        return getOriginalAttributeMap(index);        
    }
    
    /*
     * Get the original attribute name before merged, corresponding to the ith merged attribute
     * 
     * @return the original attribute if exist, null otherwise
     */
    @Override
    public Map<String,String> getOriginalAttributeMap(int index) {
        if (index>=this.getSizeMergedAttributes()||index<0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        Map<String,String> return_this = new HashMap<String,String>();
        
        final Iterator<Map.Entry<String,List<String>>> it = attributeMapping.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String,List<String>> entry = it.next();
            final String netID = entry.getKey();
            final List<String> attrs = entry.getValue();
            final String attr = attrs.get(index);
            if (attr.compareTo(nullAttr)!=0) {
                return_this.put(netID, attr);
            }
        }
        
        return return_this;
    }
    
    /*
     * Set attribute mapping
     * 
     */
    @Override
    public String setOriginalAttribute(final String netID, final String attributeName, final String mergedAttributeName) {
        if (netID==null||mergedAttributeName==null) {
            throw new java.lang.NullPointerException("Null netID or mergedAttributeName");
        }
        final int index = attributeMerged.indexOf(mergedAttributeName);
        if (index==-1) {
            throw new java.lang.IllegalArgumentException("No "+mergedAttributeName+" is contained in merged attributes");
        }
        return setOriginalAttribute(netID, attributeName, index);
    }
            
    /*
     * Set attribute mapping
     * 
     */
    @Override
    public String setOriginalAttribute(final String netID, final String attributeName, final int index){
        if (netID==null||attributeName==null||attributeName==null) {
            throw new java.lang.NullPointerException("Null netID or attributeName or mergedAttributeName");
        }
        
        if (!Arrays.asList(cyAttributes.getAttributeNames()).contains(attributeName)) {
            throw new java.lang.IllegalArgumentException("No "+attributeName+" is contained in attributes");
        }
        
        final List<String> attrs = attributeMapping.get(netID);
        if (attrs==null) return null;
        if (index>=attrs.size()||index<0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        final String old = attrs.get(index);
        if (old.compareTo(attributeName)!=0) { // not the same                     
            attrs.set(index, attributeName);

            String mergedAttr = getMergedAttribute(index);
            if (attributeExistsInOriginalNetwork(mergedAttr)
                && !AttributeValueCastUtils.isAttributeTypeConvertable(attributeName,
                                                                      mergedAttr, 
                                                                      cyAttributes)) {
                    setMergedAttribute(index,getDefaultMergedAttrName(mergedAttr,true));
            }
        }

        return old;
    }
    
    /*
     * remove original attribute 
     * 
     */
    @Override
    public String removeOriginalAttribute(final String netID, final String mergedAttributeName) {
        if (netID==null||mergedAttributeName==null) {
            throw new java.lang.NullPointerException("Null netID or mergedAttributeName");
        }
        
        final int index = attributeMerged.indexOf(mergedAttributeName);
        if (index==-1) {
            throw new java.lang.IllegalArgumentException("No "+mergedAttributeName+" is contained in merged attributes");
        }
        
        return removeOriginalAttribute(netID, index);
    }
    
    /*
     * remove original attribute 
     * 
     */
    @Override
    public String removeOriginalAttribute(final String netID, final int index) {
        if (netID==null) {
            throw new java.lang.NullPointerException("Null netID");
        }
        
        if (index<0 || index>=getSizeMergedAttributes()) {
            throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }
        
        final List<String> attrs = attributeMapping.get(netID);
        
        String old = attrs.set(index, nullAttr);
        pack(index);
        
        return old;
    }

    /*
     * remove merged attribute, along with the corresponding origianl attribute
     * 
     */
    @Override
    public String removeMergedAttribute(final String mergedAttributeName) {
        if (mergedAttributeName==null) {
            throw new java.lang.NullPointerException("Null mergedAttributeName");
        }
        
        final int index = attributeMerged.indexOf(mergedAttributeName);
        if (index ==-1 ) {
            return null;
        }
        
        return removeMergedAttribute(index);
    }
    
    /*
     * remove merged attribute, along with the corresponding origianl attribute
     * 
     */
    @Override
    public String removeMergedAttribute(final int index) {
        if (index<0 || index>=getSizeMergedAttributes()) {
            throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }
        
        String old = attributeMerged.remove(index);
        int n = attributeMapping.size();
        for (int i=0; i<n; i++) {
            attributeMapping.get(i).remove(index);
        }
        
        return old;
    }
    
    /*
     * Add new attribute in the end of the current network
     * 
     */
    @Override
    public String addAttributes(final Map<String,String> mapNetIDAttributeName, final String mergedAttrName) {
        return addAttributes(mapNetIDAttributeName,mergedAttrName,getSizeMergedAttributes());
    }
    
    /*
     * Add new attribute in the end of the current network
     * 
     */
    @Override
    public String addAttributes(final Map<String,String> mapNetIDAttributeName, final String mergedAttrName, final int index) {
        if (mapNetIDAttributeName==null || mergedAttrName==null) {
            throw new java.lang.NullPointerException();
        }
        
        if (index<0 || index>getSizeMergedAttributes()) {
            throw new java.lang.IndexOutOfBoundsException("Index out of bounds");
        }
        
        if (mapNetIDAttributeName.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Empty map");
        }
        
        final Set<String> networkSet = getNetworkSet();
        if (!networkSet.containsAll(mapNetIDAttributeName.keySet())) {
            throw new java.lang.IllegalArgumentException("Non-exist network(s)");
        }
        
        if (!Arrays.asList(cyAttributes.getAttributeNames()).containsAll(mapNetIDAttributeName.values())) {
            throw new java.lang.IllegalArgumentException("Non-exist attribute(s)");
        }
        
        final Iterator<Map.Entry<String,List<String>>> it = attributeMapping.entrySet().iterator();
        //final Iterator<Vector<String>> it = attributeMapping.values().iterator();
        while (it.hasNext()) { // add an empty attr for each network
            final Map.Entry<String,List<String>> entry = it.next();
            final String netID = entry.getKey();
            final List<String> attrs = entry.getValue();
            
            if (mapNetIDAttributeName.containsKey(netID)) {
                attrs.add(index,mapNetIDAttributeName.get(netID));
            } else {
                attrs.add(index,nullAttr);
            }
        }
        
        String defaultName = getDefaultMergedAttrName(mergedAttrName,false);
        attributeMerged.add(index,defaultName);// add in merged attr  
        return defaultName;
    }

    /*
     * 
     * 
     */
    @Override
    public void addNetwork(final String netID) {
        if (netID==null) {
            throw new java.lang.NullPointerException();
        }
        
        final String[] attributeNames = cyAttributes.getAttributeNames();
        Arrays.sort(attributeNames);

        final int nAttr = attributeNames.length;
        if (attributeMapping.isEmpty()) { // for the first network added
            
            final List<String> attrs = new Vector<String>();
            attributeMapping.put(netID, attrs);
                            

            for (int i=0; i<nAttr; i++) {
                //TODO REMOVE IN Cytoscape3.0
                if (attributeNames[i].compareTo(Semantics.CANONICAL_NAME)==0) {
                    continue;
                }//TODO REMOVE IN Cytoscape3.0
                
                addNewAttribute(netID, attributeNames[i]);
            }
            
            //TODO REMOVE IN 3.0, canonicalName in each network form a separate attribute in resulting network
            addNewAttribute(netID, Semantics.CANONICAL_NAME);//TODO REMOVE IN Cytoscape3.0
            

        } else { // for each attributes to be added, search if the same attribute exists
                 // if yes, add to that group; otherwise create a new one
            List<String> attrs = attributeMapping.get(netID);
            if (attrs!=null) { // this network already exist
                System.err.println("Error: this network already exist");
                return;
            }

            final int nr = attributeMerged.size(); // # of rows, the same as the # of attributes in merged network

            attrs = new Vector<String>(nr); // new map
            for (int i=0; i<nr; i++) {
                attrs.add(nullAttr);
            }
            attributeMapping.put(netID, attrs);

            for (int i=0; i<nAttr; i++) {
                final String at = attributeNames[i];
                 
                //TODO REMOVE IN Cytoscape3.0, canonicalName in each network form a separate attribute in resulting network
                if (at.compareTo(Semantics.CANONICAL_NAME)==0) {
                    addNewAttribute(netID, Semantics.CANONICAL_NAME);
                    continue;
                }//TODO REMOVE IN Cytoscape3.0
                 
                boolean found = false;             
                for (int ir=0; ir<nr; ir++) {
                    if (attrs.get(ir).compareTo(nullAttr)!=0) continue; // if the row is occupied
                    if (attributeMerged.get(ir).compareTo(at)==0) { // same name as the merged attribute
                        found = true;
                        attrs.set(ir, at);// add the attribute on the ir row
                        break; 
                    }

                    final Iterator<String> it = attributeMapping.keySet().iterator();
                    while (it.hasNext()) {
                        final String net_curr = it.next();
                        final String attr_curr = attributeMapping.get(net_curr).get(ir);
                        if (attr_curr.compareTo(at)==0) { // same name as the original attribute
                            //if (AttributeValueCastUtils.isAttributeTypeSame(attr_curr,at,attributes)) // not neccessay in Cytoscape2.6
                                                                                                       // since attributes are global
                            found = true;
                            attrs.set(ir, at); // add the attribute on the ir row
                            break; 
                        }
                    }

                    //if (found) break; // do not need to break, add to multiple line if match
                }

                if (!found) { //no same attribute found
                    addNewAttribute(netID,at);
                }                 
            }
        }
    }
    
    /*
     * get all network titles
     * 
     */
    @Override
    public Set<String> getNetworkSet() {
        return attributeMapping.keySet();
    }

    /*
     * get number of networks
     * 
     */
    @Override
    public int getSizeNetwork() {
        return attributeMapping.size();
    }   
    
    /*
     * 
     * 
     */
    @Override
    public void removeNetwork(final String netID) {
        if (netID==null) {
            throw new java.lang.NullPointerException();
        }
        final List<String> removed = attributeMapping.remove(netID);
        final int n = removed.size();
        for (int i=n-1; i>=0; i--) {
            if (removed.get(i).compareTo(nullAttr)!=0) { // if the attribute is not empty
                pack(i);
            }
        }
    }
    
    /* 
     * Remove empty rows from the current attribute mapping
     * 
     * @param attributeMapping the current attribute mapping
     * 
     */
    protected void pack(final int index) {
        if (index<0 || index>=getSizeMergedAttributes()) {
            throw new java.lang.IndexOutOfBoundsException("Index out of boundary.");
        }
        
        Iterator<List<String>> it = attributeMapping.values().iterator();
        while (it.hasNext()) {
            if (it.next().get(index).compareTo(nullAttr)!=0) {
                return;
            }
        }

        attributeMerged.remove(index);

        it = attributeMapping.values().iterator();
        while ( it.hasNext() ) {
            it.next().remove(index);
        }

//        if (attributeMapping.isEmpty()) {
//            attributeMerged.clear();
//        }

    }
    
    protected boolean attributeExistsInOriginalNetwork(final String attr) {
        if (attr==null) {
            throw new java.lang.NullPointerException();
        }
        return Arrays.asList(cyAttributes.getAttributeNames()).contains(attr);
    }
    
    private String getDefaultMergedAttrName(final String attr, boolean excludeOriginalAttribute) {
        if (attr==null) {
            throw new java.lang.NullPointerException();
        }
        
        String appendix = "";
        int i = 0;

        while (true) {
            String attr_ret = attr+appendix;
            if (attributeMerged.contains(attr_ret)||(excludeOriginalAttribute&&attributeExistsInOriginalNetwork(attr_ret))){
                appendix = "." + ++i;
            } else {
                return attr+appendix;
            } 
        }
    }
    
        
    protected void addNewAttribute(final String netID, final String attributeName) {
        if (netID==null || attributeName==null) {
            throw new java.lang.NullPointerException();
        }
        
        final Iterator<List<String>> it = attributeMapping.values().iterator();
        while (it.hasNext()) { // add an empty attr for each network
            it.next().add(nullAttr);
        }
        final List<String> attrs = attributeMapping.get(netID);
        attrs.set(attrs.size()-1, attributeName); // set attr
        
        String attrMerged = attributeName;
        //TODO remove in Cytosape3
        if (attributeName.compareTo(Semantics.CANONICAL_NAME)==0) {
            attrMerged = netID+"."+Semantics.CANONICAL_NAME;
        }//TODO remove in Cytosape3
        
        attributeMerged.add(getDefaultMergedAttrName(attrMerged,false)); // add in merged attr  
    }

}
