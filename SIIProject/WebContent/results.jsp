

<%@ page import="org.examples.Constants" %>

<%@ page import="org.jinstagram.Instagram" %>
<%@ page import="java.util.*" %>


<%@ page import="org.jinstagram.entity.users.basicinfo.UserInfoData" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeed" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeedData" %>

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



<div class="container">

    <div class="row">

  
        <%
            UserInfoData userInfoData = instagram.getCurrentUserInfo().getData();

        %>
        <p class="lead">

   <%
   MediaFeed media =instagram.getUserFeeds();
  
   List<MediaFeedData> listaMedia =new ArrayList<MediaFeedData>();
   listaMedia= media.getData();
   int cont =0;
         %>

        <p>Follows : <%=userInfoData.getCounts().getFollows()%>
        </p>

        <p>Followed By : <%=userInfoData.getCounts().getFollwed_by()%>
        </p>

        <p>Media Count : <%=userInfoData.getCounts().getMedia()%>
        
        </p>
         <p>Media Likes : 
        <% for( MediaFeedData m : listaMedia){
        	System.out.println(cont);
        	cont = cont + m.getLikes().getCount();
        }
        	%>
        	</p>
        <p>
        </p>
        

</div>
    </div>

    <hr>


