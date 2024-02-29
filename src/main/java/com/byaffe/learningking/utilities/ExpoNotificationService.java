package com.byaffe.learningking.utilities;

import com.google.gson.Gson;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpoNotificationService {

    public static void sendNotification(String title, String description, List<String> deviceIds, Map<String, Object> extraData) throws PushClientException, InterruptedException {

        ExpoPushMessage expoPushMessage = new ExpoPushMessage();
        expoPushMessage.setTitle(title);
        expoPushMessage.setBody(description);
        expoPushMessage.setTo(deviceIds);
        expoPushMessage.setData(extraData);
        List<ExpoPushMessage> expoPushMessages = new ArrayList<>();
        expoPushMessages.add(expoPushMessage);

        PushClient client = new PushClient();
        List<List<ExpoPushMessage>> chunks = client.chunkPushNotifications(expoPushMessages);
        List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = new ArrayList<>();
        

        for (List<ExpoPushMessage> chunk : chunks) {
            messageRepliesFutures.add(client.sendPushNotificationsAsync(chunk));
        }

        // Wait for each completable future to finish
        List<ExpoPushTicket> allTickets = new ArrayList<>();
        for (CompletableFuture<List<ExpoPushTicket>> messageReplyFuture : messageRepliesFutures) {
            try {
                for (ExpoPushTicket ticket : messageReplyFuture.get()) {
                    System.err.println("ExpoPushTicket>>>>>"+new Gson().toJson(ticket));
                    allTickets.add(ticket);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
    
    public static void main(String[] args) {
         try {
             List<String> ids= Arrays.asList("ExponentPushToken[c9LbXNGjQ3l0N5mC0gR_ED]","ExponentPushToken[Ir_MwHI3J2PO5R_9nW2dKN]");
                ExpoNotificationService.sendNotification("Testing", "Testing description", ids, null);
            } catch (PushClientException | InterruptedException ex) {
                Logger.getLogger(AppUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}
