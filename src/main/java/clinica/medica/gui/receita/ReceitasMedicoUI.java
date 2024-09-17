package clinica.medica.gui.receita;

import clinica.medica.database.ReceitasSQL;
import clinica.medica.database.UsuariosSQL;
import clinica.medica.documentos.Receita;
import clinica.medica.gui.recursos.JListFilter;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.gui.telas.CadastroUI;
import clinica.medica.gui.telas.TelaLogadaUI;
import clinica.medica.gui.recursos.ImpressaoUI;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Classe que possui os métodos usados na criação da interface da parte de receitas do médico
 */
public class ReceitasMedicoUI {
    /**
     * Método referente a tela em que o médico irá escolher se quer prescrever
     * uma nova receita, ou verificar as existentes
     * @param telaLogada Painel principal que vai conter essa tela
     * @return Painel criado para a prescrição de receitas
     */
    public static JPanel telaAreaDasReceitas(TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Área das receitas");

        JPanel painelReceita = new JPanel();
        painelReceita.setLayout(new GridBagLayout());
        painelReceita.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JButton novaReceitaButton = new JButton("Prescrever nova receita");
        constraints.gridy = 0;
        painelReceita.add(novaReceitaButton, constraints);

        JButton verificarReceitaButton = new JButton("Verificar receitas");
        constraints.gridy = 1;
        painelReceita.add(verificarReceitaButton, constraints);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelReceita, BorderLayout.CENTER);

        novaReceitaButton.addActionListener(telaLogada);
        verificarReceitaButton.addActionListener(telaLogada);

        return painelPrincipal;
    }

    /**
     * Método para criação de uma nova tela referente a criação de uma nova receita
     * @param medicoLogado Médico que criará a receita
     * @param pacienteCpf Paciente para qual a receita será prescrita
     * @param telaLogada Painel principal que vai conter esse novo painel
     * @return Painel de cadastro de uma nova receita
     */
    public static JPanel telaNovaReceita(Medico medicoLogado, String pacienteCpf, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Nova receita");

        JPanel painelReceita = new JPanel();
        painelReceita.setLayout(new GridBagLayout());
        painelReceita.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JLabel cpfPacienteLabel = new JLabel("CPF do paciente");
        constraints.gridy = 0;
        painelReceita.add(cpfPacienteLabel, constraints);

        JFormattedTextField cpfField = CadastroUI.inicializaCpf();
        cpfField.setText(pacienteCpf);
        cpfField.setEditable(false);
        cpfField.setEnabled(false);
        constraints.gridy = 1;
        painelReceita.add(cpfField, constraints);

        JLabel remedioLabel = new JLabel("Nome do remedio");
        constraints.gridy = 2;
        painelReceita.add(remedioLabel, constraints);

        JTextField remedioField = new JTextField(20);
        remedioField.setEditable(true);
        remedioField.setEnabled(true);
        constraints.gridy = 3;
        painelReceita.add(remedioField, constraints);

        JLabel detalhesLabel = new JLabel("Detalhes");
        constraints.gridy = 4;
        painelReceita.add(detalhesLabel, constraints);

        JTextArea detalhesArea = new JTextArea(20, 40);
        detalhesArea.setEditable(true);
        detalhesArea.setLineWrap(true);
        detalhesArea.setWrapStyleWord(true);
        constraints.gridy = 5;
        painelReceita.add(detalhesArea, constraints);

        JButton cadastrarReceitaButton = new JButton("Cadastrar receita");
        constraints.gridy = 6;
        painelReceita.add(cadastrarReceitaButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 7;
        painelReceita.add(voltarButton, constraints);

        cadastrarReceitaButton.addMouseListener(new MouseAdapter() {
            /**
             * Evento disparado quando o botão de cadastrar a receita é clicado
             * @param e Evento disparado
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String remedio = remedioField.getText();
                String cpf = cpfField.getText();
                cpf = cpf.replaceAll("[.-]", "");
                String detalhes = detalhesArea.getText();
                Receita receita = new Receita(medicoLogado.getCpf(), cpf, remedio, new Date(Calendar.getInstance().getTime().getTime()), detalhes);

                if (ReceitasSQL.cadastrarReceita(receita, painelReceita)) {
                    JOptionPane.showMessageDialog(painelReceita, "Cadastro de Receita realizado com sucesso!");
                    remedioField.setText("");
                    cpfField.setText("");
                    detalhesArea.setText("");
                    telaLogada.atualizaPainel(medicoLogado);
                }
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelReceita, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método referente ao painel que mostrará as receitas prescritas pelo médico logado
     * @param medicoLogado Médico que vai verificar as receitas
     * @param telaLogada Tela principal onde o painél será adicionado
     * @return Painel para visualização são receitas
     */
    public static JPanel showReceitas(Medico medicoLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Receitas");

        JPanel painelReceita = new JPanel();
        painelReceita.setLayout(new GridBagLayout());
        painelReceita.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        ArrayList<Receita> receitas = ReceitasSQL.selectAllReceitasFromMedico(medicoLogado.getCpf());
        String[] listaReceita = new String[receitas.size()];

        int i = 0;
        for (Receita rc : receitas) {
            listaReceita[i] = "Receita - " + rc.getPaciente().getNome() + " - " + rc.getNomeDoRemedio() + " - " + rc.getId();
            i++;
        }

        JList<String> list = new JList<>(listaReceita);

        JLabel pesquisaLabel = new JLabel("Pesquisar receita");
        constraints.gridy = 0;
        painelReceita.add(pesquisaLabel, constraints);

        JTextField pesquisaField = new JTextField(20);
        constraints.gridy = 1;
        painelReceita.add(pesquisaField, constraints);

        JListFilter<String> listFilter = new JListFilter<>(list);
        listFilter.attachFilterField(pesquisaField);

        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 2;
        painelReceita.add(scrollPanel, constraints);

        JButton imprimirReceitaButton = new JButton("Visualizar receita");
        constraints.gridy = 3;
        painelReceita.add(imprimirReceitaButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 4;
        painelReceita.add(voltarButton, constraints);

        imprimirReceitaButton.addMouseListener(new MouseAdapter() {
            /**
             * Método referente ao evento disparado para impressão da receita
             * selecionada
             * @param e Evento disparado
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String receitaSelecionada = list.getSelectedValue();
                String[] elementos = receitaSelecionada.split("-");
                int id = Integer.parseInt(elementos[3].strip());

                Receita receita = new Receita(id);
                ImpressaoUI.imprimirDocumento(receita);
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelReceita, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que mostra a tela dos pacientes disponíveis para prescrever uma receita
     * @param medicoLogado Médico que irá prescrever a receita
     * @param telaLogada Painel principal onde esse novo painel será posicionado
     * @return Painel criado pelo método
     */
    public static JPanel showPacientesParaPrescreverReceita(Medico medicoLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Pacientes");

        JPanel painelExame = new JPanel();
        painelExame.setLayout(new GridBagLayout());
        painelExame.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        ArrayList<Paciente> pacientes = UsuariosSQL.selectAllPacientes();
        String[] listaPacientes = new String[pacientes.size()];

        int i = 0;
        for (Paciente paciente : pacientes) {
            listaPacientes[i] = paciente.getNome() + " - " + paciente.getCpf();
            i++;
        }

        JList<String> list = new JList<>(listaPacientes);

        JLabel pesquisaLabel = new JLabel("Pesquisar paciente");
        constraints.gridy = 0;
        painelExame.add(pesquisaLabel, constraints);

        JTextField pesquisaField = new JTextField(20);
        constraints.gridy = 1;
        painelExame.add(pesquisaField, constraints);

        JListFilter<String> listFilter = new JListFilter<>(list);
        listFilter.attachFilterField(pesquisaField);

        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 2;
        painelExame.add(scrollPanel, constraints);

        JButton utilizarExameButton = new JButton("Prescrever receita para esse paciente");
        constraints.gridy = 3;
        painelExame.add(utilizarExameButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 4;
        painelExame.add(voltarButton, constraints);

        utilizarExameButton.addMouseListener(new MouseAdapter() {
            /**
             * Evento para quando o paciente para a criação da receita
             * for selecionado
             * @param e Evento disparado
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String exameSelecionado = list.getSelectedValue();
                String[] elementos = exameSelecionado.split("-");
                String pacienteCpf = elementos[1].strip();
                telaLogada.getContentPanel().add(ReceitasMedicoUI.telaNovaReceita(medicoLogado, pacienteCpf, telaLogada), "Prescrever receita para esse paciente");
                telaLogada.getCardLayout().show(telaLogada.getContentPanel(), "Prescrever receita para esse paciente");
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelExame, BorderLayout.CENTER);

        return painelPrincipal;
    }
}