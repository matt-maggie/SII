package org.sii;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.exceptions.InstagramException;

public class ESutilities {
	
	public ESutilities(){}
	
	

	public void mediaAnalyzer (){
		Node node = nodeBuilder().node();
		Client client = node.client();
		int contatore = 0;
		SearchResponse s1 = client.prepareSearch("instagraMondo")     
				.addFields("_id")
				.setQuery(QueryBuilders.matchAllQuery())
				.setSize(20000)
				.execute()
				.actionGet();
		System.out.println(s1.toString());

		SearchHits search=	s1.getHits();
		System.out.println("bellaa");
		while(contatore >0){
			for (SearchHit hit : search) { 
				String id = hit.getId();
				Map<String, Object> json = new HashMap<String, Object>();
				json.put("id",id);

				//String id = "{"+ "id:"+  hit.id()+"}";
				if (contatore>=2519){
					IndexResponse resp = client.prepareIndex("prova01", "temp",id)
							.setSource(json)
							.execute()
							.actionGet();
					contatore--;
					System.out.println("daje1");
				}
				else{
					IndexResponse resp = client.prepareIndex("prova02", "temp",id)
							.setSource(json)
							.execute()
							.actionGet();
					contatore--;
					System.out.println("daje2");
				}
			}

		}


		node.close();


	}
	
	
public void sdoppiaDb (Instagram instagram, String index){

	int contatore =500;
	List<String> userRecovered = recoveryUsers01(index);
	while(contatore >0){
		
		Node node = nodeBuilder().node();
		Client client = node.client();
		Map<String, Object> json = new HashMap<String, Object>();
		String id = userRecovered.get(contatore);
		json.put("id",id);
		IndexResponse resp = client.prepareIndex("prova03", "temp",id)
				.setSource(json)
				.execute()
				.actionGet();
		node.close();
		
		deleteUser(id,"prova02");
		
		contatore--;

System.out.println("andata");
	}
	
	
	
}

	public void ObjectAnalyzer (Instagram instagram) throws InstagramException{
		Node node = nodeBuilder().node();
		Client client = node.client();
		int contatore = 5038;
		SearchResponse s1 = client.prepareSearch("instagramnew")     
				.addFields("_id")
				.setQuery(QueryBuilders.matchAllQuery())
				.setSize(20000)
				.execute()
				.actionGet();
		System.out.println(s1.toString());

		SearchHits search=	s1.getHits();
		System.out.println("bellaa");
		while(contatore >0){
			for (SearchHit hit : search) {   
				String id = hit.getId();
				GetResponse response = client.prepareGet("instagramnew", "utenti", id)
						.execute()
						.actionGet();
				//String id = "{"+ "id:"+  hit.id()+"}";
				if (contatore>2519){
					IndexResponse resp = client.prepareIndex("cl01", "utente",id)
							.setSource(response)
							.execute()
							.actionGet();
					contatore--;
					System.out.println("daje1");
				}
				else{
					IndexResponse resp = client.prepareIndex("cl02", "utente",id)
							.setSource(response)
							.execute()
							.actionGet();
					contatore--;
					System.out.println("daje2");
				}
			}

		}


		node.close();


	}



	public List<String> recoveryUsers01(String index){
		List<String> userRec = new ArrayList<String>();
		Node node = nodeBuilder().node();
		Client client = node.client();
		SearchResponse s1 = client.prepareSearch(index)     
				.addFields("_id")
				.setQuery(QueryBuilders.matchAllQuery())
				.setSize(20000)
				.execute()
				.actionGet();
		SearchHits search=	s1.getHits();
		for (SearchHit hit : search) {    
			String id = hit.id();
			userRec.add(id);

		}
		node.close();

		return userRec;
	}




	public void deleteUser(String id, String index){
		Node node = nodeBuilder().node();
		Client client = node.client();
		DeleteResponse response = client.prepareDelete(index,"temp", id)
				.execute()
				.actionGet();
		System.out.println("rimosso");
		node.close();


	}

	public void copy(){

		Node node = nodeBuilder().node();
		Client client = node.client();
		SearchResponse s1 = client.prepareSearch("prova01")     
				.addFields("_id")
				.setQuery(QueryBuilders.matchAllQuery())
				.setSize(20000)
				.execute()
				.actionGet();

		SearchHits search=	s1.getHits();
		for (SearchHit hit : search) {
			String id = hit.getId();
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("id",id);
			IndexResponse resp = client.prepareIndex("backup01", "temp",id)
					.setSource(json)
					.execute()
					.actionGet();
		}

		node.close();

	}



	public void copy3(){

		Node node = nodeBuilder().node();
		Client client = node.client();
		SearchResponse s1 = client.prepareSearch("prova03")     
				.addFields("_id")
				.setQuery(QueryBuilders.matchAllQuery())
				.setSize(20000)
				.execute()
				.actionGet();

		SearchHits search=	s1.getHits();
		for (SearchHit hit : search) {
			String id = hit.getId();
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("id",id);
			IndexResponse resp = client.prepareIndex("backup03", "temp",id)
					.setSource(json)
					.execute()
					.actionGet();
		}


		node.close();

	}


}
