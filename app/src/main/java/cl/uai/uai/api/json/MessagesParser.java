package cl.uai.uai.api.json;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class MessagesParser {


    public static Message[] parse(MessagesResponse response) {
        Message[] messages =  new Message[0];
        messages = concat(messages, response.pregrado);
        messages = concat(messages, response.asuntos_estudiantiles);
        messages = concat(messages, response.deportes);
        messages = concat(messages, response.eventos_uai);
        messages = concat(messages, response.finanzas);
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
