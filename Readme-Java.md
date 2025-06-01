A estrutura principal é fornecer uma interface gráfica (GUI) para gerenciar informações de alunos em um banco de dados MySQL. 

•	Funcionalidades:

1.	Salvar dados de alunos (Turma, Nome, Data de Nascimento, Nota NP1, Nota NP2, e Média Total calculada automaticamente).

2.	Visualizar todos os alunos de uma turma específica.

3.	Editar informações de um aluno existente através do seu ID.

4.	Excluir o registro de um aluno pelo seu ID.

5.	Calcular a média (Total) das notas NP1 e NP2 em tempo real.

6.	Sair da aplicação.

7.	Componentes Principais

8.	A aplicação Gerenciamentodealunos (que estende JFrame) atua como a janela principal.

-------------------------------------------------------------------------------------------------------------
- Rótulos (JLabel): Para identificar os campos de entrada (Turma, Nome, Data de Nascimento, NP1, NP2, Total).

- Campos de Texto (JTextField): Para o usuário digitar e visualizar os dados. O campo "Total" é somente leitura.

- Botões (JButton): Para acionar as ações (Sair, Visualizar, Excluir, Salvar, Editar).

•	Métodos e Funcionalidades Essenciais:

- Gerenciamentodealunos() (Construtor):
Configuração da GUI: Inicializa todos os componentes, organiza-os na janela usando GridBagLayout para um controle preciso de posicionamento e margens.

- Ações dos Botões: Associa cada botão a um método específico (e.g., btnSalvar chama salvarDados()).

- Cálculo Automático do Total: Adiciona um KeyAdapter aos campos txtNP1 e txtNP2, que chama calcularTotal() sempre que uma tecla é liberada, atualizando a média em tempo real.

- Configurações da Janela: Define o comportamento de fechamento, empacota os componentes e centraliza a janela.

- addComponent(): 
Método auxiliar para adicionar rótulos e campos de texto ao layout de forma mais concisa.

- calcularTotal(): 

1.	Função: Calcula a média das notas NP1 e NP2 inseridas nos respectivos campos.

2.	Validação: Trata NumberFormatException se as entradas não forem números válidos, limpando o campo "Total".

salvarDados():

1.	Função: Pega os dados dos campos da GUI e os insere no banco de dados.

2.	Dinâmica da Tabela: Diferentemente do código anterior, este método cria uma nova tabela no MySQL com o nome da turma (sanitizado) se ela ainda não existir.

3.	Inclusão de Dados: Insere o nome, data de nascimento, notas e o total calculado na tabela da turma.

4.	Feedback: Informa o sucesso ou erro da operação via JOptionPane.

visualizarTabela():

1.	Função: Exibe os dados de todos os alunos de uma turma específica no console (através de um JOptionPane).

2.	Requer Turma: Pede que a turma seja informada antes de tentar a visualização.

editarAluno():

1.	Função: Permite atualizar os dados de um aluno existente.

2.	Interação: Solicita o ID do aluno e, em seguida, os novos valores para Nome, Data de Nascimento, NP1 e NP2 via caixas de diálogo.

3.	Atualização: Calcula a nova média e atualiza o registro correspondente no banco de dados da turma.

4.	Feedback: Mensagens informam o resultado da edição.

excluirLinha():

1.	Função: Remove um registro de aluno do banco de dados.

2.	Interação: Solicita o ID do aluno a ser excluído.

3.	Deleção: Executa a query DELETE na tabela da turma.

4.	Feedback: Confirma a exclusão ou informa se o ID não foi encontrado.

sanitizeInput(): 
Método auxiliar crucial para limpar o nome da turma, removendo caracteres especiais e substituindo-os por underscores, para garantir que o nome da tabela no MySQL seja válido e seguro.

getConnection():

1.	Função: Estabelece a conexão com o banco de dados MySQL chamado gerenciamento_de_alunos.

2.	Configurações: Contém a URL, usuário (root) e senha (12345). Lembre-se de ajustar essas credenciais.

limparCampos(): 
Reseta todos os campos de texto da GUI para vazio.

main(String[] args): Ponto de entrada da aplicação, que cria e exibe a janela Gerenciamentodealunos.

•	Dependências Para executar este código, você precisará:

1.	JDK (Java Development Kit).

2.	Driver JDBC para MySQL (.jar) no classpath do projeto.

3.	Um servidor MySQL em execução com um banco de dados chamado gerenciamento_de_alunos. 

4.	As tabelas para cada turma serão criadas dinamicamente pelo próprio programa.

Este programa oferece uma solução mais completa para gerenciamento, com funcionalidades de CRUD (Criar, Ler, Atualizar, Deletar) e um controle mais flexível sobre a estrutura do banco de dados ao criar tabelas por turma.
