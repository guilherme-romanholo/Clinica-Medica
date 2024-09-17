package clinica.medica.gui.exame;

import clinica.medica.database.ExamesSQL;
import clinica.medica.database.MedicosSQL;
import clinica.medica.documentos.Exame;
import clinica.medica.gui.recursos.JListFilter;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.gui.telas.CadastroUI;
import clinica.medica.gui.telas.TelaLogadaUI;
import clinica.medica.gui.recursos.ImpressaoUI;
import clinica.medica.usuarios.Medico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Classe que possui os métodos usados na criação da interface de exames do médico
 */
public class ExamesMedicoUI {
    /**
     * método que montará o painel da área de exames do médico
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    public static JPanel telaAreaDosExames(TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Área dos exames");

        JPanel painelExame = new JPanel();
        painelExame.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JButton novoExameButton = new JButton("Prescrever novo exame");
        constraints.gridy = 0;
        painelExame.add(novoExameButton, constraints);

        JButton verificarExameButton = new JButton("Verificar exames");
        constraints.gridy = 1;
        painelExame.add(verificarExameButton, constraints);

        novoExameButton.addActionListener(telaLogada);
        verificarExameButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelExame, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel contendo os campos de preenchimento para cadastrar um novo exame
     * @param medicoLogado médico que irá fazer o exame
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    public static JPanel telaNovoExame(Medico medicoLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Novo exame");

        JPanel painelExame = new JPanel();
        painelExame.setLayout(new GridBagLayout());
        painelExame.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JLabel tipoLabel = new JLabel("Tipo de exame");
        constraints.gridy = 0;
        painelExame.add(tipoLabel, constraints);

        JTextField tipoField = new JTextField(20);
        constraints.gridy = 1;
        painelExame.add(tipoField, constraints);

        JLabel cpfPacienteLabel = new JLabel("CPF do paciente");
        constraints.gridy = 2;
        painelExame.add(cpfPacienteLabel, constraints);

        JFormattedTextField cpfField = CadastroUI.inicializaCpf();
        constraints.gridy = 3;
        painelExame.add(cpfField, constraints);

        JLabel comentarioLabel = new JLabel("Comentários");
        constraints.gridy = 4;
        painelExame.add(comentarioLabel, constraints);

        JTextArea comentarioArea = new JTextArea(20, 40);
        comentarioArea.setEditable(true);
        comentarioArea.setLineWrap(true);
        comentarioArea.setWrapStyleWord(true);
        constraints.gridy = 5;
        painelExame.add(comentarioArea, constraints);

        JButton cadastrarExameButton = new JButton("Cadastrar exame");
        constraints.gridy = 6;
        painelExame.add(cadastrarExameButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 7;
        painelExame.add(voltarButton, constraints);

        cadastrarExameButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados fornecidos e salva o exame no banco de dados
             * @param e evento de click do mouse no botão Cadastrar exame
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String tipo = tipoField.getText();
                String cpf = cpfField.getText();
                cpf = cpf.replaceAll("[.-]", "");
                String comentario = comentarioArea.getText();
                if (MedicosSQL.cadastrarNovoExame(tipo, cpf, medicoLogado.getCpf(), new Date(Calendar.getInstance().getTime().getTime()), comentario, painelExame)) {
                    JOptionPane.showMessageDialog(painelExame, "Cadastro de exame realizado com sucesso!");
                    telaLogada.atualizaPainel(medicoLogado);
                }
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelExame, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel contendo os exames feitos pelo médico
     * @param medicoLogado médico logado no sistema
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    public static JPanel showExamesFeitosPeloMedico(Medico medicoLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Exames");

        JPanel painelExame = new JPanel();
        painelExame.setLayout(new GridBagLayout());
        painelExame.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        ArrayList<Exame> exames = ExamesSQL.verificarExames(medicoLogado.getCpf());
        String[] listaExame = new String[exames.size()];

        int i = 0;
        for (Exame ex : exames) {
            listaExame[i] = "Exame - " + ex.getPaciente().getNome() + " - " + ex.getTipo() + " - " + ex.getId();
            i++;
        }

        JList<String> list = new JList<>(listaExame);

        JLabel pesquisaLabel = new JLabel("Pesquisar exame");
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


        JButton imprimirExameButton = new JButton("Visualizar exame");
        constraints.gridy = 3;
        painelExame.add(imprimirExameButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 4;
        painelExame.add(voltarButton, constraints);

        imprimirExameButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados do exame e imprime este em uma nova tela
             * @param e evento de click do mouse no botão Imprimir exame
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String exameSelecionado = list.getSelectedValue();
                String[] elementos = exameSelecionado.split("-");
                int id = Integer.parseInt(elementos[3].strip());
                Exame exame = new Exame(id);
                ImpressaoUI.imprimirDocumento(exame);
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelExame, BorderLayout.CENTER);

        return painelPrincipal;
    }
}
