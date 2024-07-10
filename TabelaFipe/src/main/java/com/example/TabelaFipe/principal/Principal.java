package com.example.TabelaFipe.principal;

import com.example.TabelaFipe.Service.ConsumoAPI;
import com.example.TabelaFipe.Service.ConverteDados;
import com.example.TabelaFipe.model.DadosModelo;
import com.example.TabelaFipe.model.DadosVeiculo;
import com.example.TabelaFipe.model.Veiculo;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    public static String ANSI_BLUE = "\u001B[34m";
    public static String ANSI_YELLOW = "\u001B[33m";
    public static String ANSI_GREEN = "\u001B[32m";
    public static String ANSI_RED = "\u001B[31m";
    public static String ANSI_CLEAR = "\u001B[0m";

    private ConsumoAPI consumo = new ConsumoAPI();
    private Scanner scanner = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    public void exibeMenu() {

        System.out.println(ANSI_BLUE + "\n-------------- SEJA BEM VINDO AO SISTEMA DE TABELA FIPE --------------\n" + ANSI_CLEAR);
        String endereco;
        while (true) {
            System.out.println("""
                    Qual tipo de automóvel você deseja pesquisar? 
                    Tipos: carros, motos, caminhões.
                    """);
            System.out.print(ANSI_YELLOW + "Digite: " + ANSI_CLEAR );
            var tipoAutomovel = scanner.nextLine();

            if (tipoAutomovel.toLowerCase().contains("carr")) {
                endereco = URL_BASE + "carros/marcas";
                break;
            } else if (tipoAutomovel.toLowerCase().contains("mot")) {
                endereco = URL_BASE + "motos/marcas";
                break;
            } else if (tipoAutomovel.toLowerCase().contains("caminh")) {
                endereco = URL_BASE + "caminhoes/marcas";
                break;
            } else {
                System.out.println("Opção inválida. Digite novamente!");
            }
        }
        var json = consumo.obterDados(endereco);
        var marcas = conversor.obterLista(json, DadosVeiculo.class);

        System.out.println(ANSI_GREEN + "------VEÍCULOS------\n" + ANSI_CLEAR);
        marcas.stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);

        System.out.println(ANSI_YELLOW + "\nDigite o código da marca que deseja: " + ANSI_CLEAR);
        var codigo = scanner.nextLine();
        endereco = endereco + "/" + codigo.replace(" ", "") + "/modelos";
        json = consumo.obterDados(endereco);
        var modelosLista = conversor.obterDados(json, DadosModelo.class); //o modelo já está representado como uma lista, então uso o obter dados
        System.out.println(ANSI_GREEN + "---------MODELOS DA MARCA---------\n" + ANSI_CLEAR);

        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(DadosVeiculo::codigo))
                .forEach(System.out::println);

        System.out.println(ANSI_YELLOW + "\nDigite o modelo que deseja pesquisar: " + ANSI_CLEAR);
        var nomeModelo = scanner.nextLine();

        List<DadosVeiculo> modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeModelo.toLowerCase()))
                .collect(Collectors.toList());


        System.out.println(ANSI_GREEN + "\n---------MODELOS FILTRADOS---------\n" + ANSI_CLEAR);
        modelosFiltrados.forEach(System.out::println);

        System.out.println(ANSI_YELLOW + "\nDigite o código do modelo para buscar os valores: " + ANSI_CLEAR);
        var codigoModelo = scanner.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);

        List<DadosVeiculo> anos = conversor.obterLista(json, DadosVeiculo.class);

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println(ANSI_GREEN + "\n----------- VEÍCULOS ENCONTRADO: -----------\n" + ANSI_CLEAR);
        veiculos.forEach(System.out::println);



    }
}
