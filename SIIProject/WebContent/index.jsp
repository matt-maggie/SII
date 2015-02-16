

<%@ page import="org.jinstagram.auth.oauth.InstagramService"  %>
<%@ page import="org.jinstagram.auth.InstagramAuthService" %>
<%@ page import="org.examples.InstagramUtils" %>
<%@ page import="org.examples.Constants" %>

<%@ page import="java.util.Properties" %>

<%
    // If we already have an instagram object, then redirect to profile.jsp
    Object objInstagram = session.getAttribute(Constants.INSTAGRAM_OBJECT);
   /* if (objInstagram != null) {
    	
       response.sendRedirect(request.getContextPath() + "/second.jsp");
    } */

    Properties properties = InstagramUtils.getConfigProperties();

    String clientId = properties.getProperty(Constants.CLIENT_ID);
    String clientSecret = properties.getProperty(Constants.CLIENT_SECRET);
    String callbackUrl = properties.getProperty(Constants.REDIRECT_URI);
    String scope = properties.getProperty(Constants.SCOPE);


    InstagramService service = new InstagramAuthService()
            .apiKey(clientId)
            .apiSecret(clientSecret)
            .callback(callbackUrl)
            .scope("likes relationships")
            .build();
    		

    String authorizationUrl = service.getAuthorizationUrl(null);

    session.setAttribute(Constants.INSTAGRAM_SERVICE, service);
%>
  <input align="center" type="submit" name="logout" value="logout">

<div class="container">

    <div class="jumbotron">
        <h1>Demo</h1>
        <p class="lead">A web application, developed by Matteo Amadei e Margherita Sidoti based on  <a href="https://github.com/sachin-handiekar/jInstagram">jInstagram API</a></p>
        <p><a href="<%= authorizationUrl%>"><img src="entra.png"/></a>
        </p>
    </div>

