package br.com.faac;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Verificar {
    // Lista:
    private Lista dicionarioArqUm, dicionarioArqDois;
    private String arquivoTeste, localArqSaida;

    public void analisar(String localA, String localB, String localC) throws FileNotFoundException, IOException {
        this.dicionarioArqUm = new Lista();
        this.dicionarioArqDois = new Lista();
        this.arquivoTeste = localA;
        this.localArqSaida = localC;
        this.analisar(localA, dicionarioArqUm);
        this.analisar(localB, dicionarioArqDois);
    }

    private void analisar(String local, Lista dicionario) throws FileNotFoundException, IOException {
        // Arquivo:
        File arquivo = new File(local);
        // Verifica se é um arquivo:
        if (arquivo.isFile()) {
            // Montar um dicionário:
            this.montarDicionario(local, dicionario);
            // Verificar conteúdo:
            dicionario.setEstruturas(this.analisarConteudo(local));
            // Inicia a verificação:
            if (dicionario.equals(this.dicionarioArqDois)) {
                this.verificarIgualdade(local);
            }
        } else {
            if (dicionario == this.dicionarioArqUm) {
                throw new IllegalArgumentException("Passe um arquivo.");
            }
            // Vasculha por novos arquivos no diretório:
            String[] arquivos = arquivo.list();
            for (String file : arquivos) {
                this.analisar(local + "\\" + file, dicionario);
            }
        }
    }

    private void montarDicionario(String local, Lista dicionario) throws IOException {
        try (FileReader arq = new FileReader(local)) {
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            while (linha != null) {
                linha = this.preRemocaoParen(linha);
                this.separarPalavras(linha, dicionario);
                linha = lerArq.readLine();
            }
            arq.close();
        }
    }

    private void separarPalavras(String linha, Lista dicionario) {
        String[] palavras = linha.split(" ");
        for (String palavra : palavras) {
            Palavra novaPalvara = new Palavra(palavra);
            dicionario.adicionar(novaPalvara);
        }
    }

    private String prepararRemocao(String linha) {
        linha = linha.replace("+", " ");
        linha = linha.replace("-", " ");
        linha = linha.replace("*", " ");
        linha = linha.replace("/", " ");
        linha = linha.replace("%", " ");
        linha = linha.replace("=", " ");
        linha = linha.replace(",", " ");
        linha = linha.replace("!", " ");
        linha = linha.replace("\\", " ");
        linha = linha.replace("&", " ");
        linha = linha.replace("|", " ");
        linha = linha.replace("<", " ");
        linha = linha.replace(">", " ");
        linha = linha.replace("^", " ");
        linha = linha.replace("$", " ");
        linha = linha.replace("#", " ");
        linha = linha.replace("@", " ");
        linha = linha.replace("~", " ");
        linha = linha.replace("`", " ");
        linha = linha.replace("'", " ");
        linha = linha.replace("_", " ");
        linha = linha.replace("?", " ");
        linha = linha.replace(":", " ");
        linha = linha.replace("\"", " ");
        linha = linha.replace("[", " ");
        linha = linha.replace("]", " ");
        linha = linha.replace(".", " ");
        linha = linha.replace(";", " ");
        linha = linha.replace("{", " ");
        linha = linha.replace("}", " ");
        return linha;
    }
    
    private String preRemocaoParen(String linha){
        linha = linha.replace("(", " ");
        linha = linha.replace(")", " ");
        linha = this.prepararRemocao(linha);
        return linha;
    }

    private void verificarIgualdade(String arqVerificacao) throws IOException {
        // Sobre o arquivo:
        File arquivo;
        String conteudo;
        arquivo = new File(this.arquivoTeste);
        conteudo = "Nome do arquivo principal: " + arquivo.getName();
        arquivo = new File(arqVerificacao);
        conteudo += System.lineSeparator() + "Nome do arquivo de verificação: " + arquivo.getName();
        int quantidade = this.dicionarioArqUm.quantidadeReservada(dicionarioArqDois);
        // Palavaras similares:
        conteudo += System.lineSeparator() + System.lineSeparator() + "Similaridade - Palavras reservadas";
        conteudo += System.lineSeparator() + "Quantidade de palavras similares: " + quantidade;
        int porcenPala = this.dicionarioArqUm.porcentagemPalavras(quantidade);
        conteudo += System.lineSeparator() + "Porcentagem: " + porcenPala + "%";
        // Estruturas similares:
        conteudo += System.lineSeparator() + System.lineSeparator() + "Similaridade - Estrutura";
        quantidade = 
                this.dicionarioArqUm.qtdEstruturas(dicionarioArqDois);
        conteudo += System.lineSeparator() + "Possui " + quantidade + 
                " método(s) com estruturas semelhantes";
        int pocenEstru = this.dicionarioArqUm.pocentagemEstrutura(dicionarioArqDois);
        conteudo += "%nPorcentagem: " + pocenEstru + "%";
        int resultado = (int) (((porcenPala * 0.1) + (pocenEstru * 0.5))/ 6);
        conteudo += System.lineSeparator() + System.lineSeparator() + "Média de similaridade: " +
                 resultado + "%";
        conteudo += System.lineSeparator() + System.lineSeparator() +
                "---------- / ----------" +
                System.lineSeparator() + System.lineSeparator();
        this.escrever(this.localArqSaida + "\\resultado.txt", conteudo);
        this.dicionarioArqDois.limpar();
    }
    
    private String analisarConteudo(String local) throws IOException{
        boolean isMetodo = false;
        String estruturaComposta = "";
        try (FileReader arq = new FileReader(local)) {
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            while (linha != null) {
                if(this.comeco(linha)){
                    if(isMetodo){
                        estruturaComposta += ";";
                    }
                    isMetodo = true;
                }
                
                if(isMetodo){
                    if(this.comecoEstru(linha) != ' ')
                        estruturaComposta += this.comecoEstru(linha);
                }
                linha = lerArq.readLine();
            }
            estruturaComposta += ";";
            arq.close();
        }
        estruturaComposta = estruturaComposta.trim();
        return estruturaComposta;
    }
    
    private char comecoEstru(String linha){
        linha = this.preRemocaoParen(linha);
        String[] palavras = linha.split(" ");
        for(String palavra : palavras){
            palavra = palavra.trim();
            switch (palavra) {
                case "while":
                    return 'W';
                case "if":
                    return 'I';
                case "else":
                    return 'E';
                case "for":
                    return 'F';
                case "do":
                    return 'D';
                case "switch":
                    return 'S';
            }
            
        }
        return '-';
    }
    
    private boolean comeco(String linha){
        String palavras[] = linha.split(" ");
        for(String palavra : palavras){
            if(this.possuiComeco(palavra)){
                char[] letras = linha.toCharArray();
                int cont = 0;
                for(char letra : letras){
                    if(letra == '(' || letra == ')')
                        cont++;
                    
                    if(cont == 2)
                        return true;
                }
            }
        }
        return false;
    }
    
    private boolean possuiComeco(String palavra){
        return palavra.equals("public") ||
               palavra.endsWith("private") ||
               palavra.equals("protected");
    }
    
    private void escrever(String path, String conteudo){
        try {
            // O parametro é que indica se deve sobrescrever ou continua no
            // arquivo.
            FileWriter fw = new FileWriter(path, true);
            try (BufferedWriter conexao = new BufferedWriter(fw)) {
                conexao.write(conteudo);
                conexao.newLine();
            }
        } catch (Exception e) {
        }
    }
}