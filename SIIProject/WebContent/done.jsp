<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    

<%@ page import="org.jinstagram.auth.oauth.InstagramService"  %>
<%@ page import="org.jinstagram.auth.InstagramAuthService" %>
<%@ page import="org.examples.InstagramUtils" %>
<%@ page import="org.examples.Constants" %>
<%@ page import="org.jinstagram.Instagram" %>
<%@ page import="org.jinstagram.entity.users.basicinfo.UserInfoData" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeed" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeedData" %>
<%@ page import="java.util.*" %>



<%@ page import="java.util.Properties" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
DONE
<%

    Object objInstagram = session.getAttribute(Constants.INSTAGRAM_OBJECT);

    Instagram instagram = null;

    if (objInstagram != null) {
        instagram = (Instagram) objInstagram;
    } else {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }


%>


<%   
// Remove both of the objects from the session
session.removeAttribute(Constants.INSTAGRAM_OBJECT);
session.removeAttribute(Constants.INSTAGRAM_SERVICE);


%>


  <%
            UserInfoData userInfoData = instagram.getCurrentUserInfo().getData();

        %>
        <p class="lead">

   <%
  

   int cont =0;
         %>

        <p>Follows : <%=userInfoData.getCounts().getFollows()%> <!-- numero di utenti che seguo -->
        </p>

        <p>Followed By : <%=userInfoData.getCounts().getFollwed_by()%>  <!-- numero di utenti che mi seguo -->
        </p>

        <p>Media Count : <%=userInfoData.getCounts().getMedia()%>
        
        </p>
         <p>Media Likes : 
         
        <%  String id =userInfoData.getId();
         MediaFeed media= instagram.getRecentMediaFeed(id);
         List<MediaFeedData> listaMedia;
         listaMedia= media.getData();
        for( MediaFeedData m :listaMedia){ %>
        <%= m.getTags() %>
        Media+
        	<%= cont =cont +m.getLikes().getCount()%>
        	<%} %>
        	

</body>
</html>