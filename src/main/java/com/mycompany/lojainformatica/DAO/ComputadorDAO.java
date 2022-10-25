/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lojainformatica.DAO;
import com.mycompany.lojainformatica.model.Computador;
import com.mycompany.lojainformatica.utils.GerenciadorConexao;
import static com.mycompany.lojainformatica.utils.GerenciadorConexao.abrirConexao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Cleiton
 */
public class ComputadorDAO {
    
   
     public static boolean salvar(Computador p)
    {
        boolean retorno = false;
        Connection conexao = null ;
        PreparedStatement instrucaoSQL = null;
        try {
            conexao = abrirConexao();

            instrucaoSQL = conexao.prepareStatement("INSERT INTO computador (marca,hd,processador) VALUES(?,?,?)"
                                                        , Statement.RETURN_GENERATED_KEYS);
            instrucaoSQL.setString(1, p.getMarca());
            instrucaoSQL.setString(2, p.getHd());
            instrucaoSQL.setString(3, p.getProcessador());    
            int linhasRetorno = instrucaoSQL.executeUpdate();
        
            if(linhasRetorno>0)
            {
                retorno = true;
                
                ResultSet generatedKeys = instrucaoSQL.getGeneratedKeys(); //Recupero o ID do cliente
                if (generatedKeys.next()) {
                        p.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Falha ao obter o ID do cliente.");
                }
            }
            else{
                retorno = false;
            }   
        } catch (SQLException | ClassNotFoundException ex) {
            retorno = false;
            JOptionPane.showMessageDialog(null, ex);
        }
        finally{
             try {
                if(instrucaoSQL!=null)
                    instrucaoSQL.close();
                conexao.close();
              } catch (SQLException ex) {
             }
        }
       
        return retorno;
    }
     
    public static boolean editar(Computador p)
    {
        boolean retorno = false;
        Connection conexao = null;
        PreparedStatement instrucaoSQL = null;
        
        try {
            conexao = abrirConexao();
            instrucaoSQL = conexao.prepareStatement("UPDATE computador SET hd = ?, processador=? WHERE id =? ");
            
            //Adiciono os parâmetros ao meu comando SQL
            instrucaoSQL.setString(1, p.getHd());
            instrucaoSQL.setString(2, p.getProcessador());
            instrucaoSQL.setInt(3, p.getId());
            
            //Mando executar a instrução SQL
            int linhasAfetadas = instrucaoSQL.executeUpdate();
            
            if(linhasAfetadas>0)
            {
                retorno = true;
            }
            else{
                retorno = false;
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            retorno = false;
        } finally{
            
            //Libero os recursos da memória
            try {
                if(instrucaoSQL!=null)
                    instrucaoSQL.close();
                
                //GerenciadorConexao.fecharConexao();
                conexao.close();
                
              } catch (SQLException ex) {
             }
        }
        
        return retorno;
    } 
    
    public static boolean deletar(Computador p)
    {
        boolean retorno = false;
        Connection conexao = null;
        PreparedStatement instrucaoSQL = null;
        
        try {
            conexao = abrirConexao();
            instrucaoSQL = conexao.prepareStatement("DELETE FROM computador WHERE id = ?");
            
            //Adiciono os parâmetros ao meu comando SQL
            instrucaoSQL.setInt(1, p.getId());
            
            //Mando executar a instrução SQL
            int linhasAfetadas = instrucaoSQL.executeUpdate();
            
            if(linhasAfetadas>0)
            {
                retorno = true;
            }
            else{
                retorno = false;
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            retorno = false;
        } finally{
            
            //Libero os recursos da memória
            try {
                if(instrucaoSQL!=null)
                    instrucaoSQL.close();
                
                //GerenciadorConexao.fecharConexao();
                conexao.close();
                
              } catch (SQLException ex) {
             }
        }
        
        return retorno;
    } 
    
    public static ArrayList<Computador> listaComputadors()          
    {
        ResultSet rs = null;
        Connection conexao = null;
        PreparedStatement instrucaoSQL = null; 
        
        ArrayList<Computador> listaComputadores = new ArrayList<Computador>();
        
        try {
            
            conexao = abrirConexao();
                    
            instrucaoSQL = conexao.prepareStatement("SELECT * FROM computador;");

            rs = instrucaoSQL.executeQuery();
            
            //Percorrer o resultSet
            while(rs.next())
            {
                Computador c = new Computador();
                c.setId(rs.getInt("id"));
                c.setHd(rs.getString("Hd"));
                c.setProcessador(rs.getString("processador"));
                
                //Adiciono na listaClientes
                listaComputadores.add(c);
            }
            
        }catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            listaComputadores = null;
        } finally{
            
            try {
                if(rs!=null)
                    rs.close();                
                if(instrucaoSQL!=null)
                    instrucaoSQL.close();
                conexao.close();
                
                        
              } catch (SQLException ex) {
             }
        }
        
        return listaComputadores;
    } 
    
     public static ArrayList<Computador> consultarCamputadorFiltro(String pProcessador)
    {
        ResultSet rs = null;
        Connection conexao ;
        PreparedStatement instrucaoSQL = null; 
        
        ArrayList<Computador> listaComputadores = new ArrayList<>();
        
        try {
            
            conexao = GerenciadorConexao.abrirConexao();
            instrucaoSQL = conexao.prepareStatement("SELECT * FROM computador WHERE processador LIKE ?;");
            
            //Adiciono os parâmetros ao meu comando SQL
            instrucaoSQL.setString(1,"%" + pProcessador + '%' );

            rs = instrucaoSQL.executeQuery();
            
            while(rs.next())
            {
                Computador c = new Computador();
                c.setId(rs.getInt("id"));
                c.setHd(rs.getString("Hd"));
                c.setProcessador(rs.getString("processador"));
                
                //Adiciono na listaClientes
                listaComputadores.add(c);
            }
            
        }catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            listaComputadores = null;
        } finally{
            //Libero os recursos da memória
            try {
                if(rs!=null)
                    rs.close();                
                if(instrucaoSQL!=null)
                    instrucaoSQL.close();
                
                GerenciadorConexao.fecharConexao();
                        
              } catch (SQLException ex) {
             }
        }
        
        return listaComputadores;
    }
      
}
