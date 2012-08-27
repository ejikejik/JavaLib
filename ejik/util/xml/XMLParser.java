package ejik.util.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XMLParser extends DefaultHandler {

	
	private XMLObject root = null;
	private XMLObject current = null;
	private Stack<XMLObject> stack = new Stack<XMLObject>();
	
	/*public boolean validate(String xml) throws SAXException, IOException {
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = factory.newSchema();
		Validator validator = schema.newValidator();
		validator.validate((Source)(new InputSource(new StringReader(xml))));
		return true;
	}*/
	
	public XMLObject parse(String xml) throws SAXException, ParserConfigurationException, IOException {
		//this.validate(xml);
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		parser.parse(new InputSource(new StringReader(xml)), this);
		return root;
	}
	
	@Override
	public void startElement(String uri, String name, String qname, Attributes attributes) {
		if (qname.equalsIgnoreCase("message")) {
			String uid = attributes.getValue("uid");
			this.root = new XMLObject("message", uid);
			this.current = this.root;
		}
		if (qname.equalsIgnoreCase("object")) {
			String type = attributes.getValue("type");
			XMLObject o = new XMLObject(type);
			this.stack.push(current);
			this.current.set(type, o);
			this.current = o;
		}
		if (qname.equalsIgnoreCase("property")) {
			if (this.current != null) {
				this.current.set(attributes.getValue("name"), attributes.getValue("value"));
			}
		}
	}
	
	@Override
	public void endElement(String uri, String name, String qname) {
		if (qname.equalsIgnoreCase("object") || qname.equalsIgnoreCase("message")) {
			if (this.current != this.root) {
				this.current = this.stack.pop();
			}
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
	}
	
}
