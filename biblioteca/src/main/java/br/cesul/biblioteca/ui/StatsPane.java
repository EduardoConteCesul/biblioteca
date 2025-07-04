package br.cesul.biblioteca.ui;

import br.cesul.biblioteca.dao.BookDao;

import javafx.collections.FXCollections;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Tela de estatisticas.
 * Gera um grafico Pizza com número de livros por categoria.
 * Recalcula sempre que refresh() é chamado.
 */
public class StatsPane extends BorderPane implements Refreshable {

    private final BookDao dao = new BookDao();
    private final PieChart chart = new PieChart();

    public StatsPane() {
        setCenter(new VBox(new Label("Livros por Categoria"), chart));
        refresh();        // primeiro carregamento
    }

    @Override public void refresh() {
        chart.getData().clear();        // zera dados antigos
        dao.countByCategory().forEach((cat, qt) ->
                chart.getData().add(new PieChart.Data(cat, qt)));
    }
}
