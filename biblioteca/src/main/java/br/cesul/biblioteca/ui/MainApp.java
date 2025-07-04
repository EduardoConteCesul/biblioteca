package br.cesul.biblioteca.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Cria 3 abas e adiciona um listener que dispara refresh()
 * sempre que o usuário troca de aba.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        // instanciamos cada tela (content)
        TabPane tabs = new TabPane(
                mk("Acervo",      new BookPane()),
                mk("Empréstimos", new LendPane()),
                mk("Estatísticas",new StatsPane())
        );

        // listener = chama refresh da aba que ficou visível
        tabs.getSelectionModel().selectedItemProperty().addListener((obs, oldT, newT) -> {
            if (newT.getContent() instanceof Refreshable r) {
                r.refresh();                 // recarrega lista/gráfico
            }
        });

        stage.setScene(new Scene(tabs, 700, 480));
        stage.setTitle("Biblioteca Piloto");
        stage.show();
    }

    // helper que cria tab não-fechável
    private Tab mk(String title, Node content) {
        Tab t = new Tab(title, content);
        t.setClosable(false);
        return t;
    }

    public static void main(String[] args) { launch(args); }
}
