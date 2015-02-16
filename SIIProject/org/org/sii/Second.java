package org.sii;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.elasticsearch.client.Client;
import org.examples.Constants;
import org.jinstagram.Instagram;
import org.jinstagram.entity.users.feed.MediaFeed;

/**
 * Servlet implementation class Second
 */
@WebServlet("/second")
public class Second extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Second() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tag = request.getParameter("tag");
		long cont = 500;
		HttpSession session = request.getSession();
		String go = request.getParameter("go");
		DBManager createConnection = new DBManager();
		//createConnection.createTable();
		if(go!=null && tag!=null && !tag.isEmpty()){
			Instagram instagram =(Instagram) session.getAttribute(Constants.INSTAGRAM_OBJECT);


			try {
				IncreaseFollowers increase= new IncreaseFollowers();

				//double lat =41.854690;
				//double lon=	12.469625;
				//Client client = increase.createClient();
				//for
				//double delta = 0.00001;
				//lat = lat+delta;
				//lon = lon+delta;
				Punto p1 = new Punto (		41.890070, 12.493347 );//colosseo
				Punto p2 = new Punto (41.938394, 12.534232); //casa nonna
				Punto p3 = new Punto	(41.903054, 12.431677); //san Pietro
				Punto p4 = new Punto	(41.868871, 12.439722); //casa
				Punto p7 = new Punto (41.855950, 12.469812); //università
				Punto p6 = new Punto	(41.827214, 12.508865); //laurentina

				Punto p5 = new Punto	(41.936668, 12.474662);//stadio
				List<Punto> punti = new ArrayList<Punto>();
				punti.add(p1);
				punti.add(p2);
				punti.add(p3);
				punti.add(p4);
				punti.add(p5);
				punti.add(p6);
				punti.add(p7);
		
				
				
				Punto m1 = new Punto (51.501564, -0.141911 );//buckingam palace
				Punto m2 = new Punto (48.858596, 2.294524); //torre eiffel
				Punto m3 = new Punto	(41.386001, 2.164260); //Barcellona
				Punto m4 = new Punto	(41.868871, 12.439722); //casa
				Punto m7 = new Punto (41.855950, 12.469812); //università
				Punto m6 = new Punto	(41.827214, 12.508865); //laurentina

				Punto m5 = new Punto	(41.936668, 12.474662);//stadio
				List<Punto> mondo = new ArrayList<Punto>();
				mondo.add(m1);
				mondo.add(m2);
				mondo.add(m3);
				mondo.add(m4);
				mondo.add(m5);
				mondo.add(m6);
				mondo.add(m7);
				
				//increase.squareSearch(instagram,mondo);
				//increase.mediaAnalyzer();
		
				boolean trovato = true;
				boolean val1= true;
				boolean val2 =true;
			while (trovato &&val1 &&val2){
				ESutilities esu = new ESutilities();
			if(val1)
			val1 =increase.makeFollowing(instagram, 1, "prova01",esu);
			if(val2)
			val2=	increase.makeLiking(instagram, 3, "prova02",esu);
				Thread.sleep(3600000);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
			ServletContext application = getServletContext();
			RequestDispatcher rd = application.getRequestDispatcher("/done.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub


	}

}
