/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;
import modelos.*;
import java.sql.Connection;
import java.util.ArrayList;
import bancodedados.ConexaoSGDB;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
/**
 *
 * @author ejmcc
 */
public class ContatoDAO implements IcrudContato{
  //Atributos
  private Connection conexao = null;
    private String nomeBusca;
  //Metodo Construtor
  public ContatoDAO()throws Exception{
    conexao = ConexaoSGDB.getConexao();
  }
 
  //Metodos Sobrecarregados
  @Override
  public void incluir(Contato objeto) throws Exception {
    try{
      // O SQL de inserção
      String sql = "INSERT INTO contatos(nomeCompleto, email, ddi, ddd, numero) values (?, ?, ?, ?, ?)";
      // 2. Preparando a query - Comando a ser enviado para o SGBD
      PreparedStatement preparedStatement = conexao.prepareStatement(sql);
      // Parameters iniciar os elementos
      preparedStatement.setString(1, objeto.getNomeCompleto());
      preparedStatement.setString(2, objeto.getEmail());
      preparedStatement.setInt(3, objeto.getTelefone().getDdi());
      preparedStatement.setInt(4, objeto.getTelefone().getDdd());
      preparedStatement.setInt(5, objeto.getTelefone().getNumero());
      preparedStatement.executeUpdate();
    }catch (SQLException erro) {
      //Erro do comando SQL - chave, coluna, nome da tabela, ...
      throw new Exception("Metodo Incluir Contato - Erro de SQL: "+ erro.getMessage());
    }    
    catch(Exception erro){
      String msg = "Metodo Incluir Contato - "+erro.getMessage();
      throw new Exception(msg);
    }  
  }
@Override
  public ArrayList<Contato> listagemDeContatos() throws Exception {
    try {
            ArrayList<Contato> lista = new ArrayList<>();
            String sql = "SELECT * FROM contatos ORDER BY idcontato";
            
            Statement statement = conexao.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            
            while (rs.next()) {
                lista.add(extrairContatoDoResultSet(rs));
            }
            return lista;
            
        } catch (SQLException erro) {
            throw new Exception("Erro SQL (Listagem): " + erro.getMessage());
        }    
  }
  
  @Override
  public void excluir(int idContato) throws Exception {  
    try{
      // O SQL de exclusao de dados
      String sql = "delete from contatos where idcontato=?";
      PreparedStatement preparedStatement = conexao.prepareStatement(sql);
      // Parameters start with 1
      preparedStatement.setInt(1, idContato);
      preparedStatement.executeUpdate();
    }catch (SQLException erro) {
      //Erro do comando SQL - chave, coluna, nome da tabela, ...
      throw new Exception("Metodo Excluir Contato - Erro de SQL: "+ erro.getMessage());
    }    
    catch(Exception erro){
      String msg = "Metodo Excluir Contato - "+erro.getMessage();
      throw new Exception(msg);
    } 
  }

  @Override
  public void alterar(Contato objeto) throws Exception {  
    try {
      // O SQL de exclusao de dados
      String sql = "update  contatos set nomeCompleto=?,email=?,ddi=?,ddd=?,numero=? where idContato=?";
      PreparedStatement preparedStatement = conexao.prepareStatement(sql);
      // Parameters start with 1
      preparedStatement.setString(1, objeto.getNomeCompleto());
      preparedStatement.setString(2, objeto.getEmail());
      preparedStatement.setInt(3, objeto.getTelefone().getDdi());
      preparedStatement.setInt(4, objeto.getTelefone().getDdd());
      preparedStatement.setInt(5, objeto.getTelefone().getNumero());
      preparedStatement.setInt(6, objeto.getIdContato());
      preparedStatement.executeUpdate();
    }catch (SQLException erro) {
      //Erro do comando SQL - chave, coluna, nome da tabela, ...
      throw new Exception("Metodo Alterar Contato - Erro de SQL: "+ erro.getMessage());
    }    
    catch(Exception erro){
      String msg = "Metodo Alterar Contato - "+erro.getMessage();
      throw new Exception(msg);
    } 
  }

  @Override
  public Contato consultar(String nomeCompleto) throws Exception {
    try {
            // Busca pelo nome
            String sql = "SELECT * FROM contatos WHERE nomecompleto = ?"; // Use ILIKE se quiser ignorar maiusculas/minusculas
            PreparedStatement preparedStatement = conexao.prepareStatement(sql);
            preparedStatement.setString(1, nomeCompleto);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                return extrairContatoDoResultSet(rs);
            }
            return null;
            
        } catch (SQLException erro) {
            throw new Exception("Erro SQL (Consultar): " + erro.getMessage());
        }
  }

  

  @Override
  public Contato buscarID(int idContato) throws Exception {
    try {
            String sql = "SELECT * FROM contatos WHERE idcontato = ?";
            PreparedStatement preparedStatement = conexao.prepareStatement(sql);
            preparedStatement.setInt(1, idContato);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                return extrairContatoDoResultSet(rs);
            }
            return null;
            
        } catch (SQLException erro) {
            throw new Exception("Erro SQL (BuscarID): " + erro.getMessage());
        }     
  }
  //Metodo para evitar codigo de leitura repetido
  private Contato extrairContatoDoResultSet(ResultSet rs) throws SQLException {
        Contato pessoa = new Contato();
        pessoa.setIdContato(rs.getInt("idcontato"));
        pessoa.setNomeCompleto(rs.getString("nomecompleto"));
        pessoa.setEmail(rs.getString("email"));
        
        // Tratamento para evitar erro se o telefone estiver NULL no banco
        int ddi = rs.getInt("ddi");
        int ddd = rs.getInt("ddd");
        int numero = rs.getInt("numero");
        
        // Se o banco retornou 0 (default do JDBC para null int), criamos zerado
        pessoa.setTelefone(new Telefone(ddi, ddd, numero));
        
        return pessoa;
    }
}
