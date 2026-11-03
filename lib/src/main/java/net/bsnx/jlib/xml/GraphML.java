package net.bsnx.jlib.xml;

import net.bsnx.jlib.collections.UniqueList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Repraesentiert ein GraphML Dokument
 * 
 * @link http://de.wikipedia.org/wiki/GraphML
 * 
 * @author Bjoern R. Salgert
 * @date 28.05.2011
 */
public class GraphML {

	public static void main(String[] args) {
		GraphML g = new GraphML();
		g.addNode("a");
		g.addNode("b");
		g.addNode("c");
		g.connect("a", "b");
		g.connect("a", "c");
		System.out.println(g);
	}
	
	/**
	 * Support f�r Yed
	 * 
	 * @link http://www.yworks.com/de/products_yed_about.html
	 */
	public static boolean YED = true;
	
	public static final String XMLNS = "http://graphml.graphdrawing.org/xmlns";
	
	public static final String ID = "id";
	public static final String SOURCE = "source";
	public static final String TARGET = "target";
	public static final String KEY = "key";
	
	private List<String> ids = new UniqueList<String>();
	
	private Document doc;
	
	private Element root;
	
	private Element graph;
	
	public GraphML() {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			root = doc.createElement("graphml");
			doc.appendChild(root);
			root.setAttribute("xmlns", XMLNS);
			graph = doc.createElement("graph");
			graph.setAttribute(ID, "G");
			root.appendChild(graph);
			if (YED) {
				// xmlns:y="http://www.yworks.com/xml/graphml"
				root.setAttribute("xmlns:y", "http://www.yworks.com/xml/graphml");
				
				//<key for="graphml" id="d0" yfiles.type="resources"/>
				Element key = XML.insert(doc, root, "key");
				key.setAttribute("for", "graphml");
				key.setAttribute("id", "d0");
				key.setAttribute("yfiles.type", "resources");
				
				//<key attr.name="url" attr.type="string" for="node" id="d1"/>
				key = XML.insert(doc, root, "key");
				key.setAttribute("attr.name", "url");
				key.setAttribute("attr.type", "string");
				key.setAttribute("for", "node");
				key.setAttribute("id", "d1");
				
				//<key attr.name="description" attr.type="string" for="node" id="d2"/>
				key = XML.insert(doc, root, "key");
				key.setAttribute("attr.name", "description");
				key.setAttribute("attr.type", "string");
				key.setAttribute("for", "node");
				key.setAttribute("id", "d2");
				
				//<key for="node" id="d3" yfiles.type="nodegraphics"/>
				key = XML.insert(doc, root, "key");
				key.setAttribute("yfiles.type", "nodegraphics");
				key.setAttribute("for", "node");
				key.setAttribute("id", "d3");
				
				//<key attr.name="url" attr.type="string" for="edge" id="d4"/>
				key = XML.insert(doc, root, "key");
				key.setAttribute("attr.name", "url");
				key.setAttribute("attr.type", "string");
				key.setAttribute("for", "edge");
				key.setAttribute("id", "d4"); 
						
				//<key attr.name="description" attr.type="string" for="edge" id="d5"/>
				key = XML.insert(doc, root, "key");
				key.setAttribute("attr.name", "description");
				key.setAttribute("attr.type", "string");
				key.setAttribute("for", "edge");
				key.setAttribute("id", "d5"); 
				
				//<key for="edge" id="d6" yfiles.type="edgegraphics"/>
				key = XML.insert(doc, root, "key");
				key.setAttribute("yfiles.type", "edgegraphics");
				key.setAttribute("for", "edge");
				key.setAttribute("id", "d6");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean know(String id) {
		return ids.contains(id);
	}
	
	public GraphML addNode(String id) {
		Element node = root = doc.createElement("node");
		ids.add(id);
		node.setAttribute(ID, id);
		graph.appendChild(node);
		if (YED) {
			// <data key="d2"/>
		    //  <data key="d3">
		    //    <y:ShapeNode>
		    //      <y:Geometry height="30.0" width="30.0" x="91.0" y="344.0"/>
		    //      <y:Fill color="#FFCC00" transparent="false"/>
		    //      <y:BorderStyle color="#000000" type="line" width="1.0"/>
		    //      <y:NodeLabel alignment="center" autoSizePolicy="content" fontFamily="Dialog" fontSize="12" fontStyle="plain" hasBackgroundColor="false" hasLineColor="false" height="18.701171875" modelName="internal" modelPosition="c" textColor="#000000" visible="true" width="10.673828125" x="9.6630859375" y="5.6494140625">a</y:NodeLabel>
		    //      <y:Shape type="rectangle"/>
		    //    </y:ShapeNode>
		    //  </data>
			Element data = XML.insert(doc, node, "data");
			data.setAttribute(KEY, "d2");
			data = XML.insert(doc, root, "data");
			data.setAttribute(KEY, "d3");
			Element shapeNode = XML.insert(doc, data, "y:ShapeNode");
			Element geo = XML.insert(doc, shapeNode, "y:Geometry");
			geo.setAttribute("height", "30.0");
			geo.setAttribute("width", "150.0");
			geo.setAttribute("x", "90.0");
			geo.setAttribute("y", "90.0");
			Element fill = XML.insert(doc, shapeNode, "y:Fill");
			fill.setAttribute("color", "#FFCC00");
			fill.setAttribute("transparent", "false");
			Element border = XML.insert(doc, shapeNode, "y:BorderStyle");
			border.setAttribute("color", "#000000");
			border.setAttribute("type", "line");
			border.setAttribute("width", "1.0");
			Element nodeLabel = XML.insert(doc, shapeNode, "y:NodeLabel");
			nodeLabel.setAttribute("alignment", "center");
			nodeLabel.setAttribute("autoSizePolicy", "content");
			nodeLabel.setAttribute("fontFamily", "Dialog");
			nodeLabel.setAttribute("fontSize", "12");
			nodeLabel.setAttribute("fontStyle", "plain");
			nodeLabel.setAttribute("hasBackgroundColor", "false");
			nodeLabel.setAttribute("hasLineColor", "false");
			nodeLabel.setAttribute("height", "18.701171875");
			nodeLabel.setAttribute("modelName", "internal");
			nodeLabel.setAttribute("modelPosition", "c");
			nodeLabel.setAttribute("textColor", "#000000");
			nodeLabel.setAttribute("visible", "true");
			nodeLabel.setAttribute("width", "10.673828125");
			nodeLabel.setAttribute("x", "9.6630859375");
			nodeLabel.setAttribute("y", "5.6494140625");
			nodeLabel.setTextContent(id); // <---
			Element shape = XML.insert(doc, shapeNode, "y:Shape");
			shape.setAttribute("type", "rectangle");
		}
		return this;
	}
	
	public GraphML connect(String a_id, String b_id) {
		Element node = root = doc.createElement("edge");
		node.setAttribute(ID, "connection_" + a_id + "_to_" + b_id);
		node.setAttribute(SOURCE, a_id);
		node.setAttribute(TARGET, b_id);
		graph.appendChild(node);
		return this;
	}
	
	@Override
	public String toString() {
		return XML.toString(doc);
	}
	
}
