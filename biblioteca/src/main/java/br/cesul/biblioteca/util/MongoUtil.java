package br.cesul.biblioteca.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * MongoUtil — classe utilitária:
 *   • cria UMA única conexão com o MongoDB
 *   • qualquer parte da app chama MongoUtil.db()
 *
 * Se criássemos MongoClient em cada DAO, abririam dezenas
 * de conexões
 */
public class MongoUtil {

    // conexão única, estática e final
    private static final MongoClient CLIENT =
            MongoClients.create("mongodb://localhost:27017"); // troque a URI se precisar

    // expõe o database “biblioteca_piloto”
    public static MongoDatabase db() {
        return CLIENT.getDatabase("biblioteca_piloto");
    }
}
