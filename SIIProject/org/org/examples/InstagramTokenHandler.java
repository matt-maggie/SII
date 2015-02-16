package org.examples;

import org.jinstagram.Instagram;

import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.sii.ESutilities;
import org.sii.IncreaseFollowers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;


public class InstagramTokenHandler extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String code = request.getParameter("code");
        System.out.println(code);

        InstagramService service = (InstagramService) request.getSession().getAttribute(Constants.INSTAGRAM_SERVICE);

        Verifier verifier = new Verifier(code);

        Token accessToken = service.getAccessToken(null, verifier);
        System.out.println(accessToken.toString());
        Properties properties = InstagramUtils.getConfigProperties();
        String clientSecret = properties.getProperty(Constants.CLIENT_SECRET);

      //  String clientSecret ="14eea68c28704f86a23cbd60dea2a7dd";
        Instagram instagram = new Instagram(accessToken.getToken(),clientSecret,"127.0.0.1");
       // Instagram instagram = new Instagram(accessToken);
        HttpSession session = request.getSession();

        session.setAttribute(Constants.INSTAGRAM_OBJECT, instagram);
        
        System.out.println(request.getContextPath());
        // Redirect to User Profile page.
        
    	ESutilities esu = new ESutilities();
IncreaseFollowers increase= new IncreaseFollowers();

        response.sendRedirect(request.getContextPath() + "/second.jsp");
        String logout = request.getParameter("logout");
     

    }


}
