package org.sii;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;

import com.google.gson.Gson;

public class RetrieveFollowers {
	/* Metodo che dato l'id dell'utente, ritorna una stringa con i suoi followers*/
	public String retrieveFollowers(String id,Instagram instagram) throws InstagramException {
		Node node = nodeBuilder().node();
		Client client = node.client();

		//List<String> followers= new ArrayList<String>();
		String followers ="";
		UserFeed uFeed =instagram.getUserFollowList(id);
		List<UserFeedData> udata = uFeed.getUserList();

		for(UserFeedData d: udata){

			followers =followers +","+d.getId();
		}

		UserFollowers usf = new UserFollowers(id,followers);
		IndexResponse resp = client.prepareIndex("listafollowers1", "followers",id)
				.setSource(toJson(usf))
				.execute()
				.actionGet();

		node.close();
		return followers;
	}

/* Metodo che ritrova gli utenti dalla lista dei followers e li cancella*/
	public void retrieve4Delete(String id, Instagram instagram) throws InstagramException{
		IncreaseFollowers increase = new IncreaseFollowers();
		ESutilities esu = new ESutilities();
		UserFeed uFeed =instagram.getUserFollowList(id);
		List<UserFeedData> udata = uFeed.getUserList();
		List<String> followers = new ArrayList<String>();
		for(UserFeedData d : udata){
			followers.add(d.getId());

		}

		for (String s: followers){
			esu.deleteUser(s,"prova01");
			System.out.println("rimosso");
		}


	}
	/*metodo per estrarre i tag dati gli id degli utenti*/
	public void TagsExtractor(Instagram instagram, List<String> idUtenti) throws InstagramException{
		int contatore =4900;
		while(contatore>0 && idUtenti!=null){
			String userId =idUtenti.get(0);
			idUtenti.remove(0);
			MediaFeed media = instagram.getRecentMediaFeed(userId, 33, null, null, null, null);
			List<MediaFeedData> elements = media.getData();
			UtenteTags uTags = new UtenteTags();
			List<String> tagsUtente = new ArrayList<String>();
			for (MediaFeedData mediaFeedData : elements){
				contatore--;
				List<String > tags = mediaFeedData.getTags();
				for(String s: tags)
					tagsUtente.add(s);


			}
			uTags.setTags(tagsUtente);


		}

	}



	public RetrieveFollowers() {
		Node node = nodeBuilder().node();
		Client client = node.client();
	}





	public String toJson(Object obj){

		Gson gson = new Gson();

		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(obj);
		System.out.println(json);
		return json;
	}

}
