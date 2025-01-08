import java.sql.*;

public class Teste_Conexao {
    public static void main(String[] args) {
        // Dados da conexão com o banco de dados
        String url = "jdbc:mysql://localhost:3306/gerenciamento_de_alunos"; // Caso queira visualizar a existencia de outra tabela em outro banco de dados, substitua "Controle_estoque pelo seu banco de dados"
        String usuario = "root"; // Usuario
        String senha = "12345"; // Senha

        System.out.println("Conectando ao banco de dados e listando tabelas..."); //Carregamento

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); //processo de conexão com as variaveis
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) { //Comando de puxar as tabelas existentes

            System.out.println("Tabelas existentes no banco de dados:");
            while (rs.next()) {
                String tabela = rs.getString(1); // Obtém o nome da tabela
                System.out.println("- " + tabela);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ou listar tabelas: " + e.getMessage());
        }
    }
}