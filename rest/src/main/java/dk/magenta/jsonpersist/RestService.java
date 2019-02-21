package dk.magenta.jsonpersist;

import dk.magenta.jsonpersist.mail.SendMail;
import dk.magenta.jsonpersist.persistence.FilePersistor;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONException;
import org.json.JSONObject;

@Path("rest")
public class RestService {

    private static final Logger LOGGER = LogManager.getLogger();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String retrieveRecords() throws IOException {
        LOGGER.info("Recieved get");
        return "I'm alive!";
    }
//    
//    @GET
//    @Path("/{recordID}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String retrieveRecord(@PathParam("recordID") String id) throws IOException {
//        FilePersistor persistor = new FilePersistor();
//        return persistor.jsonString(Integer.valueOf(id));
//    }, @Context UriInfo ui

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //@Consumes(MediaType.APPLICATION_JSON)
    public String saveJSON(FormDataMultiPart formData) throws IOException, MessagingException {
        try {
            LOGGER.info("Recieved multipart " + formData.getFields().keySet());
            JSONObject object;
            object = new JSONObject();
            Map<String, List<FormDataBodyPart>> formParams = formData.getFields();
            for (String key : formParams.keySet()) {
                List<FormDataBodyPart> values = formParams.get(key);
                if (values.size() == 1) {
                    object.put(key, values.get(0).getValue());
                } else if (values.size() > 1) {
                    for (FormDataBodyPart bodyPart : values) {
                        object.append(key, bodyPart.getValue());
                    }
                }
            }
            FilePersistor persistor = new FilePersistor();
            Integer id = persistor.saveJSON(object.toString());
            SendMail.sendMassage(id, object);
            return "id: " + id;
        } catch (IOException | MessagingException | RuntimeException ex) {
            LOGGER.error(ex);
            throw ex;
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String saveJSON(MultivaluedMap<String, String> formParams) throws IOException, MessagingException {
        try {
            LOGGER.info("Recieved urlencoded form " + formParams.keySet());
            JSONObject object;
            object = new JSONObject();
            for (String key : formParams.keySet()) {
                List<String> values = formParams.get(key);
                if (values.size() == 1) {
                    object.put(key, values.get(0));
                } else if (values.size() > 1) {
                    object.put(key, values);
                }

            }
            FilePersistor persistor = new FilePersistor();
            Integer id = persistor.saveJSON(object.toString());
            SendMail.sendMassage(id, object);
            return "id: " + id;
        } catch (IOException | MessagingException | RuntimeException ex) {
            LOGGER.error(ex);
            throw ex;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveJSON(String jsonString) throws IOException, MessagingException {
        try {
            LOGGER.info("Recieved JSON " + jsonString);
            FilePersistor persistor = new FilePersistor();
            Integer id = persistor.saveJSON(jsonString);
            SendMail.sendMassage(id, new JSONObject(jsonString));
            return "id: " + id;
        } catch (IOException | MessagingException | RuntimeException ex) {
            LOGGER.error(ex);
            throw ex;
        }
    }
}
