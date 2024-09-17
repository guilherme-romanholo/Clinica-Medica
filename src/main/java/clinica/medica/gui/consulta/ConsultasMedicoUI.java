package clinica.medica.gui.consulta;

import clinica.medica.consultas.Consulta;
import clinica.medica.consultas.Encaixe;
import clinica.medica.database.ConsultaSQL;
import clinica.medica.gui.recursos.JListFilter;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.gui.telas.TelaLogadaUI;
import clinica.medica.usuarios.Medico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Classe que possui os métodos usados na criação da 'interface' da parte de consultas do médico
 */
public class ConsultasMedicoUI {
    /**
     * Método que montará o painel da interface da área de consultas na parte do médico
     * @param telaLogada tela principal que conterá o painel da área das consultas
     * @return retorna o painel da área de consultas consultas para adicioná-lo na tela principal
     */
    public static JPanel telaAreaDasConsultas(TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Área das consultas");

        JPanel painelConsulta = new JPanel();
        painelConsulta.setLayout(new GridBagLayout());
        painelConsulta.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JButton novaConsultaButton = new JButton("Agendar novo encaixe");
        constraints.gridy = 0;
        painelConsulta.add(novaConsultaButton, constraints);

        JButton verificarConsultaButton = new JButton("Verificar consultas");
        constraints.gridy = 1;
        painelConsulta.add(verificarConsultaButton, constraints);

        verificarConsultaButton.addActionListener(telaLogada);
        novaConsultaButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelConsulta, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel contendo todas as consultas que estão marcadas com o médico
     * @param medicoLogado CPF do médico que deve realizar as consultas
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    public static JPanel showConsultasMarcadas(Medico medicoLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Consultas");

        JPanel painelConsulta = new JPanel();
        painelConsulta.setLayout(new GridBagLayout());
        painelConsulta.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        ArrayList<Consulta> consultas = ConsultaSQL.selectAllConsultasFromMedico(medicoLogado.getCpf());
        String[] listaConsulta = new String[consultas.size()];

        int i = 0;
        for (Consulta rc : consultas) {
            if (!rc.isRealizada()) {
                if (rc.isEncaixe()) {
                    listaConsulta[i] = "Encaixe marcado - " + "Paciente: " + rc.getPaciente().getNome() + " - " + "Data: " + rc.getData() + " - " + "Horário: " + rc.getHorario() + " horas" + " - " + rc.getId();
                }else{
                    listaConsulta[i] = "Consulta marcada - " + "Paciente: " + rc.getPaciente().getNome() + " - " + "Data: " + rc.getData() + " - " + "Horário: " + rc.getHorario() + " horas" + " - " + rc.getId();
                }
                i++;
            }
        }

        JList<String> list = new JList<>(listaConsulta);

        JLabel pesquisaLabel = new JLabel("Pesquisar consulta");
        constraints.gridy = 0;
        painelConsulta.add(pesquisaLabel, constraints);

        JTextField pesquisaField = new JTextField(20);
        constraints.gridy = 1;
        painelConsulta.add(pesquisaField, constraints);

        JListFilter<String> listFilter = new JListFilter<>(list);
        listFilter.attachFilterField(pesquisaField);

        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 2;
        painelConsulta.add(scrollPanel, constraints);

        JButton vizualizarComentarioButton = new JButton("Visualizar comentários da consulta");
        constraints.gridy = 3;
        painelConsulta.add(vizualizarComentarioButton, constraints);

        JButton editarConsultaButton = new JButton("Editar consulta");
        constraints.gridy = 4;
        painelConsulta.add(editarConsultaButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 5;
        painelConsulta.add(voltarButton, constraints);

        vizualizarComentarioButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados da consulta selecionada e mostra na tela os comentários gerais da consulta
             * @param e evento de click do mouse no botão Visualizar comentário
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                //Divide a String selecionada a partir do caracter '-' e seleciona o ID da consulta no final da string
                String consultaSelecionada = list.getSelectedValue();
                String[] elementos = consultaSelecionada.split("-");
                int id = Integer.parseInt(elementos[6].strip());
                Consulta consulta = new Consulta(id);
                //Verificação para ver a situação e o tipo da consulta

                //Caso de consulta normal
                if (consulta.getMotivoCancelamento().equals("") && !consulta.isEncaixe())
                    JOptionPane.showMessageDialog(painelConsulta, "Comentários do paciente: " + consulta.getDescricao(), "Comentarios da consulta", JOptionPane.INFORMATION_MESSAGE);
                //Caso de consulta cancelada
                else if (!consulta.getMotivoCancelamento().equals(""))
                    JOptionPane.showMessageDialog(painelConsulta, "Comentários do paciente: " + consulta.getDescricao() + "\n\nMotivo do cancelamento: " + consulta.getMotivoCancelamento(), "Comentarios da consulta", JOptionPane.INFORMATION_MESSAGE);
                //Caso de encaixe
                else if(consulta.isEncaixe()){
                    Encaixe encaixe = new Encaixe(consulta.getId());
                    JOptionPane.showMessageDialog(painelConsulta, "Comentários do paciente: " + consulta.getDescricao() + "\n\nMotivo da emergência: " + encaixe.getMotivoEmergencia(), "Comentarios da consulta", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        editarConsultaButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que atualiza a consulta a partir da opção selecionada pelo médico
             * @param e the event to be processed
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String consultaSelecionada = list.getSelectedValue();
                String[] elementos = consultaSelecionada.split("-");
                int id = Integer.parseInt(elementos[6].strip());
                Consulta consulta = new Consulta(id);

                JRadioButton feitaButton = new JRadioButton("Marcar consulta como realizada");
                JRadioButton canceladaButton = new JRadioButton("Cancelar consulta");
                ButtonGroup radioGroup = new ButtonGroup();
                radioGroup.add(feitaButton);
                radioGroup.add(canceladaButton);

                JPanel radioPanel = new JPanel();
                radioPanel.setLayout(new FlowLayout());
                radioPanel.add(feitaButton);
                radioPanel.add(canceladaButton);

                //JOption Pane que verifica a opção de atualização da consulta
                int option = JOptionPane.showOptionDialog(painelConsulta, radioPanel, "Selecione uma opção", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                if (option == JOptionPane.OK_OPTION) {
                    //Caso em que a consulta é apenas marcada com realizada
                    if (feitaButton.isSelected()) {
                        //Atualiza a consulta no banco de dados
                        ConsultaSQL.atualizaConsulta(consulta.getId());
                        JOptionPane.showMessageDialog(painelConsulta, "Consulta finalizada com sucesso! ", "Consulta finalizada", JOptionPane.INFORMATION_MESSAGE);
                        telaLogada.atualizaPainel(medicoLogado);
                    } else if (canceladaButton.isSelected()) { //Caso quem a consulta é cancelada
                        JPanel fieldPanel = new JPanel();
                        fieldPanel.setLayout(new BorderLayout());
                        JLabel motivoLabel = new JLabel("Digite o motivo do cancelamento");
                        JTextArea cancelamento = new JTextArea(10, 10);
                        cancelamento.setEditable(true);
                        cancelamento.setLineWrap(true);
                        fieldPanel.add(motivoLabel, BorderLayout.NORTH);
                        fieldPanel.add(cancelamento, BorderLayout.SOUTH);

                        //É criado um novo JOption Pane que contém o motivo do cancelamento
                        int option2 = JOptionPane.showOptionDialog(painelConsulta, fieldPanel, "Cancelamento", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (option2 == JOptionPane.OK_OPTION) {
                            //Pega o motivo do cancelamento e atualiza a consulta no banco de dados
                            String motivo = cancelamento.getText();
                            ConsultaSQL.atualizaConsulta(consulta.getId(), motivo);
                            JOptionPane.showMessageDialog(painelConsulta, "Consulta cancelada com sucesso!", "Consulta cancelada", JOptionPane.INFORMATION_MESSAGE);
                            telaLogada.atualizaPainel(medicoLogado);
                        }
                    }
                }
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelConsulta, BorderLayout.CENTER);

        return painelPrincipal;
    }
}
