package cl.uai.uai.api.json;

import android.util.Log;

import java.util.Arrays;
import java.util.Comparator;

import cl.uai.uai.main.Helper;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class MessagesParser {


    public static void parse(MessagesResponse response) {
        Message[] messages =  new Message[0];
        messages = concat(messages, response.pregrado);
        messages = concat(messages, response.asuntos_estudiantiles);
        messages = concat(messages, response.deportes);
        messages = concat(messages, response.eventos_uai);
        messages = concat(messages, response.finanzas);
        messages = orderByDate(messages);

        for (Message message1 : messages) {
            Log.i("Adding to message list", "message: " + message1.id);
            Helper.addToMessagesList(message1);
        }
    }

    public static Message[] orderByDate(Message[] unordered) {
        Arrays.sort(unordered, new Comparator<Message>() {
            @Override
            public int compare(Message message, Message message2) {
                return message.date.compareToIgnoreCase(message2.date);
            }
        });

        return unordered;
    }

    public static Message[] concat(Message[] A, Message[] B) {
        int aLen = A.length;
        int bLen = B.length;
        Message[] C= new Message[aLen+bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

}
