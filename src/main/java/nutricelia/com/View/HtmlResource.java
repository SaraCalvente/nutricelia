package nutricelia.com.View;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Path("/")
public class HtmlResource {


    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getInitialPage()throws IOException{
        return "<meta http-equiv=\"refresh\" content=\"0; url=/InitialPage.html\">";
    }

    @Path("/HomePage")
   @GET
   @Produces(MediaType.TEXT_HTML)
   public String getHomePage()throws IOException{
       return "<meta http-equiv=\"refresh\" content=\"0; url=/HomePage.html\">";

   }



}
