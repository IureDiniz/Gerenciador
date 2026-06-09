/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import dao.EquipamentoDAO;
/**
 *
 * @author Estudante
 */
public class Equipamento {
    private int id;
    private String nome;
    private String descricao;
    private int quantidade;
    private int categoria_id;
    private String localizacao;
    private String status;
    
    //construtor
    public Equipamento(int id, String nome, String descricao, int quantidade, int categoria_id, String localizacao, String status){
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.categoria_id = categoria_id;
        this.localizacao = localizacao;
        this.status = status;
    }
    
    //verifica se o equipamento do ID fornecido existe
    public static boolean existe_equipamento(int id){
        if (EquipamentoDAO.getEquipamento(id) != null){
            return true;
        }else{
            return false;
        }
    }

    //método toString caso seja necesário apresentar os dados como uma String
    @Override
    public String toString(){
        String m = "ID: " + this.id + "\n"
                + "Nome: " + this.nome + "\n"
                + "Descrição: " + this.descricao + "\n"
                + "Quantidade: " + this.quantidade + "\n" 
                + "ID categoria: " + this.categoria_id + "\n"
                + "Localização: " + this.localizacao + "\n"
                + "Status: " + this.status + "\n";
        return m;
    }
    
    //getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(int categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
