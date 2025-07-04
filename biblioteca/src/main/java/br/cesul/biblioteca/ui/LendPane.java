package br.cesul.biblioteca.ui;

import br.cesul.biblioteca.dao.BookDao;
import br.cesul.biblioteca.model.Book;

import com.mongodb.client.model.Filters;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Tela de EMPRESTIMOS / DEVOLUÇÃO.
 * Mostra dois ListViews (Disponíveis / Emprestados).
 * -------------------------
 * • Lista ESQUERDA >>> livros DISPONÍVEIS (os que tem status = DISPONIVEL)
 * • Lista DIREITA >>>>> livros EMPRESTADOS (os que tem status = EMPRESTADO)
 * • Campo texto >>>>> digitar o nome de quem pegou o livro
 * • Botão Emprestar >>>> chama dao.lend(id, leitor) (altera status do livro)
 * • Botão Devolver >>>>> dao.giveBack(id) (volta status do livro)
 *
 *  IMPORTANTE: Esta tela Implementa a interface Refreshable:
 *  Quando o usuário troca de aba, a MainApp  chama  refresh(),
 *  então este metodo PRECISA RECARREGAR AS DUAS LISTAS.
 */
public class LendPane extends BorderPane implements Refreshable {

    private final BookDao dao = new BookDao();

    // Listas observáveis ligadas aos ListViews, quando da setAll, a ListView se atualiza sozinho
    // Aqui estão os CONTEÚDOS das listviews
    private final ObservableList<Book> disponiveis = FXCollections.observableArrayList();
    private final ObservableList<Book> emprestados = FXCollections.observableArrayList();

    // ListViews atreladas às listas acima.
    // Estes são os widgets de tela, sem CONTEUDO, nao mostra nada
    private final ListView<Book> listDisp = new ListView<>(disponiveis);
    private final ListView<Book> listEmp  = new ListView<>(emprestados);

    private TextField tLeitor;
    private Button btnEmprestar, btnDevolver;

    // Como estamos trabalhando com abas que são telas chamadas pela main.
    // montamos a interface aqui memsmo, no construtor de LendPage()
    public LendPane() {
        // Aqui formatei como quero exibir cada linha
        // Usa a BookCell (classe interna) para mostrar “Título → Leitor”
        // Não precisa mexer
        listDisp.setCellFactory(lv -> new BookCell());
        listEmp .setCellFactory(lv -> new BookCell());

        /* =================================================
           TODO (A) — CAMPO TEXTO onde digitar o nome
           • Use: TextField tLeitor
           • Dica: use setPromptText() pra setar o placeholder do campo
           =================================================
        */

        // Faça aqui o TODO (A)

        tLeitor = new TextField();
        tLeitor.setPromptText("Nome do Leitor");


        /* =================================================
           TODO (B) — BOTÃO “Emprestar”
           -------------------------------------------------
           Metas da ação do botão:
             - Descobrir qual livro está selecionado
             - Ler o nome digitado no TextField 'tLeitor'
             - Chamar a lógica de empréstimo no DAO
             - Limpar o campo de texto
             - Atualizar as listas na tela (refresh)

           Métodos úteis:
             * btnEmprestar.setOnAction(event -> { … }):
               > registra o que acontece quando o usuário clica.

             * listDisp.getSelectionModel().getSelectedItem():
               > devolve o Book atualmente destacado (clicado) na ListView de DISPONÍVEIS.

             * tLeitor.getText():
               > devolve o texto digitado (nome do leitor).

             * dao.lend(id, nome):
               > grava no mongo (altera status para emprestado).

             * tLeitor.clear():
               > limpa o campo de texto.

             * refresh():
               > atualiza a lista do banco e refaz as duas ListViews.

           Use um if para garantir que existe um livro
           selecionado (clicado) E que o campo de leitor não está vazio.
           =================================================
        */

        // Faça aqui o TODO (B)

        btnEmprestar = new Button("Emprestar");

        btnEmprestar.setOnAction(actionEvent -> {
            Book bookSelected = listDisp.getSelectionModel().getSelectedItem();
            String nameLeitor = tLeitor.getText();
            if (bookSelected != null && !nameLeitor.isBlank()){

                dao.lend(bookSelected.id(), nameLeitor);
                tLeitor.clear();
                refresh();
            }
        });


        /* =================================================
           TODO (C) — BOTÃO “Devolver”
           Mesma lógica do botão emprestar, muda apenas o metodo do dao e a lista
           =================================================
        */

        // Faça aqui o TODO (C)

        btnDevolver = new Button("Devolver");

        btnDevolver.setOnAction(actionEvent -> {
            Book bookSelected = listEmp.getSelectionModel().getSelectedItem();
            dao.giveBack(bookSelected.id());
            refresh();
        });

           // LAYOUT FINAL, APENAS DESCOMENTE E VERIFIQUE OS NOMES DAS VARIÁVEIS
           VBox left  = new VBox(new Label("Disponíveis"), listDisp, tLeitor, btnEmprestar);
           VBox right = new VBox(new Label("Emprestados"), listEmp, btnDevolver);
           left.setSpacing(8);
           right.setSpacing(8);
           setPadding(new Insets(10));
           setLeft(left);
           setCenter(right);

        // carrega os dados pela primeira vez, deixar essa linha por ultimo
        refresh();
    }

    // Atualiza as listas a partir do banco usando dao.findAll()
    // TODO RECARREGAR listas toda vez que refresh() for chamado
    @Override public void refresh() {
        // 1. pega todos livros >>>> dao.findAll()
        // 2. filtra por status "DISPONIVEL" >>>> colocar em listDisp (usar equals para filtrar)
        // 3. filtra por status "EMPRESTADO" >>>> colocar em listEmp (usar equals para filtrar)
        // 4. Dar setAll em ambas as listas

        disponiveis.clear();
        emprestados.clear();

        List<Book> listBooks = dao.findAll();
        listBooks.forEach(book -> {
            if(book.status().equals(StatusLivro.DISPONIVEL.toString())){
                disponiveis.add(book);
            }
            if(book.status().equals(StatusLivro.EMPRESTADO.toString())){
                emprestados.add(book);
            }
        });
    }

    // célula customizada
    private static class BookCell extends ListCell<Book> {
        @Override protected void updateItem(Book b, boolean empty) {
            super.updateItem(b, empty);
            if (empty || b == null) {
                setText(null);
            } else {
                setText(b.titulo() + (b.status().equals("EMPRESTADO") ? " → " + b.emprestadoPara() : ""));
            }
        }
    }
}
