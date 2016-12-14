package br.com.faac;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Lista {
    private Palavra[] dicionario = new Palavra[10];
    private final String[] palavrasReservadas;
    private int ultimo = 0;
    private String estruturas;
    
    public Lista() throws IOException{
        this.palavrasReservadas = new String[49];
        this.reservadas();
    }
    
    public void setEstruturas(String estruturas){
        this.estruturas = estruturas;
    }
    
    public int qtdEstruturas(Lista lista){
        String[] lista1 = this.estruturas.split(";");
        String[] lista2 = lista.estruturas.split(";");
        int quantidade = 0;
        for(String p1 : lista1){
            for(String p2 : lista2){
                if(p1.equals(p2)){
                        quantidade++;
                }
            }
        }
        return quantidade;
    }
    
    public int pocentagemEstrutura(Lista lista){
        String[] lista1 = this.estruturas.split(";");
        return (100 * this.qtdEstruturas(lista)) / lista1.length;
    }
    
    private void reservadas() throws IOException{
        InputStream file;
        file = getClass().getClassLoader().getResourceAsStream("Documento/Reservadas.txt");
        Scanner s = new Scanner(file);
        int i = 0;
        while(s.hasNext()){
            this.palavrasReservadas[i] =  s.next();
            i++;
        }
        /*BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            int i = 0;
            while (linha != null) {
                this.palavrasReservadas[i] = linha;
                i++;
                linha = lerArq.readLine();
            }
            arq.close();*/
    }
    
    public String listar(){
        String retorno = "";
        for(int i = 0; i < this.ultimo; i++){
            retorno += "Palavra: " + dicionario[i].getPalavra() + 
                    "\nQuantidade: " + dicionario[i].getQuantidade() + "\n\n";
        }
        retorno += "Quantidade de palavras: " + this.ultimo;
        return retorno;
    }
    
    private boolean possui(Palavra palavra){
        for(int i = 0; i < this.ultimo; i++){
            if(dicionario[i].igual(palavra)){
                return true;
            }
        }
        return false;
    }
    
    private int posicao(Palavra palavra){
        for(int i = 0; i < this.ultimo; i++){
            if(dicionario[i].igual(palavra)){
                return i;
            }
        }
        return -1;
    }
    
    private void garanteEspaco(){
        if(this.ultimo == this.dicionario.length){
            Palavra[] aux = new Palavra[this.dicionario.length * 2];
            System.arraycopy(dicionario, 0, aux, 0, dicionario.length);
            dicionario = aux;
        }
    }
    
    public void adicionar(Palavra palavra){
        this.garanteEspaco();
        
        if(this.ehReservada(palavra.getPalavra())){
            if(!possui(palavra)){
                dicionario[this.ultimo] = palavra;
                dicionario[this.ultimo].setQuantidade();
                this.ultimo++;
            }
            else{
                dicionario[this.posicao(palavra)].setQuantidade();
            }
        }
    }
    
    private boolean ehReservada(String palavra){
        for (String palavraReservada : this.palavrasReservadas) {
            if(palavra.equals(palavraReservada)){
                return true;
            }
        }
        return false;
    }
    
    public int quantidadeReservada(Lista lista){
        int quantidade = 0;
        for(int j = 0; j < this.ultimo; j++){
            for(int i = 0; i < lista.ultimo; i++){
                if(this.dicionario[j].igual(lista.dicionario[i])){
                    if(this.dicionario[j].getQuantidade() == lista.dicionario[i].getQuantidade()){
                        quantidade++;   
                    }
                }
            }
        }
        return quantidade;
    }
    
    public int porcentagemPalavras(int quantidade){
        return (quantidade * 100) / this.ultimo;
    }
    
    public void limpar(){
        this.dicionario = new Palavra[10];
        this.ultimo = 0;
    }
}
