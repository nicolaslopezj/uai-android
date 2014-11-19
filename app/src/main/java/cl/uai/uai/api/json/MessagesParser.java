package cl.uai.uai.api.json;

import cl.uai.uai.main.Helper;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class MessagesParser {


    public static Message[] parse(MessagesResponse response) {
        Message[] messages =  new Message[0];
        messages = Helper.isSubscribedTo("pregrado") ? concat(messages, response.pregrado) : messages;
        messages = Helper.isSubscribedTo("asuntos_estudiantiles") ? concat(messages, response.asuntos_estudiantiles) : messages;
        messages = Helper.isSubscribedTo("deportes") ? concat(messages, response.deportes) : messages;
        messages = Helper.isSubscribedTo("eventos_uai") ? concat(messages, response.eventos_uai) : messages;
        messages = Helper.isSubscribedTo("finanzas") ? concat(messages, response.finanzas) : messages;
        return messages;
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
