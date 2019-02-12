package dk.magenta.jsonpersist;

import dk.magenta.jsonpersist.persistence.FilePersistor;
import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;


@Path("rest")
public class RestService {
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieveRecords() {     
        return "Got it!";
    }
    
    @GET
    @Path("/{recordID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieveRecord(@PathParam("recordID") String id) {
        return "Got it!";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String saveJSON(String jsonString) throws IOException {
        FilePersistor persistor = new FilePersistor();
        Integer id = persistor.saveJSON(jsonString);
        return "id: "+id;
    }
}
