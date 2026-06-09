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
    
    public static boolean existe_equipamento(int id){
        if(EquipamentoDAO.getEquipamento(id) != null){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean possui_quantidade(int idEquipamento, int qtde){
        Equipamento equipamento = EquipamentoDAO.getEquipamento(idEquipamento);
    }
}
