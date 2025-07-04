package br.cesul.biblioteca.ui;

/**
 * Interface:
 *   Componentes que precisam recarregar seus dados do banco
 *   implementam este metodo. O TabPane chama refresh()
 *   toda vez que a aba ganha foco.
 */
public interface Refreshable {
    void refresh();
}
