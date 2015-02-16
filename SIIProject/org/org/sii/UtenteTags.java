package org.sii;

import java.util.List;
/*La classe UtenteTags crea oggetti Utenti tags con una stringa id e una lista di tag associati,gli ultimi tag aggiunti da un utente*/
public class UtenteTags {
	String id;
	List<String> tags;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public UtenteTags(String id, List<String> tags) {
		super();
		this.id = id;
		this.tags = tags;
	}
	
	
	public UtenteTags(){}

}
