package br.cesul.biblioteca.ui;

import br.cesul.biblioteca.dao.BookDao;
import br.cesul.biblioteca.model.Book;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * Tela de ACERVO (CRUD completo).
 * Implementa Refreshable > recarrega sempre que a aba
 * ganhar foco ou após alteração.
 */
public class BookPane extends BorderPane implements Refreshable {

    private final BookDao dao = new BookDao();

    // ObservableList liga banco com TableView
    private final ObservableList<Book> data = FXCollections.observableArrayList();

    private final TableView<Book> table = new TableView<>(data);

    public BookPane() {
        setPadding(new Insets(10));

        // === COLUNAS ===
        table.getColumns().addAll(
                col("Título",   "titulo",      200),
                col("Autor",    "autor",       140),
                col("Categoria","categoria",   100),
                col("Status",   "status",       90),
                col("Leitor",   "emprestadoPara", 140)
        );
        setCenter(table);

        // === FORMULÁRIO “Adicionar livro”
        TextField tTitulo    = txt("Título");
        TextField tAutor     = txt("Autor");
        TextField tCategoria = txt("Categoria");

        Button add = new Button("Adicionar");
        add.setOnAction(e -> {
            // validação simples
            if (tTitulo.getText().isBlank()) return;

            dao.insert(tTitulo.getText(), tAutor.getText(), tCategoria.getText());
            tTitulo.clear(); tAutor.clear(); tCategoria.clear();
            refresh();                         // atualiza tabela
        });

        Button del = new Button("Excluir");
        del.setOnAction(e -> {
            Book b = table.getSelectionModel().getSelectedItem();
            if (b != null) {
                dao.delete(b.id());
                refresh();
            }
        });

        GridPane form = new GridPane();
        form.setHgap(6); form.setVgap(6);
        form.addRow(0, new Label("Título:"), tTitulo);
        form.addRow(1, new Label("Autor:"),  tAutor);
        form.addRow(2, new Label("Categoria:"), tCategoria, add, del);
        setTop(form);

        // carrega registros existentes
        refresh();
    }

    // Refreshable = recarrega lista do banco
    @Override public void refresh() { data.setAll(dao.findAll()); }

    // Helper para criar TextFields com placeholder
    private TextField txt(String prompt) {
        TextField t = new TextField();
        t.setPromptText(prompt);
        return t;
    }

    // Helper para criar colunas com lambda
    private TableColumn<Book, String> col(String title, String field, int w) {
        TableColumn<Book, String> c = new TableColumn<>(title);
        c.setMinWidth(w);
        c.setCellValueFactory(v -> {
            Book b = v.getValue();
            return switch (field) {
                case "titulo"         -> new ReadOnlyStringWrapper(b.titulo());
                case "autor"          -> new ReadOnlyStringWrapper(b.autor());
                case "categoria"      -> new ReadOnlyStringWrapper(b.categoria());
                case "status"         -> new ReadOnlyStringWrapper(b.status());
                default               -> new ReadOnlyStringWrapper(b.emprestadoPara());
            };
        });
        return c;
    }
}
