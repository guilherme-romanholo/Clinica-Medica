package clinica.medica.gui.telas;

import clinica.medica.database.UsuariosSQL;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe para implementação da tela de Login
 */
public class LoginUI {
    protected static JFrame frame;

    /**
     * Método público para chamar a tela de login.
     */
    public static void chamarTela() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showLogin();
            }
        });
    }

    /**
     * Método privado para a implementação da tela de 'login'.
     */
    private static void showLogin() {
        frame = new JFrame("Login");
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(450, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painel = new JPanel() {
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

        painel.setLayout(new BorderLayout());

        JLabel logoLabel = RecursosUI.criaImagemLabel("/images/logoMini.png", 160, 100);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;

        JPanel centralPanel = new JPanel();
        centralPanel.setOpaque(false);
        centralPanel.setLayout(new GridBagLayout());

        centralPanel.add(logoLabel, constraints);

        constraints.insets = new Insets(0, 0, 5, 0);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel emailLabel = RecursosUI.criaImagemLabel("/images/iconMail.png", 30, 30);
        centralPanel.add(emailLabel, constraints);

        JTextField emailField = new JTextField(20);
        configuraField(emailField);
        constraints.gridx = 1;
        centralPanel.add(emailField, constraints);

        JLabel senhaLabel = RecursosUI.criaImagemLabel("/images/iconLock.png", 30, 30);
        constraints.gridx = 0;
        constraints.gridy = 2;
        centralPanel.add(senhaLabel, constraints);

        JPasswordField senhaField = new JPasswordField(20);
        configuraField(senhaField);
        constraints.gridx = 1;
        centralPanel.add(senhaField, constraints);

        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.fill = GridBagConstraints.CENTER;

        JButton loginButton = criaLoginButton("Login");
        constraints.gridy = 3;
        centralPanel.add(loginButton, constraints);

        JButton cadastroButton = criaLoginButton("Cadastrar-se");
        constraints.gridy = 4;
        centralPanel.add(cadastroButton, constraints);

        painel.add(centralPanel, BorderLayout.CENTER);

        frame.add(painel);

        cadastroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPasswordField passwordField = new JPasswordField();
                int option = JOptionPane.showOptionDialog(frame, passwordField, "Digite a senha de ADMIN: ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

                // Verifica a opção selecionada pelo usuário
                if (option == JOptionPane.OK_OPTION) {
                    char[] password = passwordField.getPassword();
                    String senha = new String(password);
                    if(senha.equals("12345"))
                        CadastroUI.telaCadastroMedico();
                    else
                        JOptionPane.showMessageDialog(frame, "Senha de ADMIN incorreta, tente novamente!", "Senha incorreta", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String email = emailField.getText();
                char[] senha = senhaField.getPassword();
                String password = new String(senha);
                Paciente pacienteLogado = UsuariosSQL.loginPaciente(email, password);
                Medico medicoLogado = UsuariosSQL.loginMedico(email, password);

                if (medicoLogado != null) {
                    TelaLogadaUI.mostrarTela(medicoLogado);
                    emailField.setText("");
                    senhaField.setText("");
                } else if (pacienteLogado != null) {
                    TelaLogadaUI.mostrarTela(pacienteLogado);
                    emailField.setText("");
                    senhaField.setText("");
                } else {
                    JOptionPane.showMessageDialog(painel, "Não foi possível logar o médico/paciente, tente novamente!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    senhaField.setText("");
                }
            }
        });
    }

    /**
     * Método para configurar os campos de texto
     * @param field Campo de texto
     * @param <T> Tipo do campo de texto
     */
    public static <T extends JTextComponent> void configuraField(T field) {
        field.setCaretColor(Color.WHITE);
        field.setForeground(Color.WHITE);
        field.setFont(new Font("Roboto", Font.BOLD, 14));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        field.setOpaque(false);
    }

    /**
     * Método para criar os botões de login
     * @param nome Nome do botão
     * @return Retorna o botão personalizado
     */
    public static JButton criaLoginButton(String nome) {
        JButton button = new JButton(nome){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, width, height, height, height);

                g2.setColor(getForeground());
                g2.drawString(getText(), 10, 20);

                g2.dispose();
            }
        };
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setBackground(Color.decode("#67dcff"));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBorderPainted(false);
        return button;
    }
}