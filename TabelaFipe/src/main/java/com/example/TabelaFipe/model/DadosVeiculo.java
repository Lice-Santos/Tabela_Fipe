package com.example.TabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public record DadosVeiculo(@JsonAlias("codigo") String codigo,
                         @JsonAlias("nome") String nome) {

}
