package ejik.util.xml;

import java.util.Vector;

import ejik.types.ObjectList;


public class XMLObject{
	
	public class Pair {
		public Pair(String name, Object value) {
			this.name = name;
			this.value = value;
		}
		public String name = "";
		public Object value = "";
	}
	
	private Vector<Pair> properties =  new Vector<Pair>();
	private String type = "";
	private String uid = "";
	
	public XMLObject(String type) {
		this.type = type;
	}
	
	public XMLObject(String type, String uid) {
		this.type = type;
		this.uid = uid;
	}

	public void set(String name, Object value) {
		properties.add(new Pair(name, value));
	}

	public String getType() {
		return this.type;
	}

	public String getUid() {
		return this.uid;
	}

	public ObjectList get(String name) {
		ObjectList res = new ObjectList();
		for (Pair pair: properties) {
			if (pair != null && pair.name.equalsIgnoreCase(name)) {
				res.add(pair.value);
			}
		}
		return res;
	}
	
	public ObjectList getObjects() {
		ObjectList res = new ObjectList();
		for (Pair pair: properties) {
			if (pair != null && pair.value instanceof XMLObject) {
				res.add(pair.value);
			}
		}
		return res;
	}

	public Object getOne(String name) {
		for (Pair pair: properties) {
			if (pair != null && pair.name.equalsIgnoreCase(name)) {
				return pair.value;
			}
		}
		return null;
	}
	
	public String getString(String name) throws Exception {
		Boolean found = false;
		for (Pair pair: properties) {
			if (pair != null && pair.name.equalsIgnoreCase(name)) {
				found = true;
				return (String)pair.value;
			}
		}
		if (!found) {
			throw new Exception("No \""+name+"\" in \""+this.type+"\" of type String");
		}
		return null;
	}
	
	public XMLObject getXMLObject(String name) throws Exception {
		Boolean found = false;
		for (Pair pair: properties) {
			if (pair != null && pair.name.equalsIgnoreCase(name) && pair.value instanceof XMLObject) {
				found = true;
				return (XMLObject)pair.value;
			}
		}
		if (!found) {
			throw new Exception("No "+name+" in "+this.type+" of type XMLObject");
		}
		return null;
	}

}
