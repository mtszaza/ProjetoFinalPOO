/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancodedados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mathe
 */
public class ConexaoSGDB {
    //Atributos
  //A conexão com o banco
  private static Connection conexao = null;
  // Configurações do Banco (Ajuste seu usuário e senha aqui)
  // Driver do banco de dados
  private static String driver = "org.postgresql.Driver";
  // Dados do banco de dados - local do banco - porta - nome do Banco
  private static String URL = "jdbc:postgresql://127.0.0.1:5432/AgendaContatosSGBD";
  // Seu usuário do banco
  private static String USER = "postgres";
  // Sua senha do banco
  private static String PASSWORD = "123456"; 
  //Metodos

  public ConexaoSGDB() {}
  
  public static Connection getConexao() throws Exception{
    try {
      if(conexao == null){
        //Carrega o driver do PostgreSQL
        Class.forName(driver);
        //Cria a conexão com o SGBD
        conexao = DriverManager.getConnection(URL, USER, PASSWORD);
      }
      return conexao;
    }
    catch(ClassNotFoundException erro){
      //Erro de não encontrar o drive do banco no projeto
      throw new Exception("Drive: "+erro.getMessage());
    }
    catch(SQLException erro){
      //Erro no banco de dados: usuario, senha ou banco de dados 
      throw new Exception("Banco: " + erro.getMessage());
    }
  }
}
