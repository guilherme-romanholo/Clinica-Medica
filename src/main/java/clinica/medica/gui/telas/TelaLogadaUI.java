package clinica.medica.gui.telas;

import clinica.medica.consultas.Consulta;
import clinica.medica.database.ConsultaSQL;
import clinica.medica.gui.consulta.ConsultasMedicoUI;
import clinica.medica.gui.consulta.ConsultasPacienteUI;
import clinica.medica.gui.encaixe.EncaixeMedicoUI;
import clinica.medica.gui.exame.ExamesMedicoUI;
import clinica.medica.gui.exame.ExamesPacienteUI;
import clinica.medica.gui.laudo.LaudosMedicoUI;
import clinica.medica.gui.laudo.LaudosPacienteUI;
import clinica.medica.gui.receita.ReceitasMedicoUI;
import clinica.medica.gui.receita.ReceitasPacienteUI;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;
import clinica.medica.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import com.formdev.flatlaf.FlatLightLaf;

import static clinica.medica.gui.exame.ExamesMedicoUI.telaAreaDosExames;


/**
 * Classe para criação da tela principal após o login
 */
public class TelaLogadaUI extends JFrame implements ActionListener {
    private String ultimaTela, telaAtual;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    /**
     * Método construtor da tela principal do programa
     * @param user Usuaário que foi logado
     */
    public TelaLogadaUI (Usuario user) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tela Logada");
        setLayout(new BorderLayout());
        setSize(1200, 800);

        // ========== Menu ==========
        menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g.create();

                GradientPaint gradient = new GradientPaint(0, 0, Color.decode("#5e8ab3"), getWidth(), getHeight(), Color.decode("#67dcff"));

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.dispose();
            }
        };
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JLabel logoLabel = RecursosUI.criaImagemLabel("/images/logoMini.png", 160, 100);
        menuPanel.add(logoLabel);

        JPanel botoesPanel = botoesNavegacao(user);
        botoesPanel.setOpaque(false);
        menuPanel.add(botoesPanel);

        add(menuPanel, BorderLayout.WEST);

        // ========== Conteúdo ==========
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        criaPaineisConteudo(user);

        add(contentPanel, BorderLayout.CENTER);

        telaAtual = "Principal";
        cardLayout.show(contentPanel, "Principal");
    }

    /**
     * Método para realizar a mudança das telas no CardLayout, sair e voltar
     * as telas
     * @param e Evento realizado
     */
    public void actionPerformed(ActionEvent e) {
        String nomePainel = e.getActionCommand();
        if (nomePainel.equals("Sair")) {
            int resp = JOptionPane.showOptionDialog(null, "Tem certeza que deseja sair?", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Sim", "Não"}, "Sim");
            if (resp == JOptionPane.YES_OPTION) {
                dispose();
                LoginUI.frame.setVisible(true);
            }
        } else if (nomePainel.equals("Voltar")) {
            cardLayout.show(contentPanel, ultimaTela);
            telaAtual = ultimaTela;
        } else {
            ultimaTela = telaAtual;
            telaAtual = nomePainel;
            cardLayout.show(contentPanel, nomePainel);
        }
    }

    /**
     * Método para a criação dos botões do menu
     * @param nome Nome do botão
     * @return Botão personalizado
     */
    public JButton criaBotaoMenu(String nome) {
        JButton button = new JButton(nome);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        return button;
    }

    /**
     * Método para a criação do painel que vai conter os botões
     * @param user Usuário logado
     * @return Retorna o painel com os botões
     */
    public JPanel botoesNavegacao(Usuario user) {
        JPanel menuButtonsPanel = new JPanel();
        menuButtonsPanel.setLayout(new BoxLayout(menuButtonsPanel, BoxLayout.Y_AXIS));

        ArrayList<JButton> buttons = new ArrayList<>();

        buttons.add(criaBotaoMenu("Principal"));

        if (user instanceof Medico) {
            buttons.add(criaBotaoMenu("Exames"));
            buttons.add(criaBotaoMenu("Laudos"));
            buttons.add(criaBotaoMenu("Consultas"));
            buttons.add(criaBotaoMenu("Receitas"));
            buttons.add(criaBotaoMenu("Cadastrar paciente"));
        } else if (user instanceof Paciente) {
            buttons.add(criaBotaoMenu("Verificar consultas"));
            buttons.add(criaBotaoMenu("Verificar laudos"));
            buttons.add(criaBotaoMenu("Verificar exames"));
            buttons.add(criaBotaoMenu("Verificar receitas"));
            buttons.add(criaBotaoMenu("Agendar consulta"));
        }

        buttons.add(criaBotaoMenu("Sair"));

        for (JButton btn : buttons) {
            menuButtonsPanel.add(btn);
            btn.addActionListener(this);
        }

        return menuButtonsPanel;
    }

    /**
     * Método para criar os paineis com o conteudo de cada área
     * @param user Usuário logado
     * @param <T> Tipo do usuário, médico ou paciente
     */
    public <T extends Usuario> void criaPaineisConteudo(T user) {

        contentPanel.add(painelPrincipal(user), "Principal");

        if (user instanceof Medico) {
            contentPanel.add(telaAreaDosExames(this), "Exames");
            contentPanel.add(LaudosMedicoUI.telaAreaDeLaudos(this), "Laudos");
            contentPanel.add(ReceitasMedicoUI.telaAreaDasReceitas(this), "Receitas");
            contentPanel.add(ConsultasMedicoUI.telaAreaDasConsultas(this), "Consultas");
            contentPanel.add(EncaixeMedicoUI.telaAgendarEncaixe((Medico) user, this), "Agendar novo encaixe");
            contentPanel.add(CadastroUI.cadastroPaciente( (Medico) user, this), "Cadastrar paciente");
            contentPanel.add(ExamesMedicoUI.telaNovoExame((Medico) user, this), "Prescrever novo exame");
            contentPanel.add(ExamesMedicoUI.showExamesFeitosPeloMedico((Medico) user, this), "Verificar exames");
            contentPanel.add(LaudosMedicoUI.showExamesDisponiveisParaLaudo((Medico) user, this), "Prescrever novo laudo");
            contentPanel.add(LaudosMedicoUI.showLaudos((Medico) user, this), "Verificar laudos");
            contentPanel.add(ReceitasMedicoUI.showPacientesParaPrescreverReceita((Medico) user, this), "Prescrever nova receita");
            contentPanel.add(ReceitasMedicoUI.showReceitas((Medico) user, this), "Verificar receitas");
            contentPanel.add(ConsultasMedicoUI.showConsultasMarcadas((Medico) user, this), "Verificar consultas");
        } else if (user instanceof Paciente) {
            contentPanel.add(ConsultasPacienteUI.telaVerificarConsultas((Paciente) user), "Verificar consultas");
            contentPanel.add(LaudosPacienteUI.telaVerificarLaudo((Paciente) user, this), "Verificar laudos");
            contentPanel.add(ExamesPacienteUI.showExamesDoPaciente((Paciente) user, this), "Verificar exames");
            contentPanel.add(ConsultasPacienteUI.showMedicos((Paciente) user, this), "Agendar consulta");
            contentPanel.add(ReceitasPacienteUI.telaVerificarReceitas((Paciente) user, this), "Verificar receitas");
        }
    }

    /**
     * Método para realizar a atualização dos paineis
     * @param user Usuário logado
     */
    public void atualizaPainel(Usuario user) {
        Component[] components = contentPanel.getComponents();

        for (Component comp : components) {
            contentPanel.remove(comp);
        }

        criaPaineisConteudo(user);
        cardLayout.show(contentPanel, telaAtual);
    }

    /**
     * Método para criar o painel de conteúdo principal
     * @param user Usuário logado
     * @return Painel de conteúdo principal
     * @param <T> Tipo do usuário, médico ou paciente
     */
    protected static <T extends Usuario> JPanel painelPrincipal(T user) {
        LoginUI.frame.setVisible(false);

        JPanel painelPrincipal = new JPanel();

        painelPrincipal.setVisible(true);
        painelPrincipal.setSize(800, 600);
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);

        // ============ Informações do usuário ============
        JPanel infoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g.create();

                GradientPaint gradient = new GradientPaint(0, 0, Color.decode("#5e8ab3"), getWidth(), getHeight(), Color.decode("#67dcff"));

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.dispose();
            }
        };

        infoPanel.setBackground(Color.decode("#67dcff"));
        infoPanel.setLayout(new GridLayout(1, 2));

        JLabel nomeLabel = new JLabel();
        JLabel info1Label = new JLabel();
        JLabel info2Label = new JLabel();

        if (user instanceof Medico medicoLogado) {
            nomeLabel.setText("Dr. " + medicoLogado.getNome());
            info1Label.setText("CRM: " + medicoLogado.getCRM());
            info2Label.setText("Área de atuação: " + medicoLogado.getAreaAtuacao());
        } else if (user instanceof Paciente pacienteLogado) {
            nomeLabel.setText(pacienteLogado.getNome());
            info1Label.setText("CPF: " + pacienteLogado.getCpf());
            info2Label.setText("Idade: " + pacienteLogado.getIdade() + " anos");
        }

        infoPanel.add(nomeLabel);
        nomeLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        nomeLabel.setForeground(Color.WHITE);

        JPanel info2Panel = new JPanel();
        info2Panel.setLayout(new GridBagLayout());
        info2Panel.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;

        info2Panel.add(info1Label, constraints);
        info1Label.setFont(new Font("Roboto", Font.BOLD, 14));
        info1Label.setForeground(Color.WHITE);

        constraints.gridy = 1;

        info2Panel.add(info2Label, constraints);
        info2Label.setFont(new Font("Roboto", Font.BOLD, 14));
        info2Label.setForeground(Color.WHITE);

        infoPanel.add(info2Panel);

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);

        // ============ Consultas do dia ============
        JPanel consultasPanel = new JPanel();
        consultasPanel.setBackground(Color.WHITE);
        consultasPanel.setLayout(new GridBagLayout());

        JLabel consultasDiaLabel = new JLabel("Consultas do Dia");
        constraints.gridx = 0;
        constraints.gridy = 0;
        consultasPanel.add(consultasDiaLabel, constraints);

        ArrayList<Consulta> consultas = null;

        if (user instanceof Medico medico) {
            consultas = ConsultaSQL.selectAllConsultasFromMedico(medico.getCpf(), new Date(Calendar.getInstance().getTimeInMillis()));
        } else if (user instanceof Paciente paciente) {
            consultas = ConsultaSQL.selectAllConsultasFromPaciente(paciente.getCpf(), new Date(Calendar.getInstance().getTimeInMillis()));
        }

        String[] listaConsulta;

        if (consultas.size() == 0) {
            listaConsulta = new String[1];
            listaConsulta[0] = "Não há consultas agendadas para hoje";
        } else {
            listaConsulta = new String[consultas.size()];
        }

        int i = 0;
        for (Consulta rc: consultas) {
            if(user instanceof Paciente) {
                if (!rc.isRealizada())
                    listaConsulta[i] = "Consulta marcada - " + "Dr. " + rc.getMedico().getNome() + " - " + "Data: " + rc.getData().toString() + " - " + "Horário: " + rc.getHorario() + " horas";
            }else if( user instanceof Medico){
                if (!rc.isRealizada())
                    listaConsulta[i] = "Consulta marcada - " + "Paciente: " + rc.getPaciente().getNome() + " - " + "Data: " + rc.getData().toString() + " - " + "Horário: " + rc.getHorario() + " horas";
            }
            i++;
        }

        JList<String> list = new JList<>(listaConsulta);
        list.setEnabled(false);

        constraints.gridy = 1;
        consultasPanel.add(list, constraints);

        painelPrincipal.add(consultasPanel, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método para mostrar o frame
     * @param user Usuário logado
     */
    public static void mostrarTela(Usuario user) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
                public void run() {
                TelaLogadaUI tela = new TelaLogadaUI(user);
                tela.setVisible(true);
            }
        });
    }

    /**
     * Método getter para retornar o painel de conteúdos
     * @return Painel de conteúdos
     */
    public JPanel getContentPanel() {
        return contentPanel;
    }

    /**
     * Método getter para retornar o CardLayout
     * @return CardLayout
     */
    public CardLayout getCardLayout() {
        return cardLayout;
    }
}
