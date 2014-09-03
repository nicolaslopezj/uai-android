package cl.uai.uai.api.json;

import com.google.api.client.util.Key;

/**
 * Created by nicolaslopezj on 01-09-14.
 */
public class MessagesResponse {

    @Key
    public Message[] pregrado;

    @Key
    public Message[] eventos_uai;

    @Key
    public Message[] asuntos_estudiantiles;

    @Key
    public Message[] deportes;

    @Key
    public Message[] finanzas;

}
