package org.sii;
/*La classe UserFollowers crea un oggetto UserFollowers con una stringa id e una stringa id che contiene gli id dei followers*/
public class UserFollowers {
	String id;
	String followers;
	
public UserFollowers(){}



public UserFollowers(String id, String followers) {
	super();
	this.id = id;
	this.followers = followers;
}




public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getFollowers() {
	return followers;
}

public void setFollowers(String followers) {
	this.followers = followers;
}


}
