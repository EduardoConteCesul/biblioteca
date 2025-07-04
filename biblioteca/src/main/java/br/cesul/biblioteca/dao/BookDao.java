package br.cesul.biblioteca.dao;

import br.cesul.biblioteca.model.Book;
import br.cesul.biblioteca.ui.StatusLivro;
import br.cesul.biblioteca.util.MongoUtil;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DAO = Data Access Object
 * Centraliza toda a manipulação com o MongoDB para a coleção "books".
 *
 * ------- O QUE VOCÊ DEVE FAZER -------
 * 1. Implementar os 5 métodos com TODO.
 * 2. Usar a coleção 'books' fornecida.
 * 3. Converter Document para Book usando toBook().
 *
 * Dicas: *set*   troca o valor  |  *combine* junta várias ações.
 * */
public class BookDao {
    private final MongoCollection<Document> col = MongoUtil.db().getCollection("books");

    // Serialização Document para Book, não precisa mexer
    private Book toBook(Document d) {
        return new Book(
                d.getObjectId("_id").toHexString(),
                d.getString("titulo"),
                d.getString("autor"),
                d.getString("categoria"),
                d.getString("status"),
                d.getString("emprestadoPara")
        );
    }

    // ---------- TODO (1) READ ler todos documentos e retornar uma lista de Book ----------
    public List<Book> findAll() {

        // 1. criar lista local
        List<Book> livros = new ArrayList<>();

        // 2. percorrer col.find() em um foreach
        // 3. converter cada Document em Book
        FindIterable<Document> documents = col.find();
        documents.forEach(document -> livros.add(toBook(document)));

        // 4. retornar a lista local
        return livros;
    }

    // ---------- TODO (2) CREATE gravar novo livro no DB ----------
    public void insert(String titulo, String autor, String categoria) {

        if (titulo.isBlank() || autor.isBlank() || categoria.isBlank()) return;
        // inserir novo DOCUMENTO no mongo (col.insertOne(doc)) :

        col.insertOne(
                new Document("titulo", titulo)
                        .append("autor", autor)
                        .append("categoria", categoria)
                        .append("status", StatusLivro.DISPONIVEL)
                        .append("emprestadoPara", "")
        );
        // Neste momento o livro deve ser salvo com as variáveis que
        // voce recebeu por parametro + status="DISPONIVEL", emprestadoPara="".
    }

    // ---------- TODO (3) DELETE remover livro por id ----------
    public void delete(String id) {

        // remover (com col.deleteOne()) onde _id == id
        FindIterable<Document> documents = col.find();

        documents.forEach(document -> {
            if(document.getObjectId("_id").toHexString().equals(id)) {
                col.deleteOne(document);
            }
        });

        // Dica: Ver no projeto do jogo de dados >> Filters.eq.
    }

    // ---------- TODO (4) UPDATE (Emprestar Livro) ----------
    public void lend(String id, String leitor) {
        // Lógica parecida do exemplo “pontos/vitorias” visto no projeto do jogo de dados:
        // Usar col.updateOne().

        col.updateOne(
                eq("_id", new ObjectId(id)),combine(
                        set("emprestadoPara", leitor),
                        set("status", StatusLivro.EMPRESTADO)
                )
        );
        // Lembre-se que o updateOne recebe 2 parametros:
        // 1 - O primeiro é o índice de qual Document voce quer atualizar (usar Filters.eq).
        // 2 - O segundo é o valor que voce quer alterar deste documento. Neste caso
        //     seria um combine(set(), set()).
        //     (combine porque são dois valores, e o set é onde voce passa qual chave
        //     quer alterar no primeiro parametro e no segundo passa o novo valor
        //     Resumo: set("CHAVE", "VALOR")

        // Setar: 'status' >>>> EMPRESTADO e 'emprestadoPara' >>>>>> leitor
    }

    // ---------- TODO (5) UPDATE (Devolver Livro) ----------
    public void giveBack(String id) {
        // Mesmas observações do lend, a diferença aqui é que este metodo
        // altera o status="DISPONIVEL" e emprestadoPara=""
        col.updateOne(
                eq("_id", new ObjectId(id)),combine(
                        set("emprestadoPara", ""),
                        set("status", StatusLivro.DISPONIVEL)
                )
        );
    }

    /* --------- PRONTO: metodo usado pelo gráfico, não precisa mexer -------- */
    public Map<String, Long> countByCategory() {
        return findAll().stream().collect(Collectors.groupingBy(Book::categoria, Collectors.counting()));
    }
}
