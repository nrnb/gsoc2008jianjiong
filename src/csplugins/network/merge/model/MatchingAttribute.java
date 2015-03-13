/* File: MatchingAttribute.java

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


import java.util.Set;
import java.util.Map;

/**
 * Information which attribute to be used for matching nodes
 * 
 * 
 */
public interface MatchingAttribute {
    
    /*
     * Get map of network to attribute
     * 
     */
    public Map<String,String> getNetAttrMap();

    /*
     * Get the attribute of network for matching node
     * 
     */
    public String getAttributeForMatching(String netID);
    
    /*
     * Set the attribute of network for matching node
     * 
     */
    public void putAttributeForMatching(String netID, String attributeName);
    
    /*
     * add/select the attribute of network for matching node
     * 
     */
    public void addNetwork(String netID);
    
    /*
     * Remove the network, return the attribute
     * 
     */
    public String removeNetwork(String netID);
    
    /*
     * 
     * 
     */
    public int getSizeNetwork();
    
    /*
     * 
     * 
     */
    public Set<String> getNetworkSet();
            
    //TODO: ID types of the attribute could be store here
}
