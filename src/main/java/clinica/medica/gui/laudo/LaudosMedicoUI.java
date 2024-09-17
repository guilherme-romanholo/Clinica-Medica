package clinica.medica.gui.laudo;

import clinica.medica.database.ExamesSQL;
import clinica.medica.database.LaudosSQL;
import clinica.medica.database.MedicosSQL;
import clinica.medica.documentos.Exame;
import clinica.medica.documentos.Laudo;
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
 * Classe que possui os métodos usados na criação da interface da parte de laudos do médico
 */
public class LaudosMedicoUI {
    /**
     * Método que montará o painel da área de laudos do médico
     * @param telaLogada tela principal que conterá o painel da área de laudos
     * @return retorna o painel da área de laudos para adicioná-lo na tela principal
     */
    public static JPanel telaAreaDeLaudos(TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Área dos laudos");

        JPanel painelLaudo = new JPanel();
        painelLaudo.setLayout(new GridBagLayout());
        painelLaudo.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JButton novoLaudoButton = new JButton("Prescrever novo laudo");
        constraints.gridy = 0;
        painelLaudo.add(novoLaudoButton, constraints);

        JButton verificarLaudoButton = new JButton("Verificar laudos");
        constraints.gridy = 1;
        painelLaudo.add(verificarLaudoButton, constraints);

        novoLaudoButton.addActionListener(telaLogada);
        verificarLaudoButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelLaudo, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel contendo os campos de preenchimento para fazer um novo laudo
     * @param medicoLogado médico logado no sistema
     * @param exame exame utilizado para fazer o laudo
     * @param telaLogada tela principal que conterá o painel de noov laudo
     * @return retorna o painel de novo laudo para adicioná-lo na tela principal
     */
    public static JPanel telaNovoLaudo(Medico medicoLogado, Exame exame, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Novo Laudo");

        JPanel painelLaudo = new JPanel();
        painelLaudo.setLayout(new GridBagLayout());
        painelLaudo.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JLabel tipoLabel = new JLabel("Tipo de exame");
        constraints.gridy = 0;
        painelLaudo.add(tipoLabel, constraints);

        JTextField tipoField = new JTextField(20);
        tipoField.setText(exame.getTipo());
        tipoField.setEditable(false);
        tipoField.setEnabled(false);
        constraints.gridy = 1;
        painelLaudo.add(tipoField, constraints);

        JLabel cpfPacienteLabel = new JLabel("CPF do paciente");
        constraints.gridy = 2;
        painelLaudo.add(cpfPacienteLabel, constraints);

        JFormattedTextField cpfField = CadastroUI.inicializaCpf();
        cpfField.setText(exame.getPaciente().getCpf());
        cpfField.setEditable(false);
        cpfField.setEnabled(false);
        constraints.gridy = 3;
        painelLaudo.add(cpfField, constraints);

        JLabel comentarioLabel = new JLabel("Conclusão");
        constraints.gridy = 4;
        painelLaudo.add(comentarioLabel, constraints);

        JTextArea comentarioArea = new JTextArea(20, 40);
        comentarioArea.setEditable(true);
        comentarioArea.setLineWrap(true);
        comentarioArea.setWrapStyleWord(true);
        constraints.gridy = 5;
        painelLaudo.add(comentarioArea, constraints);

        JButton cadastrarLaudoButton = new JButton("Cadastrar laudo");
        constraints.gridy = 6;
        painelLaudo.add(cadastrarLaudoButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 7;
        painelLaudo.add(voltarButton, constraints);

        cadastrarLaudoButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados fornecidos e salva o laudo no banco de dados
             * @param e evento de click do mouse no botão Cadastrar laudo
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String cpf = cpfField.getText();
                cpf = cpf.replaceAll("[.-]", "");
                String conteudo = comentarioArea.getText();
                if (MedicosSQL.cadastrarNovoLaudo(exame.getId(), cpf, medicoLogado.getCpf(), new Date(Calendar.getInstance().getTime().getTime()), conteudo, painelLaudo)) {
                    JOptionPane.showMessageDialog(painelLaudo, "Cadastro de Laudo realizado com sucesso!");
                    tipoField.setText("");
                    cpfField.setText("");
                    comentarioArea.setText("");
                    telaLogada.atualizaPainel(medicoLogado);
                }

            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelLaudo, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel com os exames disponíveis para fazer um laudo
     * @param medicoLogado médico logado no sistema
     * @param telaLogada tela principal que conterá o painel dos exames disponíveis
     * @return retorna o painel dos exames disponíveis para adicioná-lo na tela principal
     */
    public static JPanel showExamesDisponiveisParaLaudo(Medico medicoLogado, TelaLogadaUI telaLogada) {
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

        JButton utilizarExameButton = new JButton("Prescrever laudo para esse exame");
        constraints.gridy = 3;
        painelExame.add(utilizarExameButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 4;
        painelExame.add(voltarButton, constraints);

        utilizarExameButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados do exame selecionado e chama um novo painel para cadastrar o laudo
             * @param e evento de click do mouse no botão Utilizar exame
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String exameSelecionado = list.getSelectedValue();
                String[] elementos = exameSelecionado.split("-");
                int id = Integer.parseInt(elementos[3].strip());
                Exame exame = new Exame(id);
                telaLogada.getContentPanel().add(LaudosMedicoUI.telaNovoLaudo(medicoLogado, exame, telaLogada), "Prescrever laudo para esse exame");
                telaLogada.getCardLayout().show(telaLogada.getContentPanel(), "Prescrever laudo para esse exame");
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelExame, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel contendo os laudos feitos pelo médico
     * @param medicoLogado médico logado no sistema
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    public static JPanel showLaudos(Medico medicoLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Laudos");

        JPanel painelLaudo = new JPanel();
        painelLaudo.setLayout(new GridBagLayout());
        painelLaudo.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        ArrayList<Laudo> laudos = LaudosSQL.verificarLaudos(medicoLogado.getCpf());
        String[] listaLaudo = new String[laudos.size()];

        int i = 0;
        for (Laudo ex : laudos) {
            listaLaudo[i] = "Laudo - " + ex.getPaciente().getNome() + " - " + ex.getExame().getTipo() + " - " + ex.getExame().getId();
            i++;
        }

        JList<String> list = new JList<>(listaLaudo);

        JLabel pesquisaLabel = new JLabel("Pesquisar laudo");
        constraints.gridy = 0;
        painelLaudo.add(pesquisaLabel, constraints);

        JTextField pesquisaField = new JTextField(20);
        constraints.gridy = 1;
        painelLaudo.add(pesquisaField, constraints);

        JListFilter<String> listFilter = new JListFilter<>(list);
        listFilter.attachFilterField(pesquisaField);

        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 2;
        painelLaudo.add(scrollPanel, constraints);

        JButton imprimirLaudoButton = new JButton("Visualizar laudo");
        constraints.gridy = 3;
        painelLaudo.add(imprimirLaudoButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 4;
        painelLaudo.add(voltarButton, constraints);

        imprimirLaudoButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados do laudo selecionado e imprime este
             * @param e evento de click do mouse no botão imprimir laudo
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String laudoSelecionado = list.getSelectedValue();
                String[] elementos = laudoSelecionado.split("-");
                int id = Integer.parseInt(elementos[3].strip());
                Laudo laudo = new Laudo(id);
                ImpressaoUI.imprimirDocumento(laudo);
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelLaudo, BorderLayout.CENTER);

        return painelPrincipal;
    }
}
