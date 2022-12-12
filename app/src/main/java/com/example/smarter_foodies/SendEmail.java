package com.example.smarter_foodies;
////"https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=375458593948-q6g9mqqp78olcictl0069anaji9c1nvp.apps.googleusercontent.com&redirect_uri=http://localhost:8888/Callback&response_type=code&scope=https://www.googleapis.com/auth/gmail.send"
//
//import java.util.*;
//
//import javax.mail.*;
//import javax.mail.internet.*;
//import javax.mail.Session;
//
//
//import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
//
//
//import android.net.LocalServerSocket;
//
//import com.google.android.gms.common.Scopes;
//import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.googleapis.json.GoogleJsonError;
//import com.google.api.client.googleapis.json.GoogleJsonResponseException;
//import com.google.api.client.http.HttpRequestInitializer;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.GmailScopes;
//import com.google.api.services.gmail.model.Message;
//import com.google.auth.Credentials;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.gson.annotations.Since;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.file.Paths;
//import java.security.GeneralSecurityException;
//import java.util.Properties;
//import java.util.Set;
//
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import org.apache.commons.codec.binary.Base64;
//
//
////public class SendEmail {
////
////
////    public static void send (String userEmail, String resume) {
////        try {
////            String stringSenderEmail = "smarterfoodies@gmail.com";
////            String stringReceiverEmail = "smarterfoodies@gmail.com";
////            String stringPasswordSenderEmail = "smarter1234";
////
////            String stringHost = "smtp.gmail.com";
////
////            Properties properties = System.getProperties();
////
////            properties.put("mail.smtp.host", stringHost);
////            properties.put("mail.smtp.port", "465");
////            properties.put("mail.smtp.ssl.enable", "true");
////            properties.put("mail.smtp.auth", "true");
////
////            Session session = Session.getInstance(properties, new Authenticator() {
////                @Override
////                protected PasswordAuthentication getPasswordAuthentication() {
////                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
////                }
////            });
//////
//////            MimeMessage mimeMessage = new MimeMessage(session);
//////            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));
//////            mimeMessage.setSubject("Subject: Application as Chef");
//////            mimeMessage.setText("User: " + userEmail + "\n Application: " + resume);
////            MimeMessage mimeMessage = createEmail("smarterfoodies@gmail.com","smarterfoodies@gmail.com",
////                    "Application","User: " + userEmail + "\n Application: " + resume);
////
////            Thread thread = new Thread(() -> {
////                try {
////                    Transport.send(mimeMessage);
////                } catch (MessagingException e) {
////                    e.printStackTrace();
////                }
////            });
////            thread.start();
////
////        } catch (AddressException e) {
////            e.printStackTrace();
////        } catch (MessagingException e) {
////            e.printStackTrace();
////        }
////    }
////
////    public static MimeMessage createEmail(String toEmailAddress,
////                                          String fromEmailAddress,
////                                          String subject,
////                                          String bodyText)
////            throws MessagingException {
////        Properties props = new Properties();
////        Session session = Session.getDefaultInstance(props, null);
////
////        MimeMessage email = new MimeMessage(session);
////
////        email.setFrom(new InternetAddress(fromEmailAddress));
////        email.addRecipient(javax.mail.Message.RecipientType.TO,
////                new InternetAddress(toEmailAddress));
////        email.setSubject(subject);
////        email.setText(bodyText);
////        return email;
////    }
////}
//
//
//
//
///* Class to demonstrate the use of Gmail Send Message API */
//public class SendEmail {
//
//    private final Gmail service;
//
//    public SendEmail() throws GeneralSecurityException, IOException {
//        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//
//        // Create the gmail API client
//        service = new Gmail.Builder(httpTransport,jsonFactory,getCredential(httpTransport,jsonFactory))
//                .setApplicationName("Gmail smarter_foodies")
//                .build();
//
//    }
//
//    private static Credential getCredential (final NetHttpTransport httpTransport, GsonFactory jsonFactory) throws IOException {
//
//        //load client secrets.
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(SendEmail.class.getResourceAsStream("/client_secret_375458593948-q6g9mqqp78olcictl0069anaji9c1nvp.apps.googleusercontent.com.json")));
//        System.out.println("after");
//        //Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, jsonFactory , clientSecrets, Set.of(GmailScopes.GMAIL_SEND))
//                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(88).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//
//    }
//
//
////    /**
////     * Send an email from the user's mailbox to its recipient.
////     *
////     * @param fromEmailAddress - Email address to appear in the from: header
////     * @param toEmailAddress   - Email address of the recipient
////     * @return the sent message, {@code null} otherwise.
////     * @throws MessagingException - if a wrongly formatted address is encountered.
////     * @throws IOException        - if service account credentials file not found.
////     */
//    public Message sendMail(String fromEmailAddress,
//                                    String toEmailAddress,String subject,String body)
//            throws MessagingException, IOException, GeneralSecurityException {
//        /* Load pre-authorized user credentials from the environment.
//           TODO(developer) - See https://developers.google.com/identity for
//            guides on implementing OAuth2 for your application.*/
////        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
////                .createScoped(GmailScopes.GMAIL_SEND);
//
//        // Encode as MIME message
//        Properties props = new Properties();
//        Session session = Session.getDefaultInstance(props, null);
//        MimeMessage email = new MimeMessage(session);
//        email.setFrom(new InternetAddress(fromEmailAddress));
//        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toEmailAddress));
//        email.setSubject(subject);
//        email.setText(body);
//
//        // Encode and wrap the MIME message into a gmail message
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        email.writeTo(buffer);
//        byte[] rawMessageBytes = buffer.toByteArray();
//        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
//        Message message = new Message();
//        message.setRaw(encodedEmail);
//
//        try {
//            // Create send message
//            message = service.users().messages().send("me", message).execute();
//            System.out.println("Message id: " + message.getId());
//            System.out.println(message.toPrettyString());
//            return message;
//        } catch (GoogleJsonResponseException e) {
//            // TODO(developer) - handle error appropriately
//            GoogleJsonError error = e.getDetails();
//            if (error.getCode() == 403) {
//                System.err.println("Unable to send message: " + e.getDetails());
//            }
//            if (error.getCode() == 404) {
//                System.err.println("Unable: " + e.getDetails());
//            }
//            else {
//                throw e;
//            }
//        }
//        return null;
//    }
//
//
////    public static Message createMessageWithEmail(MimeMessage emailContent)
////            throws MessagingException, IOException {
////        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
////        emailContent.writeTo(buffer);
////        byte[] bytes = buffer.toByteArray();
////        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
////        Message message = new Message();
////        message.setRaw(encodedEmail);
////        return message;
////    }
//
//
//}
//
//
//
//
