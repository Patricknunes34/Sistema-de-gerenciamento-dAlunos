import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class Gerenciamentodealunos extends JFrame {
    private JLabel lblTurma, lblNome, lblDataNascimento, lblNP1, lblNP2, lblTotal;
    private JTextField txtTurma, txtNome, txtDataNascimento, txtNP1, txtNP2, txtTotal;
    private JButton btnSair, btnVisualizar, btnExcluir, btnSalvar, btnEditar;

    public Gerenciamentodealunos() {
        super("Gerenciamento de Alunos");

        // Inicializando componentes
        lblTurma = new JLabel("Turma:");
        lblNome = new JLabel("Nome:");
        lblDataNascimento = new JLabel("Data de Nascimento:");
        lblNP1 = new JLabel("Nota NP1:");
        lblNP2 = new JLabel("Nota NP2:");
        lblTotal = new JLabel("Total:");

        txtTurma = new JTextField(15);
        txtNome = new JTextField(15);
        txtDataNascimento = new JTextField(15);
        txtNP1 = new JTextField(15);
        txtNP2 = new JTextField(15);
        txtTotal = new JTextField(15);
        txtTotal.setEditable(false);

        btnSair = new JButton("Sair");
        btnVisualizar = new JButton("Visualizar");
        btnExcluir = new JButton("Excluir");
        btnSalvar = new JButton("Salvar");
        btnEditar = new JButton("Editar");

        // Configurando o layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponent(lblTurma, txtTurma, gbc, 0);
        addComponent(lblNome, txtNome, gbc, 1);
        addComponent(lblDataNascimento, txtDataNascimento, gbc, 2);
        addComponent(lblNP1, txtNP1, gbc, 3);
        addComponent(lblNP2, txtNP2, gbc, 4);
        addComponent(lblTotal, txtTotal, gbc, 5);

        // Botões
        gbc.gridy = 6;
        gbc.gridx = 0;
        add(btnSalvar, gbc);
        gbc.gridx = 1;
        add(btnExcluir, gbc);
        gbc.gridx = 2;
        add(btnVisualizar, gbc);
        gbc.gridx = 3;
        add(btnEditar, gbc);
        gbc.gridx = 4;
        add(btnSair, gbc);

        // Adicionando ações aos botões
        btnSalvar.addActionListener(e -> salvarDados());
        btnVisualizar.addActionListener(e -> visualizarTabela());
        btnEditar.addActionListener(e -> editarAluno());
        btnSair.addActionListener(e -> System.exit(0));
        btnExcluir.addActionListener(e -> excluirLinha());

        // Listener para calcular o Total automaticamente
        KeyAdapter recalculaTotal = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTotal();
            }
        };
        txtNP1.addKeyListener(recalculaTotal);
        txtNP2.addKeyListener(recalculaTotal);

        // Configurações da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addComponent(JLabel label, JTextField textField, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(label, gbc);
        gbc.gridx = 1;
        add(textField, gbc);
    }

    private void calcularTotal() {
        try {
            double np1 = Double.parseDouble(txtNP1.getText());
            double np2 = Double.parseDouble(txtNP2.getText());
            double resultado = (np1 + np2) / 2;
            txtTotal.setText(String.format("%.2f", resultado));
        } catch (NumberFormatException e) {
            txtTotal.setText(""); // Limpa o campo se houver entrada inválida
        }
    }

    private void salvarDados() {
        String turma = sanitizeInput(txtTurma.getText());
        String nome = txtNome.getText();
        String dataNascimento = txtDataNascimento.getText();
        try {
            double np1 = Double.parseDouble(txtNP1.getText());
            double np2 = Double.parseDouble(txtNP2.getText());
            double total = (np1 + np2) / 2;

            String criarTabelaSQL = "CREATE TABLE IF NOT EXISTS `" + turma + "` (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "Nome VARCHAR(30) NOT NULL, " +
                    "DataNascimento DATE NOT NULL, " +
                    "NP1 DOUBLE NOT NULL, " +
                    "NP2 DOUBLE NOT NULL, " +
                    "Total DOUBLE NOT NULL);";

            String inserirDadosSQL = "INSERT INTO `" + turma + "` (Nome, DataNascimento, NP1, NP2, Total) VALUES (?, ?, ?, ?, ?)";

            try (Connection con = getConnection();
                 Statement stmt = con.createStatement();
                 PreparedStatement pstmt = con.prepareStatement(inserirDadosSQL)) {
                stmt.executeUpdate(criarTabelaSQL);
                pstmt.setString(1, nome);
                pstmt.setString(2, dataNascimento);
                pstmt.setDouble(3, np1);
                pstmt.setDouble(4, np2);
                pstmt.setDouble(5, total);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
                limparCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Notas devem ser números válidos!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + e.getMessage());
        }
    }

    private void visualizarTabela() {
        String turma = sanitizeInput(txtTurma.getText());
        if (turma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o nome da turma!");
            return;
        }

        String sql = "SELECT * FROM `" + turma + "`";
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            StringBuilder sb = new StringBuilder("\nTabela: " + turma + "\n");
            sb.append("------------------------------------------------------------------------------------------------------+\n");
            sb.append(String.format("%-5s %-20s %-15s %-10s %-10s %-10s\n", "ID", "Nome", "Data Nasc.", "NP1", "NP2", "Total"));
            sb.append("------------------------------------------------------------------------------------------------------+\n");
            while (rs.next()) {
                sb.append(String.format("%-5d %-20s %-15s %-10.2f %-10.2f %-10.2f\n",
                        rs.getInt("ID"), rs.getString("Nome"), rs.getString("DataNascimento"),
                        rs.getDouble("NP1"), rs.getDouble("NP2"), rs.getDouble("Total")));
            }
            sb.append("------------------------------------------------------------------------------------------------------+\n");

            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao exibir tabela: " + e.getMessage());
        }
    }

    private void editarAluno() {
        String turma = sanitizeInput(txtTurma.getText());
        if (turma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o nome da turma!");
            return;
        }

        String idText = JOptionPane.showInputDialog(this, "Informe o ID do aluno para editar:");
        if (idText == null || idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID não pode ser vazio!");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            // Obtendo novos dados do usuário
            String novoNome = JOptionPane.showInputDialog(this, "Novo Nome:");
            String novaDataNascimento = JOptionPane.showInputDialog(this, "Nova Data de Nascimento (yyyy-MM-dd):");
            String novoNP1 = JOptionPane.showInputDialog(this, "Nova Nota NP1:");
            String novoNP2 = JOptionPane.showInputDialog(this, "Nova Nota NP2:");

            if (novoNome == null || novaDataNascimento == null || novoNP1 == null || novoNP2 == null) {
                JOptionPane.showMessageDialog(this, "Edição cancelada ou valores inválidos!");
                return;
            }

            // Calculando o novo total
            double np1 = Double.parseDouble(novoNP1);
            double np2 = Double.parseDouble(novoNP2);
            double novoTotal = (np1 + np2) / 2;

            // Atualizando os dados no banco de dados
            String atualizarSQL = "UPDATE `" + turma + "` SET Nome = ?, DataNascimento = ?, NP1 = ?, NP2 = ?, Total = ? WHERE ID = ?";

            try (Connection con = getConnection();
                 PreparedStatement pstmt = con.prepareStatement(atualizarSQL)) {

                pstmt.setString(1, novoNome);
                pstmt.setString(2, novaDataNascimento);
                pstmt.setDouble(3, np1);
                pstmt.setDouble(4, np2);
                pstmt.setDouble(5, novoTotal);
                pstmt.setInt(6, id);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum registro encontrado com o ID informado.");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Notas ou ID inválidos!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar os dados: " + e.getMessage());
        }
    }


    private void excluirLinha() {
        String turma = sanitizeInput(txtTurma.getText());
        if (turma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o nome da turma!");
            return;
        }

        String idText = JOptionPane.showInputDialog(this, "Informe o ID do aluno para excluir:");
        if (idText == null || idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID não pode ser vazio!");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            String excluirSQL = "DELETE FROM `" + turma + "` WHERE ID = ?";

            try (Connection con = getConnection();
                 PreparedStatement pstmt = con.prepareStatement(excluirSQL)) {

                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Registro excluído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum registro encontrado com o ID informado.");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido! Insira um número inteiro.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir o registro: " + e.getMessage());
        }
    }


    private String sanitizeInput(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "_");
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/gerenciamento_de_alunos";
        String usuario = "root";
        String senha = "12345";
        return DriverManager.getConnection(url, usuario, senha);
    }

    private void limparCampos() {
        txtTurma.setText("");
        txtNome.setText("");
        txtDataNascimento.setText("");
        txtNP1.setText("");
        txtNP2.setText("");
        txtTotal.setText("");
    }

    public static void main(String[] args) {
        new Gerenciamentodealunos();
    }
}
