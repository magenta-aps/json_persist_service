/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.magenta.jsonpersist.mail;

import dk.magenta.jsonpersist.CustomProperties;
import dk.magenta.jsonpersist.RestService;
import dk.magenta.jsonpersist.Utilities;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author martin
 */
public class SendMail {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void sendMassage(Integer id, JSONObject payload) throws IOException, AddressException, MessagingException {
        CustomProperties conf = Utilities.getProperties();
        String to = null;
        String from = conf.getMailFrom();
        String host = conf.getMailServer();

        String table = "<table cellspacing=\"0\" cellpadding=\"10\" border=\"0\">";

        JSONArray blocks = payload.getJSONArray("blocks");
        for (int i = 0; i < blocks.length(); i++) {
            JSONObject block = blocks.getJSONObject(i);
            JSONArray fields = block.getJSONArray("fields");
            for (int j = 0; j < fields.length(); j++) {
                JSONObject field = fields.getJSONObject(j);
                String blockLabel = block.getString("label");
                String fieldLabel = field.getString("label");
                if ("partners".equals(blockLabel)) {
                    Integer index = field.getInt("label") + 1;
                    JSONObject projectPartner = field.getJSONObject("content");
                    table = table + getApplicantRows(projectPartner, index);
                } else if ("project".equals(blockLabel) && "subject".equals(fieldLabel)) {
                    table = table + getProjectRows(field);
                } else {
                    if ("applicant".equals(block.getString("label")) && conf.getToField().equals(field.getString("label"))) {
                        to = field.getString("content");
                    }
                    table = table + getBasicRow(field);
                }

//                if ("applicant".equals(block.getString("label")) && conf.getToField().equals(field.getString("label"))) {
//                    to = field.getString("content");
//                }
//                Object value = field.get("content");
//                if (value instanceof JSONObject) {
//                    JSONObject applicant = (JSONObject) value;
//                    List<String> keyList = new ArrayList<>(applicant.keySet());
//                    Collections.sort(keyList);
//                    String modifier = "";
//                    try {
//                        modifier = (Integer.valueOf(field.getString("display_name")) + 1) + "";
//                    } catch (NumberFormatException ex) {
//                        modifier = field.getString("display_name");
//                    }
//                    table = table + "<tr ><td width=\"80\" colspan=\"2\" valign=\"top\"><strong>Projektpartner " + modifier + "</strong></td></tr>\n";
//                    for (String key : keyList) {
//                        JSONObject applicantPart = applicant.getJSONObject(key);
//                        table = table + "<tr><td width=\"80\" valign=\"top\">" + applicantPart.get("display_name") + "</td>\n";
//                        table = table + "<td width=\"280\" valign=\"top\">" + applicantPart.get("value") + "</td></tr>";
//                    }
//
//                } else {
//                    table = table + "<tr style=\"border-bottom:1px solid #dddddd\"><td width=\"80\" valign=\"top\"><strong>" + field.getString("display_name") + "</strong></td>\n";
//                    table = table + "<td width=\"280\" valign=\"top\">" + field.get("content") + "</td></tr>";
//                }
            }

        }
        table = table + "</table>";

        //Get the session object  
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(properties);

        //compose the message  
        String text = new Scanner(SendMail.class.getResourceAsStream("/dk/magenta/jsonpersist/applicationMail.html.tmpl")).useDelimiter("\\Z").next();
        text = text.replace("{applicationID}", id.toString());
        text = text.replace("{logopath}", conf.getLogoPath());
        text = text.replace("{Footer}", "");
        text = text.replace("{values}", table);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Tak for din ansøgning", "UTF-8");
        message.setContent(text, "text/html; charset=UTF-16");

        // Send message  
        Transport.send(message);
    }

    public static String getBasicRow(JSONObject field) {
        String table = "<tr style=\"border-bottom:1px solid #dddddd\"><td width=\"80\" valign=\"top\"><strong>" + field.getString("display_name") + "</strong></td>\n";
        return table + "<td width=\"280\" valign=\"top\">" + field.get("content") + "</td></tr>";
    }

    public static String getApplicantRows(JSONObject applicant, Integer index) {
        List<String> keyList = new ArrayList<>(applicant.keySet());
        Collections.sort(keyList);

        String tableRow = "<tr ><td width=\"80\" colspan=\"2\" valign=\"top\"><strong>Projektpartner " + index + "</strong></td></tr>\n";
        for (String key : keyList) {
            JSONObject applicantPart = applicant.getJSONObject(key);
            tableRow = tableRow + "<tr><td width=\"80\" valign=\"top\">" + applicantPart.get("display_name") + "</td>\n";
            tableRow = tableRow + "<td width=\"280\" valign=\"top\">" + applicantPart.get("value") + "</td></tr>";
        }
        return tableRow;
    }

    public static String getProjectRows(JSONObject project) {
        JSONObject content = project.getJSONObject("content");
        List<String> keyList = new ArrayList<>(content.keySet());
        String table = "<tr ><td width=\"80\" colspan=\"2\" valign=\"top\"><strong>Projekt kategorier</strong></td></tr>\n";
        for (String key : keyList) {
            table = table + "<tr ><td width=\"80\" colspan=\"2\" valign=\"top\">" + content.getJSONObject(key).getString("value") + "</td></tr>\n";
        }
        return table;
    }

    protected static String getField(JSONObject payload, String fieldLabel) {
        JSONArray blocks = payload.getJSONArray("blocks");
        for (int i = 0; i < blocks.length(); i++) {
            JSONObject block = blocks.getJSONObject(0);
            if ("applicant".equals(block.getString("label"))) {
                JSONArray fields = block.getJSONArray("fields");
                for (int j = 0; j < fields.length(); j++) {
                    JSONObject field = fields.getJSONObject(j);
                    if (fieldLabel.equals(field.getString("label"))) {
                        return field.getString("content");
                    }
                }
            }
        }
        return null;
    }

}
