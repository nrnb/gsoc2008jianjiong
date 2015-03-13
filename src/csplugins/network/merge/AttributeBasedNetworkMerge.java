/* File: AttributeBasedNetworkMerge.java

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

package csplugins.network.merge;

import csplugins.network.merge.util.AttributeValueMatcher;
import csplugins.network.merge.util.DefaultAttributeValueMatcher;
import csplugins.network.merge.model.AttributeMapping;
import csplugins.network.merge.model.MatchingAttribute;
import csplugins.network.merge.util.AttributeMerger;
import csplugins.network.merge.util.AttributeValueCastUtils;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import cytoscape.Cytoscape;
import cytoscape.CyNetwork;
import cytoscape.data.CyAttributes;
import cytoscape.data.Semantics;
import cytoscape.data.attr.MultiHashMapDefinition;

import giny.model.Node;
import giny.model.Edge;
import giny.model.GraphObject;

/**
 * Attribute based Network merge
 * 
 * 
 */
public class AttributeBasedNetworkMerge extends AbstractNetworkMerge{
    protected final MatchingAttribute matchingAttribute;
    protected final AttributeMapping nodeAttributeMapping;
    protected final AttributeMapping edgeAttributeMapping;
    protected final AttributeValueMatcher attributeValueMatcher;
    protected final AttributeMerger attributeMerger;

    /**
     * Constucter for regular attribute based network merge
     * @param matchingAttribute
     * @param nodeAttributeMapping
     * @param edgeAttributeMapping
     */
    public AttributeBasedNetworkMerge(final MatchingAttribute matchingAttribute,
                               final AttributeMapping nodeAttributeMapping,
                               final AttributeMapping edgeAttributeMapping,
                               final AttributeMerger attributeMerger) {
            this(matchingAttribute,
                    nodeAttributeMapping,
                    edgeAttributeMapping,
                    attributeMerger,
                    new DefaultAttributeValueMatcher());
    }

    /**
     * Constucter for attribute based network merge with assigned comparator
     * @param matchingAttribute
     * @param nodeAttributeMapping
     * @param edgeAttributeMapping
     * @param attributeValueMatcher
     *          compare whether two attributes of nodes
     */
    public AttributeBasedNetworkMerge(final MatchingAttribute matchingAttribute,
                               final AttributeMapping nodeAttributeMapping,
                               final AttributeMapping edgeAttributeMapping,
                               final AttributeMerger attributeMerger,
                               AttributeValueMatcher attributeValueMatcher) {
        if (matchingAttribute==null
                || nodeAttributeMapping==null
                || edgeAttributeMapping==null
                || attributeMerger==null
                || attributeValueMatcher==null) {
                throw new java.lang.NullPointerException();
        }
        this.matchingAttribute = matchingAttribute;
        this.nodeAttributeMapping = nodeAttributeMapping;
        this.edgeAttributeMapping = edgeAttributeMapping;
        this.attributeMerger = attributeMerger;
        this.attributeValueMatcher = attributeValueMatcher;
    }
    
    
    /**
     * Check whether two nodes match
     *
     * @param net1,net2 two networks
     * @param n1,n2 two nodes belongs to net1 and net2 respectively
     * 
     * @return true if n1 and n2 matches
     */
    @Override
    public boolean matchNode(final CyNetwork net1, 
                             final Node n1, 
                             final CyNetwork net2, 
                             final Node n2) {
        if (net1==null || net2==null || n1==null || n2==null) {
            throw new java.lang.NullPointerException();
        }

        //TODO: should it match if n1==n2?
        if (n1==n2) {
                return true;
        }
        
        String attr1 = matchingAttribute.getAttributeForMatching(net1.getIdentifier());
        String attr2 = matchingAttribute.getAttributeForMatching(net2.getIdentifier());
        
        if (attr1==null || attr2==null) {
            throw new java.lang.IllegalArgumentException("No such network selected");
        }
                        
        //TODO: remove in cytoscape3
        if (attr1.compareTo("ID")==0) {
            attr1 = Semantics.CANONICAL_NAME;
        }
        if (attr1.compareTo("ID")==0) {
            attr1 = Semantics.CANONICAL_NAME;
        }//TODO: remove in cytoscape3
        
        final CyAttributes attributes = Cytoscape.getNodeAttributes();
        final String id1 = n1.getIdentifier();
        final String id2 = n2.getIdentifier();
        
        if (!attributes.hasAttribute(id1, attr1)
                ||!attributes.hasAttribute(id2, attr2)) { //ignore null attribute
            return false;
        }

        return attributeValueMatcher.matched(id1, attr1, id2, attr2, attributes);
    }
    
    /**
     * Merge (matched) nodes into one. This method will be refactored in Cytoscape3
     * 
     * @param mapNetNode 
     *              map of network to node, node in the network to be merged
     * 
     * @return merged Node
     */
    @Override
    public Node mergeNode(final Map<CyNetwork,Set<GraphObject>> mapNetNode) {
        //TODO: refactor in Cytoscape3, 
        // in 2.x node with the same identifier be the same node
        // and different nodes must have different identifier.
        // Is this true in 3.0?
        if (mapNetNode==null||mapNetNode.isEmpty()) {
            return null;
        }
        
        // Assign ID and canonicalName in resulting network   
        // remove in Cytoscape3
        final Iterator<Set<GraphObject>> itNodes = mapNetNode.values().iterator();
        Set<GraphObject> nodes = new HashSet<GraphObject>();        
        while (itNodes.hasNext()) {
            nodes.addAll(itNodes.next());
        }
        
        final Iterator<GraphObject> itNode = nodes.iterator();
        String id = new String(itNode.next().getIdentifier());
        
        if (nodes.size()>1) { // if more than 1 nodes to be merged, assign the id 
                              // as the combination of all identifiers
            while (itNode.hasNext()) {
                final Node node = (Node) itNode.next();
                id += "_"+node.getIdentifier();
            }

            // if node with this id exist, get new one
            String appendix = "";
            int app = 0;
            while (Cytoscape.getCyNode(id+appendix)!=null) {
                appendix = ""+ ++app; 
            }
            id += appendix;            
        }
        
        // Get the node with id or create a new node
        // for attribute confilict handling, introduce a conflict node here?
        final Node node = Cytoscape.getCyNode(id, true);
        
        // set other attributes as indicated in attributeMapping        
        setAttribute(id,mapNetNode,nodeAttributeMapping);

        return node;
    }
    
    /**
     * Merge (matched) nodes into one. This method will be refactored in Cytoscape3
     * 
     * @param mapNetEdge 
     *              map from network to Edge, Edge in the network to be merged
     * @param source, target
     *              source and target nodes in the merge network
     * 
     * @return merged Node
     */
    @Override
    public Edge mergeEdge(final Map<CyNetwork,Set<GraphObject>> mapNetEdge,
                          final Node source, 
                          final Node target,
                          final String interaction, 
                          final boolean directed) {
        //TODO: refactor in Cytoscape3
        if (mapNetEdge==null||mapNetEdge.isEmpty()||source==null||target==null) {
            return null;
        }
        
        // Get the edge or create a new one
        // attribute confilict handling?
        final Edge edge = Cytoscape.getCyEdge(source, target, 
                Semantics.INTERACTION, interaction, true, directed); // ID and canonicalName set when created
        final String id = edge.getIdentifier();
        
        // set other attributes as indicated in attributeMapping
        setAttribute(id,mapNetEdge,edgeAttributeMapping);

        return edge;
    }
    
    /*
     * set attribute for the merge node/edge according to attribute mapping
     * 
     */
    protected void setAttribute(final String id, 
                                final Map<CyNetwork,Set<GraphObject>> mapNetGOs,
                                final AttributeMapping attributeMapping) {
        if (id==null || mapNetGOs==null || attributeMapping==null) {
            throw new java.lang.NullPointerException();
        }
        
        CyAttributes cyAttributes = attributeMapping.getCyAttributes();
        
        final int nattr = attributeMapping.getSizeMergedAttributes();
        for (int i=0; i<nattr; i++) {
            final String attr_merged = attributeMapping.getMergedAttribute(i);
            if (attr_merged==null||attr_merged.length()==0) {
                throw new java.lang.IllegalStateException("Null or empty name for the merged attribute");
            }
            
            // define the attribute first
            final Set<String> attrNames = new HashSet(attributeMapping.getOriginalAttributeMap(i).values());
            final String attr_mc = AttributeValueCastUtils.getMostCompatibleAttribute(attrNames, cyAttributes);
            
            if (attr_mc!=null) { // if compatible type 
                if (!AttributeValueCastUtils.isAttributeTypeConvertable(attr_mc, attr_merged, cyAttributes)) {
                    throw new java.lang.IllegalStateException("'"+attr_mc+"' cannot be converted to '"+attr_merged+"'");
                }
                
                // if attr_merged is a new attribute, define it first
                if (!Arrays.asList(cyAttributes.getAttributeNames()).contains(attr_merged)) {
                    final MultiHashMapDefinition mmapDef = cyAttributes.getMultiHashMapDefinition();
                    mmapDef.defineAttribute(attr_merged,
                                            mmapDef.getAttributeValueType(attr_mc),
                                            mmapDef.getAttributeKeyspaceDimensionTypes(attr_mc));
                    //TODO: collecte new attribute here
                    // if exception occur or user choose to cancel, undefine
                }
            } else { // if incompatible type                
                if (!Arrays.asList(cyAttributes.getAttributeNames()).contains(attr_merged)) {
                    final MultiHashMapDefinition mmapDef = cyAttributes.getMultiHashMapDefinition();
                    mmapDef.defineAttribute(attr_merged,
                                            MultiHashMapDefinition.TYPE_STRING,
                                            null);
                    //TODO: collecte new attribute here
                    // if exception occur or user choose to cancel, undefine
                } else {
                    if (cyAttributes.getType(attr_merged)==CyAttributes.TYPE_STRING) {
                        throw new java.lang.IllegalStateException("Incompatible type can only be converted into String");
                    }
                }
            }

            // merge
            Map<String,String> mapGOAttr = new HashMap<String,String>();
            final Iterator<Map.Entry<CyNetwork,Set<GraphObject>>> itEntryNetGOs = mapNetGOs.entrySet().iterator();
            while (itEntryNetGOs.hasNext()) {
                    final Map.Entry<CyNetwork,Set<GraphObject>> entryNetGOs = itEntryNetGOs.next();
                    final String idNet = entryNetGOs.getKey().getIdentifier();
                    final String attrName = attributeMapping.getOriginalAttribute(idNet, i);
                    if (attrName!=null) {
                            final Iterator<GraphObject> itGO = entryNetGOs.getValue().iterator();
                            while (itGO.hasNext()) {
                                    final String idGO = itGO.next().getIdentifier();
                                    mapGOAttr.put(idGO, attrName);
                            }
                    }
            }

            attributeMerger.mergeAttribute(mapGOAttr, id, attr_merged, cyAttributes);
                
//            // for each attribute to be merged
//            while (itEntryNetGOs.hasNext()) {
//                final Map.Entry<CyNetwork,Set<GraphObject>> entryNetGOs = itEntryNetGOs.next();
//                final String idNet = entryNetGOs.getKey().getIdentifier();
//                final String attrName = attributeMapping.getOriginalAttribute(idNet, i);
//                if (attrName!=null) {
//                    Set<String> idsFrom = new HashSet<String>();
//                    final Iterator<GraphObject> itGO = entryNetGOs.getValue().iterator();
//                    while (itGO.hasNext()) {
//                        final String idGO = itGO.next().getIdentifier();
//                        idsFrom.add(idGO);
////                        AttributeValueCastUtils.copyAttribute(idGO,
////                                                             attrName,
////                                                             id,
////                                                             attr_merged,
////                                                             cyAttributes,
////                                                             conflictCollector);
//                    }
//
//                    attributeMerger.mergeAttribute(idsFrom, attrName, id, attr_merged, cyAttributes);
//                }
//            }
        }
    }
        
    
}
