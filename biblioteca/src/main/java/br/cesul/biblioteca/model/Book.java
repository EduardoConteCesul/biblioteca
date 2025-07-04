package br.cesul.biblioteca.model;

/**
 * Record = classe imutável + construtor + getters automáticos.
 * Representa um livro do acervo:
 *  • status : "DISPONIVEL" ou "EMPRESTADO"
 *  • emprestadoPara : nome do leitor (string vazia se disponível)
 */
public record Book(
        String id,
        String titulo,
        String autor,
        String categoria,
        String status,
        String emprestadoPara
) {}
