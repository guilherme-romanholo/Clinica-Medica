package clinica.medica.gui.encaixe;

import clinica.medica.database.ConsultaSQL;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.gui.telas.CadastroUI;
import clinica.medica.gui.telas.TelaLogadaUI;
import clinica.medica.usuarios.Medico;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.util.Calendar;
/**
 * Classe que possui os métodos usados na criação da interface da parte de encaixe do médico
 */
public class EncaixeMedicoUI {
    /**
     * Método que montará o painel contendo os campos de preenchimento para cadastrar um novo encaixe
     * @param medicoLogado medico que marcará o encaixe
     * @param data data do encaixse
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    public static JPanel telaNovoEncaixe(Medico medicoLogado, Date data, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Novo encaixe");

        JPanel painelEncaixe = new JPanel();
        painelEncaixe.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.gridx = 0;

        JLabel dataLabel = new JLabel("Data do encaixe");
        constraints.gridy = 2;
        painelEncaixe.add(dataLabel, constraints);

        JTextField dataField = new JTextField(20);
        dataField.setText(data.toString());
        dataField.setEditable(false);
        dataField.setEnabled(false);
        constraints.gridy = 3;
        painelEncaixe.add(dataField, constraints);

        JLabel horariolabel = new JLabel("Horário");
        constraints.gridy = 4;
        painelEncaixe.add(horariolabel, constraints);

        String[] horariosDisponiveis = {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};
        JComboBox<String> horariosCombo = new JComboBox<>(horariosDisponiveis);
        constraints.gridy = 5;
        painelEncaixe.add(horariosCombo, constraints);

        JLabel cpfPacienteLabel = new JLabel("CPF do paciente");
        constraints.gridy = 6;
        painelEncaixe.add(cpfPacienteLabel, constraints);

        JFormattedTextField cpfField = CadastroUI.inicializaCpf();
        constraints.gridy = 7;
        painelEncaixe.add(cpfField, constraints);

        JLabel medicoLabel = new JLabel("Medico");
        constraints.gridy = 8;
        painelEncaixe.add(medicoLabel, constraints);

        JTextField medicoField = new JTextField(20);
        medicoField.setText(medicoLogado.getNome());
        medicoField.setEditable(false);
        medicoField.setEnabled(false);
        constraints.gridy = 9;
        painelEncaixe.add(medicoField, constraints);

        JLabel comentarioLabel = new JLabel("Descrição");
        constraints.gridy = 10;
        painelEncaixe.add(comentarioLabel, constraints);

        JTextArea comentarioArea = new JTextArea(10, 20);
        comentarioArea.setSize(new Dimension(20, 20));
        comentarioArea.setEditable(true);
        comentarioArea.setLineWrap(true);
        comentarioArea.setWrapStyleWord(true);
        constraints.gridy = 11;
        painelEncaixe.add(comentarioArea, constraints);

        JLabel motivoEmergencia = new JLabel("Motivo da emergência");
        constraints.gridy = 12;
        painelEncaixe.add(motivoEmergencia, constraints);

        JTextArea motivoArea = new JTextArea(10, 20);
        motivoArea.setSize(new Dimension(20, 20));
        motivoArea.setEditable(true);
        motivoArea.setLineWrap(true);
        motivoArea.setWrapStyleWord(true);
        constraints.gridy = 13;
        painelEncaixe.add(motivoArea, constraints);

        JButton cadastrarEncaixeButton = new JButton("Agendar encaixe");
        constraints.gridy = 14;
        painelEncaixe.add(cadastrarEncaixeButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 15;
        painelEncaixe.add(voltarButton, constraints);

        cadastrarEncaixeButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados fornecidos e salva o encaixe no banco
             * @param e evento de click no botão Cadastrar encaixe
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String cpf = cpfField.getText();
                cpf = cpf.replaceAll("[.-]", "");
                String conteudo = comentarioArea.getText();
                String motivo = motivoArea.getText();
                String horario = (String) horariosCombo.getSelectedItem();
                if (ConsultaSQL.salvarEncaixe(data, medicoLogado.getCpf(), cpf, conteudo, motivo, horario, painelEncaixe)) {
                    JOptionPane.showMessageDialog(painelEncaixe, "Agendamento de encaixe realizado com sucesso!");
                    telaLogada.atualizaPainel(medicoLogado);
                }

            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(painelEncaixe, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel contendo os dias disponíveis para o agendamento do encaixe
     * @param medicoLogado medico que está solicitando o encaixe
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    public static JPanel telaAgendarEncaixe(Medico medicoLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Agendamento de encaixes");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;

        JPanel calendarPanel = new JPanel();
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.setLayout(new GridBagLayout());

        JLabel nomeMedicoLabel = new JLabel("Escolha uma data para agendar o encaixe");
        constraints.gridy = 0;
        calendarPanel.add(nomeMedicoLabel, constraints);

        JLabel calendarLabel = new JLabel("Calendário");
        constraints.gridy = 1;
        calendarPanel.add(calendarLabel, constraints);

        JCalendar calendar = new JCalendar();
        calendar.setSize(400, 400);
        calendar.getDayChooser().getDayPanel().setBackground(Color.WHITE);
        calendar.getDayChooser().setWeekOfYearVisible(false);
        calendar.setDecorationBackgroundColor(Color.WHITE);
        calendar.setMinSelectableDate(new Date(Calendar.getInstance().getTime().getTime()));
        constraints.gridy = 2;
        calendarPanel.add(calendar, constraints);

        JButton agendarConsultaButton = new JButton("Agendar o encaixe");
        constraints.gridy = 3;
        calendarPanel.add(agendarConsultaButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(telaLogada);

        class MeuPropertyListener implements PropertyChangeListener {
            protected Date data;
            /**
             * método que pega a data selecionada no calendário
             * @param e evento de mudança da data selecionada
             */
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("calendar".equals(e.getPropertyName())) {
                    Calendar selectedCalendar = (Calendar) e.getNewValue();
                    this.data = new Date(selectedCalendar.getTimeInMillis());
                }
            }
            /**
             * criação de uma nova classe interna para tratar os eventos de click do mouse nos dias do calendário
             */
            public class MeuMouseListener extends MouseAdapter {
                /**
                 * Método que adiciona o painel de agendar a consulta na tela
                 * @param e evento de click do mouse no botão Agendar consulta
                 */
                @Override
                public void mouseClicked(MouseEvent e) {
                    telaLogada.getContentPanel().add(EncaixeMedicoUI.telaNovoEncaixe(medicoLogado, data, telaLogada), "Agendar o encaixe");
                    telaLogada.getCardLayout().show(telaLogada.getContentPanel(), "Agendar o encaixe");
                }
            }
        }

        MeuPropertyListener propertyListener = new MeuPropertyListener();
        calendar.addPropertyChangeListener(propertyListener);

        MeuPropertyListener.MeuMouseListener meuMouseListener = propertyListener.new MeuMouseListener();
        agendarConsultaButton.addMouseListener(meuMouseListener);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(calendarPanel, BorderLayout.CENTER);

        return painelPrincipal;

    }
}
