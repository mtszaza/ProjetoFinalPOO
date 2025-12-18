/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controle;
import java.util.ArrayList;
import modelos.*;
import persistencia.ContatoDAO;

public class ContatoCTL implements IcrudContato{
  ContatoDAO persistenciaContatoDAO = null;

  public ContatoCTL() {
    try{
        persistenciaContatoDAO = new ContatoDAO();
    } catch (Exception error){
        System.out.println("Erro ao conectar com o Banco de Dados" + error.getMessage());
    }
  }
  
  private void verificarDadosContato(Contato objeto) throws Exception{
    String resposta = "";
    if(objeto.getIdContato() <= 0) 
      resposta += "\nValor do identificador não pode ser <= 0";
    if(objeto.getNomeCompleto().isEmpty())
      resposta += "\nNome esta vazio";
    if(objeto.getEmail().isEmpty())
      resposta += "\nEmail esta vazio";
    else {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; // Regra de formato
        if (!objeto.getEmail().matches(regex)) {
            resposta += "\nE-mail inválido! Use o formato: nome@exemplo.com";
        }
    }
    if (objeto.getTelefone().getDdi() <= 0) 
        resposta += "\nDDI inválido";
    if (objeto.getTelefone().getDdd() <= 0) 
        resposta += "\nDDD inválido";
    if (objeto.getTelefone().getNumero() <= 0) 
        resposta += "\nNúmero de telefone inválido";
    if(!resposta.isEmpty()) throw new Exception(resposta);
  }

  @Override
  public void incluir(Contato objeto) throws Exception {
      verificarDadosContato(objeto);
      
      if(buscarID(objeto.getIdContato()) != null) {
          throw new Exception("Já existe um contato com esse ID!");
       
      }
      persistenciaContatoDAO.incluir(objeto);
  }

  @Override
  public void excluir(int idContato) throws Exception {
    if(buscarID(idContato) == null){
        throw new Exception ("Naõ foi possível excluir: Contato não existe!");
    }
    persistenciaContatoDAO.excluir(idContato);
  }

  @Override
  public void alterar(Contato objeto) throws Exception {
      verificarDadosContato(objeto);
      if(buscarID(objeto.getIdContato()) == null){
          throw new Exception("Não é possível alterar o contato: Não foi encontrado!");
      }
      persistenciaContatoDAO.alterar(objeto);
  }

  @Override
  public Contato consultar(String nomeCompleto) throws Exception {
    return persistenciaContatoDAO.consultar(nomeCompleto);
  }

  @Override
  public Contato buscarID(int idContato) throws Exception {
    return persistenciaContatoDAO.buscarID(idContato);  
  }

  @Override
  public ArrayList<Contato> listagemDeContatos() throws Exception {
    return persistenciaContatoDAO.listagemDeContatos();
  }
    
    
}
