/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.magenta.jsonpersist.mail;

import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  
import org.masukomi.aspirin.Aspirin;

/**
 *
 * @author martin
 */
public class SendMail {
    
    
    public static void main(String[] args) throws MessagingException {
        sendAspirin();
    }
    
    public static void sendAspirin() throws AddressException, MessagingException{
        String to = "mnn@magenta.dk";
        String from = "incong@neto.dk";
        MimeMessage message = Aspirin.createNewMimeMessage();
        message.setFrom(new InternetAddress(from));  
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
         message.setSubject("Ping2");  
         message.setText("Hello, this is example of sending email  ");
         Aspirin.add(message);
    }
    
    public static void sendMassage(){
        String to = "mnn@magenta.dk";
        String from = "mnn@magenta.dk";
        String host = "smtp.gmail.com";
  
     //Get the session object  
        Properties properties = System.getProperties();  
        properties.setProperty("mail.smtp.host", host);  
        Session session = Session.getDefaultInstance(properties);  
  
     //compose the message  
      try{  
         MimeMessage message = new MimeMessage(session);  
         message.setFrom(new InternetAddress(from));  
         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
         message.setSubject("Ping");  
         message.setText("Hello, this is example of sending email  ");  
  
         // Send message  
         
         Transport.send(message);  
         System.out.println("message sent successfully....");  
  
      }catch (MessagingException mex) {mex.printStackTrace();}  
   }  
    
}
