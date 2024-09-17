package clinica.medica.gui.consulta;

import clinica.medica.consultas.Consulta;
import clinica.medica.consultas.Encaixe;
import clinica.medica.database.ConsultaSQL;
import clinica.medica.database.HorariosSQL;
import clinica.medica.database.UsuariosSQL;
import clinica.medica.gui.recursos.JListFilter;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.gui.telas.CadastroUI;
import clinica.medica.gui.telas.TelaLogadaUI;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Classe que possui os métodos usados na criação da interface da parte de consultas do paciente
 */
public class ConsultasPacienteUI {
    /**
     * Método que montará o painel contendo todos os médicos do sistema
     * @param pacienteLogado paciente que deseja marcar a consulta
     * @param telaLogada tela principal
     * @return retorna o painel da área de consultas consultas para adicioná-lo na tela principal
     */
    public static JPanel showMedicos(Paciente pacienteLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Medicos");

        JPanel painelExame = new JPanel();
        painelExame.setLayout(new GridBagLayout());
        painelExame.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        ArrayList<Medico> medicos = UsuariosSQL.selectAllMedicos();
        String[] listaMedicos = new String[medicos.size()];

        int i = 0;
        for (Medico medico : medicos) {
            listaMedicos[i] = medico.getNome() + " - " + medico.getAreaAtuacao() + " - " + medico.getCpf();
            i++;
        }

        JLabel pesquisaLabel = new JLabel("Pesquisar médico");
        constraints.gridy = 0;
        painelExame.add(pesquisaLabel, constraints);

        JTextField pesquisaField = new JTextField(20);
        constraints.gridy = 1;
        painelExame.add(pesquisaField, constraints);

        JList<String> list = new JList<>(listaMedicos);
        JListFilter<String> listFilter = new JListFilter<>(list);
        listFilter.attachFilterField(pesquisaField);

        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 2;
        painelExame.add(scrollPanel, constraints);

        JButton escolherMedicoButton = new JButton("Agendar consulta com o médico selecionado");
        constraints.gridy = 3;
        painelExame.add(escolherMedicoButton, constraints);

        escolherMedicoButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados do médico selecionado e chama o próximo painel da parte de consultas
             * @param e evento do mouse
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String exameSelecionado = list.getSelectedValue();
                String[] elementos = exameSelecionado.split("-");
                String cpfMedico = elementos[2].strip();
                String nomeMedico = elementos[0].strip();
                //Adiciona um novo painel por cima do painel atual
                telaLogada.getContentPanel().add(telaAgendarConsulta(cpfMedico, nomeMedico, pacienteLogado, telaLogada), "Agendar consulta com o médico selecionado");
                telaLogada.getCardLayout().show(telaLogada.getContentPanel(), "Agendar consulta com o médico selecionado");
            }
        });

        painelPrincipal.add(infoPanel,BorderLayout.NORTH);
        painelPrincipal.add(painelExame,BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel com as consultas marcadas pelo paciente
     * @param pacienteLogado paciente que está logado
     * @return retorna o painel da área de consultas consultas para adicioná-lo na tela principal
     */
    public static JPanel telaVerificarConsultas(Paciente pacienteLogado) {
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

        ArrayList<Consulta> consultas = ConsultaSQL.selectAllConsultasFromPaciente(pacienteLogado.getCpf());
        String[] listaConsulta = new String[consultas.size()];

        int i = 0;
        for (Consulta rc: consultas) {
            if(rc.isRealizada()) {

                if (rc.getMotivoCancelamento().equals(""))
                    listaConsulta[i] = "Consulta já realizada - " + "Dr. " + rc.getMedico().getNome() + " - " + "Data: " + rc.getData() + " - " + "Horário: " + rc.getHorario() + " horas" + " - " + rc.getId();
                else
                    listaConsulta[i] = "Consulta cancelada - " + "Dr. " + rc.getMedico().getNome() + " - " + "Data: " + rc.getData() + " - " + "Horário: " + rc.getHorario() + " horas" + " - " + rc.getId();
            }else {
                if(rc.isEncaixe())
                    listaConsulta[i] = "Encaixe marcado - " + "Dr. " + rc.getMedico().getNome() + " - " + "Data: " + rc.getData() + " - " + "Horário: " + rc.getHorario() + " horas" + " - " + rc.getId();
                else
                    listaConsulta[i] = "Consulta marcada - " + "Dr. " + rc.getMedico().getNome() + " - " + "Data: " + rc.getData() + " - " + "Horário: " + rc.getHorario() + " horas" + " - " + rc.getId();
            }
            i++;
        }

        JLabel pesquisaLabel = new JLabel("Pesquisar consulta");
        constraints.gridy = 0;
        painelConsulta.add(pesquisaLabel, constraints);

        JTextField pesquisaField = new JTextField(20);
        constraints.gridy = 1;
        painelConsulta.add(pesquisaField, constraints);

        JList<String> list = new JList<>(listaConsulta);

        JListFilter<String> listFilter = new JListFilter<>(list);
        listFilter.attachFilterField(pesquisaField);

        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 2;
        painelConsulta.add(scrollPanel, constraints);

        JButton vizualizarComentarioButton = new JButton("Visualizar comentários da consulta");
        constraints.gridy = 3;
        painelConsulta.add(vizualizarComentarioButton, constraints);

        vizualizarComentarioButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados da consulta e imprime na tela os comentários gerais dessa consulta
             * @param e evento do mouse
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String consultaSelecionada = list.getSelectedValue();
                String[] elementos = consultaSelecionada.split("-");
                int id = Integer.parseInt(elementos[6].strip());
                Consulta consulta = new Consulta(id);
                //Caso de consulta normal
                if(consulta.getMotivoCancelamento().equals("") && !consulta.isEncaixe())
                    JOptionPane.showMessageDialog(painelConsulta, "Comentários: " + consulta.getDescricao(), "Comentarios da consulta", JOptionPane.INFORMATION_MESSAGE);
                else { //Caso de consulta cancelada ou encaixe, respectivamente
                    if(!consulta.isEncaixe()) {
                        JOptionPane.showMessageDialog(painelConsulta, "Comentários: " + consulta.getDescricao() + "\n\nMotivo do cancelamento: " + consulta.getMotivoCancelamento(), "Comentarios da consulta", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        Encaixe encaixe = new Encaixe(consulta.getId());
                        JOptionPane.showMessageDialog(painelConsulta, "Comentários: " + consulta.getDescricao() + "\n\nMotivo da emergência: " + encaixe.getMotivoEmergencia(), "Comentarios da consulta", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        painelPrincipal.add(infoPanel,BorderLayout.NORTH);
        painelPrincipal.add(painelConsulta,BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel com as informações a serem preenchidas de uma nova consulta
     * @param pacienteLogado paciente que deseja marcar a consulta
     * @param medicoSolicitado medico referente a consulta
     * @param cpfMedico CPF do médico referente a cosnulta
     * @param data data da consulta
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    protected static JPanel telaNovaConsulta(Paciente pacienteLogado, String medicoSolicitado, String cpfMedico, Date data, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Nova consulta");

        JPanel painelConsulta = new JPanel();
        painelConsulta.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.gridx = 0;

        JLabel dataLabel = new JLabel("Data da consulta");
        constraints.gridy = 0;
        painelConsulta.add(dataLabel, constraints);

        JTextField dataField = new JTextField(20);
        dataField.setText(data.toString());
        dataField.setEditable(false);
        dataField.setEnabled(false);
        constraints.gridy = 1;
        painelConsulta.add(dataField, constraints);

        JLabel horariolabel = new JLabel("Horário");
        constraints.gridy = 2;
        painelConsulta.add(horariolabel, constraints);

        String[] horariosDisponiveis = HorariosSQL.selectHorariosDisponiveis(cpfMedico, data);
        JComboBox<String> horariosCombo = new JComboBox<>(horariosDisponiveis);
        constraints.gridy = 3;
        painelConsulta.add(horariosCombo, constraints);

        JLabel cpfPacienteLabel = new JLabel("CPF do paciente");
        constraints.gridy = 4;
        painelConsulta.add(cpfPacienteLabel, constraints);

        JFormattedTextField cpfField = CadastroUI.inicializaCpf();
        cpfField.setText(pacienteLogado.getCpf());
        cpfField.setEditable(false);
        cpfField.setEnabled(false);
        constraints.gridy = 5;
        painelConsulta.add(cpfField, constraints);

        JLabel medicoLabel = new JLabel("Medico");
        constraints.gridy = 6;
        painelConsulta.add(medicoLabel, constraints);

        JTextField medicoField = new JTextField(20);
        medicoField.setText(medicoSolicitado);
        medicoField.setEditable(false);
        medicoField.setEnabled(false);
        constraints.gridy = 7;
        painelConsulta.add(medicoField, constraints);

        JLabel comentarioLabel = new JLabel("Descrição");
        constraints.gridy = 8;
        painelConsulta.add(comentarioLabel, constraints);

        JTextArea comentarioArea = new JTextArea(20, 40);
        comentarioArea.setSize(new Dimension(100, 100));
        comentarioArea.setEditable(true);
        comentarioArea.setLineWrap(true);
        comentarioArea.setWrapStyleWord(true);
        constraints.gridy = 9;
        painelConsulta.add(comentarioArea, constraints);

        JButton cadastrarConsultaButton = new JButton("Agendar consulta");
        constraints.gridy = 10;
        painelConsulta.add(cadastrarConsultaButton, constraints);

        JButton voltarButton = new JButton("Voltar");
        constraints.gridy = 11;
        painelConsulta.add(voltarButton, constraints);

        cadastrarConsultaButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados fornecidos e salva a consulta no banco de dados, caso esteja tudo certo
             * @param e evento do botão Cadastrar consulta
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String conteudo = comentarioArea.getText();
                String horario = (String) horariosCombo.getSelectedItem();
                //Salva a consulta no banco caso haja horários disponíveis
                assert horario != null;
                if (!horario.equals("Não existem horários disponíveis nesse dia.") && ConsultaSQL.salvarConsulta(data, cpfMedico, pacienteLogado.getCpf(),conteudo, horario,painelConsulta)) {
                    JOptionPane.showMessageDialog(painelConsulta, "Agendamento de consulta realizado com sucesso!");
                    telaLogada.atualizaPainel(pacienteLogado);
                }else {
                    JOptionPane.showMessageDialog(painelConsulta, "Não é possível cadastrar consultas sem horários disponíveis !", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        voltarButton.addActionListener(telaLogada);

        painelPrincipal.add(infoPanel,BorderLayout.NORTH);
        painelPrincipal.add(painelConsulta,BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método que montará o painel contendo os dias disponíveis para o agendamento da consulta
     * @param cpfMedico CPF do médico referente a consulta
     * @param nomeMedico nome do médico refente a consulta
     * @param pacienteLogado paciente que deseja marcar a consulta
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    protected static JPanel telaAgendarConsulta(String cpfMedico, String nomeMedico, Paciente pacienteLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Agendamento de consulta");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JPanel calendarPanel = new JPanel();
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.setLayout(new GridBagLayout());

        JLabel nomeMedicoLabel = new JLabel("Escolha uma data para agendar a consulta com o Dr." + nomeMedico);
        constraints.gridy = 0;
        calendarPanel.add(nomeMedicoLabel,constraints);

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

        JButton agendarConsultaButton = new JButton("Agendar a consulta");
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
                    telaLogada.getContentPanel().add(telaNovaConsulta(pacienteLogado, nomeMedico, cpfMedico,  data, telaLogada), "Agendar a consulta");
                    telaLogada.getCardLayout().show(telaLogada.getContentPanel(), "Agendar a consulta");
                }
            }
        }

        MeuPropertyListener propertyListener = new MeuPropertyListener();
        calendar.addPropertyChangeListener(propertyListener);

        MeuPropertyListener.MeuMouseListener meuMouseListener = propertyListener.new MeuMouseListener();
        agendarConsultaButton.addMouseListener(meuMouseListener);

        painelPrincipal.add(infoPanel,BorderLayout.NORTH);
        painelPrincipal.add(calendarPanel,BorderLayout.CENTER);

        return painelPrincipal;
    }
}
