package org.sii;

import java.io.IOException;


import java.util.ArrayList;


import java.lang.Math;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpUtils;

import org.jinstagram.entity.common.Pagination;
import org.jinstagram.entity.common.User;
import org.jinstagram.entity.likes.LikesFeed;
import org.jinstagram.entity.relationships.RelationshipData;
import org.jinstagram.entity.relationships.RelationshipFeed;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.basicinfo.Counts;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.jinstagram.http.Response;
import org.jinstagram.http.Verbs;
import org.jinstagram.model.Methods;
import org.jinstagram.model.QueryParam;
import org.jinstagram.model.Relationship;
import org.jinstagram.Instagram;


import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.search.MultiMatchQuery.QueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.Node.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryBuilders.*;

import com.google.gson.Gson;

//import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.*;



public class IncreaseFollowers {

	public IncreaseFollowers() {
		super();
		// TODO Auto-generated constructor stub
	}
	List<String> userReached = new ArrayList<String>();
	Map<String,Integer> userIteration = new HashMap<String,Integer>();

	/* Metodo per selezionare gli utenti con rate (followers/following) <=1 e numero di followers <600*/
	public boolean analyze(int followers, int following){
		boolean analyze = false;
		if ( following!=0 && followers!=0 && followers<600 &&(followers/following)<=1 )
			analyze = true;
		return analyze;
	}
	/* Metodo per selezionare gli utenti con rate (followers/following) <=1 e numero di followers >600*/
	public boolean analyzeReversed(int followers, int following){
		boolean analyze = false;
		if ( following!=0 && followers!=0 && followers>600 &&(followers/following)<=1 )
			analyze = true;
		return analyze;
	}

	/* metodo di supporto che ritorna il numero di followers dell'utente, dato il suo id*/
	public int followers(String id, Instagram instagram) throws InstagramException{
		UserInfo userInfo = instagram.getUserInfo(id);
		UserInfoData userInfoData = userInfo.getData();
		Counts userCounts = userInfoData.getCounts();
		int followers = userCounts.getFollwed_by();
		return followers;
	}
	/* metodo di supporto che ritorna il numero di followings dell'utente, dato il suo id*/

	public int following(String id, Instagram instagram) throws InstagramException{
		UserInfo userInfo = instagram.getUserInfo(id);
		UserInfoData userInfoData = userInfo.getData();
		Counts userCounts = userInfoData.getCounts();
		int following = userCounts.getFollows();
		return following;
	}
	/* metodo che calcola quanti media ha pubblicato l'utente*/
	public int userCounts(String id,Instagram instagram) {
		UserInfo userInfo;
		int media =0;
		try {
			userInfo = instagram.getUserInfo(id);

			UserInfoData userInfoData = userInfo.getData();

			Counts userCounts = userInfoData.getCounts();

			media =userCounts.getMedia();

		} catch (InstagramException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return media;
	}
	/* tag da ricercare per l'approccio con db*/
	public boolean isTags(MediaFeedData mediaFeedData, Instagram instagram)throws InstagramException{
		boolean isTag = false;
		for (String s : mediaFeedData.getTags()){
			if (s.equals("foodporn")||s.equals("food")||s.equals("yummi") ||s.equals("instafood")||s.equals("cooking"))
				isTag=true;}

		return isTag;

	}
	/* Metodo per inviare i likes(in numero uguale a quello del contatore, a utenti ricercati tramite un tag*/
	public  void increaseFollowersLike(String tagName, long cont, Instagram instagram) throws InstagramException {
		TagMediaFeed tagMediaFeed = instagram.getRecentMediaTags(tagName, cont);
		List<MediaFeedData> data = tagMediaFeed.getData();
		Pagination pagination;
		int contatore = 30;
		int contaTag =0;
		while(contatore >1){
			if(contaTag>=32){
				pagination = tagMediaFeed.getPagination();
				tagMediaFeed =instagram.getTagMediaInfoNextPage(pagination);
				data= tagMediaFeed.getData();
				System.out.println("next lap!!");
			}
			for (MediaFeedData m : data){
				contaTag++;
				User u = m.getUser();
				String userId = u.getId();
				if (analyze(followers(userId,instagram), following(userId,instagram)) && contatore >0){
					if (userCounts(userId,instagram)>= 3){
						contatore --;
						MediaFeed media = instagram.getRecentMediaFeed(userId, 3, null, null, null, null);
						userReached.add(userId);
						List<MediaFeedData> elements = media.getData();
						for (MediaFeedData mediaFeedData : elements){
							if (!mediaFeedData.isUserHasLiked()){
								String idData = mediaFeedData.getId();
								LikesFeed like = instagram.setUserLike(idData); //mette il like all'utente
								System.out.println("like");
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}

				}
			}
		}
		//return userReached;
	} //end IncreaseFollowersLike
	/*Metodo per incrementare il numero di followers inviando un follow agli utenti scelti in base a uno specifico tag*/
	public  void increaseFollowersFollow(String tagName, long cont, Instagram instagram) throws InstagramException {
		TagMediaFeed tagMediaFeed = instagram.getRecentMediaTags(tagName, cont);
		List<MediaFeedData> data = tagMediaFeed.getData();
		int contatore = 58;
		int contaTag =0;
		Relationship relationship;
		relationship=Relationship.FOLLOW;
		Pagination pagination;
		while(contatore >1){
			if(contaTag>=32){
				pagination = tagMediaFeed.getPagination();
				tagMediaFeed =instagram.getTagMediaInfoNextPage(pagination);
				data= tagMediaFeed.getData();
				System.out.println("prossimo giro!!");

			}
			for (MediaFeedData m : data){
				contaTag++;
				System.out.println("****ContaTag****"+ contaTag);
				User u = m.getUser();
				String userId = u.getId();
				if (!userReached.contains(userId)){
					if (analyze(followers(userId,instagram), following(userId,instagram)) && contatore>1){
						userReached.add(userId);
						System.out.println("contatore"+ contatore);
						contatore--;
						instagram.setUserRelationship(userId,relationship);

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


					}	//end if analyze
				} //end if lista

			} }//end for
	} //end IncreaseFollowersFollow

	/*Metodo per aumentare il numero di followers, prendendo gli elementi salvati all'interno del database(utenti che hanno pubblicato
	 *  foto con i tags di isTags recentemente : per database relazionali*/
	public void increaseFollowerByDB(Instagram instagram) throws Exception{
		DBManager c = new DBManager();
		Relationship relationship;
		relationship=Relationship.FOLLOW; //crea Relationship
		Map<String,Integer> map = c.userExtractor(); // questo metodo estrae gli utenti dal DB con numero di foto di isTags >n
		c.sortMap(map); //ordino la mappa dei risultati
		int contatore =58;
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()&& contatore >0) {
			// Utilizza il nuovo elemento (coppia chiave-valore)
			// dell'hashmap
			Map.Entry entry = (Map.Entry)it.next();
			String userId = (String) entry.getKey();
			instagram.setUserRelationship(userId,relationship);
			System.out.println("followato");
			c.deleteUser(userId);
		}
	}
	/* cerca gli utenti tramite un tag e li memorizza nel DB relazionale*/
	public  void searchFollowersByNumber(String tagName, long cont, Instagram instagram) throws Exception {
		TagMediaFeed tagMediaFeed = instagram.getRecentMediaTags(tagName, cont);
		DBManager createConnection = new DBManager();
		List<MediaFeedData> data = tagMediaFeed.getData();
		int contatore = 4990;//il limite delle api è di 5000 richieste all'ora
		int contaTag =0;
		Pagination pagination;
		while(contatore >1){
			System.out.println("contatore+"+contatore);
			if(contaTag>=32){
				pagination = tagMediaFeed.getPagination();
				tagMediaFeed =instagram.getTagMediaInfoNextPage(pagination);
				data= tagMediaFeed.getData();
				System.out.println("prossimo giro!!");

			}
			for (MediaFeedData m : data){
				contaTag++;
				String userId = m.getUser().getId();
				if (analyzeReversed(followers(userId,instagram), following(userId,instagram))&& contatore>1){
					if (userCounts(userId,instagram) >= 50){ //deve avere almeno 50 foto?
						System.out.println("preso");
						int number=0;
						MediaFeed media = instagram.getRecentMediaFeed(userId, 50, null, null, null, null);
						List<MediaFeedData> elements = media.getData();
						for (MediaFeedData mediaFeedData : elements){
							contatore--;
							if(isTags(mediaFeedData,instagram))//se c'è il tag foodporn food etc
								number++;
						}
						//inserisci utente nel DB con il numero di foto in cui compare il tag
						createConnection.insertUser(userId,number);
						createConnection.insertUserTab2(userId, number);
						System.out.println("inserito");
					}
				}	//end if analyze


			} //end for
			if (contatore <=2){
				System.out.println("inizio stop");
				Thread.sleep(3600000);
				System.out.println("fine stop");
				contatore =4990;
			}
		}//end while
	} //end metodo



	//Create Client
	public Client createClient(){

		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "localtestsearch").build();
		TransportClient transportClient = new TransportClient(settings);
		transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9200));
		return (Client) transportClient;

	}


/*MetodoPer ricercare Utenti in base al tag*/
	public void tagSearch(Instagram instagram,long cont,String index,String tagName) throws InstagramException{
		Node node = nodeBuilder().node();
		Client client = node.client();
		TagMediaFeed tagMediaFeed = instagram.getRecentMediaTags(tagName, cont);
		List<MediaFeedData> data = tagMediaFeed.getData();
		int contatore = 4990;//il limite delle api è di 5000 richieste all'ora
		int contaTag =0;
		Pagination pagination;

		while(contatore >0){
			System.out.println("contatore+"+contatore);
			if(contaTag>=32){
				pagination = tagMediaFeed.getPagination();
				tagMediaFeed =instagram.getTagMediaInfoNextPage(pagination);
				data= tagMediaFeed.getData();
				System.out.println("prossimo giro!!");

			}
			for (MediaFeedData m : data){
				contaTag++;
				String userId = m.getUser().getId();
				if (analyzeReversed(followers(userId,instagram), following(userId,instagram))&& contatore>1){
					if (userCounts(userId,instagram) >= 50){ //deve avere almeno 50 foto?
						System.out.println("preso");
						UserInfo infoUser =instagram.getUserInfo(userId);

						int number=0;
						MediaFeed media = instagram.getRecentMediaFeed(userId, 50, null, null, null, null);
						List<MediaFeedData> elements = media.getData();
						for (MediaFeedData mediaFeedData : elements){
							contatore--;
							if(isTags(mediaFeedData,instagram))//se c'è il tag foodporn food etc
								number++;
						}
						UserWithNumbers user= new UserWithNumbers(infoUser,number);
						
						IndexResponse resp = client.prepareIndex(index, "utenti",userId)
								.setSource(toJson(user))
								.execute()
								.actionGet();
						System.out.println("inserito");
					}
				}	//end if analyze


			} //end for
			if (contatore <=2){
				System.out.println("inizio stop");
				try {
					Thread.sleep(3600000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("fine stop");
				contatore =4990;
			}
			//end while
		} 
		

node.close();
}

	/*Metodo che dati una latitudine e una longitudine, 
	 * trova le coordinate di punti a lui prossimi, iterativamente,
	 * e li mappa in un quadrato*/
	public List<Punto> MakeQuadrato(double latitudine,double longitudine){
		List<Punto> punti = new ArrayList<Punto>();
		double delta =0.01;
		while (delta <0.2){
			Punto p1 = new Punto(latitudine,longitudine+delta);
			Punto p2 = new Punto(latitudine+delta,longitudine);
			Punto p3 = new Punto(latitudine+delta,longitudine+delta);
			Punto p6 = new Punto(latitudine+delta,longitudine-delta);
			Punto p4= new Punto(latitudine-delta,longitudine);
			Punto p5= new Punto(latitudine-delta,longitudine-delta);
			Punto p8= new Punto(latitudine-delta,longitudine+delta);
			Punto p7 = new Punto(latitudine,longitudine-delta);
			punti.add(p1);punti.add(p2);	punti.add(p3); punti.add(p4);	
			punti.add(p5); punti.add(p6); punti.add(p7); punti.add(p8);	
			delta =delta+0.01;
		}

		return punti;
	}


	/*Metodo per ricercare gli utenti,tramite longitudine e latitudine, dati i centri(punti d'interesse ricercati*/
	public void squareSearch(Instagram instagram,List<Punto> centri) throws InstagramException{
		Node node = nodeBuilder().node();
		Client client = node.client();
		int contatore = 3000;
		int i =6;
		while(contatore >0 && i>0){
			int index = (int) (Math.random()*i);
			Punto select = centri.get(index);
			centri.remove(index);
			i--;
			List<Punto> punti = MakeQuadrato(select.daiCoordinataY(),select.daiCoordinataX());
			if(contatore >0){
				for (Punto p: punti){
					MediaFeed mediaf = instagram.searchMedia(p.daiCoordinataY(),p.daiCoordinataX());
					p.stampaPunto();
					List<MediaFeedData> data = mediaf.getData();
					if (contatore>0){
						for (MediaFeedData m : data){
							String userId = m.getUser().getId();
							UserInfo u = instagram.getUserInfo(userId);
							UserInfoData uData = u.getData();
							contatore--;
							IndexResponse resp = client.prepareIndex("instagraMondo", "utenti",userId)
									.setSource(toJson(uData))
									.execute()
									.actionGet();
						
						}}}

			}
		}
		node.close();
	}


	/*Metodo per trasformare un oggetto in json*/
	public String toJson(Object obj){
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		System.out.println(json);
		return json;
	}


	public void search(String userId,Instagram instagram, String index, String type) throws InstagramException{
		Node node = nodeBuilder().node();
		Client client = node.client();
		UserInfo u = instagram.getUserInfo(userId);
		UserInfoData uData = u.getData();
		IndexResponse resp = client.prepareIndex(index, type,userId)
				.setSource(toJson(uData))
				.execute()
				.actionGet();

	}

	/*Metodo che estrae gli utenti dal db di ES e mette un follow*/
	public  void putFollow(	List<String> idLista, long cont, Instagram instagram,ESutilities esu,String index){
		int utenteCorrente =0;
		int contatore =58;
		Relationship relationship;
		relationship=Relationship.FOLLOW;
		while (contatore >0){
			String userId = idLista.get(utenteCorrente);
			contatore --;
			utenteCorrente++;
			try {
				instagram.setUserRelationship(userId,relationship);
			} catch (InstagramException e1) {			
				e1.printStackTrace();
				contatore =0;
			}
			esu.deleteUser(userId,index);	
			System.out.println("follow");
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	/* Metodo che raccoglie utendi dal DB di elasticsearch e mette 3 likes */
	public  void putLikes(	List<String> idLista, long cont, Instagram instagram, ESutilities esu,String index)  {
		int utenteCorrente =0;
		int contatore = 97;
		while(contatore >0){
			String userId = idLista.get(utenteCorrente);
			try {
				if (userCounts(userId,instagram)>= 3){
					MediaFeed media;
					media = instagram.getRecentMediaFeed(userId, 3, null, null, null, null);

					List<MediaFeedData> elements = media.getData();
					for (MediaFeedData mediaFeedData : elements){
						contatore--;
						if (!mediaFeedData.isUserHasLiked()){
							String idData = mediaFeedData.getId();
							LikesFeed like = instagram.setUserLike(idData); //mette il like all'utente
							System.out.println("like");
							try {
								Thread.sleep(6000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					esu.deleteUser(userId,index);

				}} catch (InstagramException e1) {
					// TODO Auto-generated catch block
					contatore =0;
					e1.printStackTrace();

				}
			utenteCorrente++;
		}
	}
	public boolean makeLiking(Instagram instagram,long cont,String index, ESutilities esu) throws Exception{
		List<String> idLista =esu.recoveryUsers01(index);
		boolean trovato = true;
		if(idLista==null)
			trovato=false;
		else
			putLikes(idLista,cont,instagram,esu,index);
		return trovato;

	}
	public boolean makeFollowing(Instagram instagram, long cont,String index, ESutilities esu) throws Exception{
		boolean trovato =true;
		List<String> idLista =esu.recoveryUsers01(index);
		if(idLista==null)
			trovato=false;
		else
			putFollow(idLista,cont,instagram,esu,index);
		return trovato;


	}

	public void makeComments (Instagram instagram, long cont, String index, ESutilities esu){
		List<String> commenti = creaCommenti();
		List<String> idLista = esu.recoveryUsers01(index);
		putComments(idLista, cont, instagram, esu, commenti,index);
	}



	public void putComments(List<String> idLista, long cont, Instagram instagram, ESutilities esu,List<String> commenti,String index){

		int utenteCorrente =0;
		int contatore = 3;

		while (contatore>0){
			String userId = idLista.get(utenteCorrente);
			try {
				if (userCounts(userId,instagram)>= 1){
					MediaFeed media;
					media = instagram.getRecentMediaFeed(userId, 1, null, null, null, null);

					List<MediaFeedData> elements = media.getData();
					for (MediaFeedData mediaFeedData : elements){
						contatore--;
						String idData = mediaFeedData.getId();
						String text = random(commenti,14);
						instagram.setMediaComments(idData, text); //mette il like all'utente
						System.out.println("commento");
						System.out.println(text);
						try {
							Thread.sleep(6000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					esu.deleteUser(userId,index);

				}} catch (InstagramException e1) {
					// TODO Auto-generated catch block
					contatore =0;
					e1.printStackTrace();

				}
			utenteCorrente++;


		}
	}
	/*seleziona randomicamente un commento dalla lista dei commenti */
	private String random(List<String> commenti,int n) {
		int contatore = (int) (Math.random()*n);
		String text = commenti.get(contatore);
		return text;

	}

	public List<String> creaCommenti(){
		List<String> commenti = new ArrayList<String>();
		String one = "Good work";
		String two = "Excellent!!";
		String three ="Follow back?";
		String four="Like Back?";
		String five="It's amazing!";
		String six="Like4like?";
		String seven="Cool :)";
		String eight="Comment4Comment";
		String nine="CommentBack?";
		String ten ="Want a shoutout?";
		String eleven ="Have you shout it?";
		String twelve="beautiful photo!:D";
		String thirtheen ="I'm really impressed!=)";
		String fourteen="Wooooooooow!!*_*";
		String fifteen ="Have you done it?";
		commenti.add(one);
		commenti.add(two);
		commenti.add(three);
		commenti.add(four);
		commenti.add(five);
		commenti.add(six);
		commenti.add(seven);
		commenti.add(eight);
		commenti.add(nine);
		commenti.add(ten);
		commenti.add(eleven);
		commenti.add(twelve);
		commenti.add(thirtheen);
		commenti.add(fourteen);
		commenti.add(fifteen);


		return commenti;


	}


	/*public List<String> mutualFollow (Instagram instagram, String id1){
		List<String> mutualId = new ArrayList<String>();
		List<String> followers = recuperaFollowers(id1);
		List<String> following = recuperaFollowing(id1);
		mutualId = confronta(followers,following);
		return mutualId;

	}*/

} //end class
