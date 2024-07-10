package com.example.TabelaFipe.Service;

import java.util.List;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe); //o T torna genérico o que espera
    <T> List<T> obterLista(String json, Class<T> classe);
}
