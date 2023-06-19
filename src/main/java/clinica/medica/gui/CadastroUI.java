package clinica.medica.gui;

import clinica.medica.database.HorariosSQL;
import clinica.medica.database.UsuariosSQL;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

public class CadastroUI {

    /**
     * Método de público para chamar a tela de cadastro do médico.
     */
    public static void telaCadastroMedico() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showCadastroMedico();
            }
        });
    }

    /**
     * Método privado para implementação da tela de cadastro do médico.
     */
    private static void showCadastroMedico() {
        String[] horariosEntrada = {"8","10","12","14","16"};
        String[] horariosSaida = {"10","12","14","16","18"};

        LoginUI.frame.setVisible(false);

        JFrame frame = new JFrame("Cadastro médico");
        frame.setLocationRelativeTo(LoginUI.frame);

        frame.setLayout(new GridBagLayout());
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel usernameLabel = new JLabel("Nome");
        JLabel cpfLabel = new JLabel("CPF");
        JLabel emailLabel = new JLabel("E-mail");
        JLabel areaLabel = new JLabel("Área de atuação");
        JLabel crmLabel = new JLabel("CRM");
        JLabel passwordLabel = new JLabel("Senha");
        JLabel horariosEntradaLabel = new JLabel("Escolha o horário de entrada");
        JLabel horariosSaidaLabel = new JLabel("Escolha o horário de saida");

        JTextField usernameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField areaField = new JTextField(20);
        JTextField crmField = new JTextField(20);

        JPasswordField passwordField = new JPasswordField(20);

        JFormattedTextField cpfField = inicializaCpf();

        JButton cadastroButton = new JButton("Cadastrar");

        JComboBox horarioEntrada = new JComboBox<>(horariosEntrada);
        JComboBox horarioSaida = new JComboBox<>(horariosSaida);

        horarioEntrada.setSelectedIndex(0);
        horarioSaida.setSelectedIndex(4);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        constraints.gridy = 1;
        frame.add(usernameLabel, constraints);

        constraints.gridy = 2;
        frame.add(usernameField, constraints);

        constraints.gridy = 3;
        frame.add(emailLabel, constraints);

        constraints.gridy = 4;
        frame.add(emailField, constraints);

        constraints.gridy = 5;
        frame.add(cpfLabel, constraints);

        constraints.gridy = 6;
        frame.add(cpfField, constraints);

        constraints.gridy = 7;
        frame.add(areaLabel, constraints);

        constraints.gridy = 8;
        frame.add(areaField, constraints);

        constraints.gridy = 9;
        frame.add(crmLabel, constraints);

        constraints.gridy = 10;
        frame.add(crmField, constraints);

        constraints.gridy = 11;
        frame.add(horariosEntradaLabel, constraints);

        constraints.gridy = 12;
        frame.add(horarioEntrada, constraints);

        constraints.gridy = 13;
        frame.add(horariosSaidaLabel, constraints);

        constraints.gridy = 14;
        frame.add(horarioSaida, constraints);

        constraints.gridy = 15;
        frame.add(passwordLabel, constraints);

        constraints.gridy = 16;
        frame.add(passwordField, constraints);

        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridy = 17;
        frame.add(cadastroButton, constraints);

        //Quando clica no botão para cadastrar, pega os dados e chama a função de cadastrar o médico
        cadastroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String nome = usernameField.getText();
                String cpf = cpfField.getText();
                cpf = cpf.replaceAll("[.-]", "");
                String email = emailField.getText();
                String area = areaField.getText();
                String crm = crmField.getText();
                char[] senha = passwordField.getPassword();
                String password = new String(senha);
                Integer entrada = Integer.parseInt((String)horarioEntrada.getSelectedItem());
                Integer saida = Integer.parseInt((String)horarioSaida.getSelectedItem());

                if (UsuariosSQL.cadastroMedico(nome, cpf, email, password, area, crm, frame, entrada, saida)) {

                    for(int i = 0; i < saida - entrada; i++){
                        Integer hEnt = entrada + i;
                        String ent = hEnt.toString();
                        HorariosSQL.salvarHorarios(ent,cpf);
                    }

                    JOptionPane.showMessageDialog(frame, "Cadastro realizado com sucesso!");
                    frame.dispose();
                    LoginUI.frame.setVisible(true);
                    usernameField.setText("");
                    cpfField.setText("");
                    emailField.setText("");
                    areaField.setText("");
                    crmField.setText("");
                    passwordField.setText("");
                }

            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LoginUI.frame.setVisible(true);
            }
        });
    }

    /**
     * Método privado para a implementação da tela de cadastro do paciente.
     */
    protected static JPanel cadastroPaciente() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

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
        infoPanel.setLayout(new GridBagLayout());
        JLabel cadastroLabel = new JLabel("Cadastro de paciente");

        String[] sexo = {"Masculino", "Feminino", "Outro"};
        JPanel panelCadastro = new JPanel();

        panelCadastro.setLayout(new GridBagLayout());

        JLabel usernameLabel = new JLabel("Nome");
        JLabel cpfLabel = new JLabel("CPF");
        JLabel emailLabel = new JLabel("E-mail");
        JLabel enderecoLabel = new JLabel("Endereco");
        JLabel sexoLabel = new JLabel("Sexo");
        JLabel idadeLabel = new JLabel("Idade");
        JLabel alturaLabel = new JLabel("Altura");
        JLabel pesoLabel = new JLabel("Peso");
        JLabel passwordLabel = new JLabel("Senha");

        JTextField usernameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField enderecoField = new JTextField(20);

        JFormattedTextField cpfField = inicializaCpf();

        JComboBox<String> sexoCombo = new JComboBox<>(sexo);
        sexoCombo.setSelectedIndex(2);

        JTextField idadeField = new JTextField(20);
        JTextField alturaField = new JTextField(20);
        JTextField pesoField = new JTextField(20);

        JPasswordField passwordField = new JPasswordField(20);

        JButton cadastroButton = new JButton("Cadastrar paciente");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        infoPanel.add(cadastroLabel);
        cadastroLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        cadastroLabel.setForeground(Color.WHITE);

        constraints.gridy = 1;
        panelCadastro.add(usernameLabel, constraints);

        constraints.gridy = 2;
        panelCadastro.add(usernameField, constraints);

        constraints.gridy = 3;
        panelCadastro.add(emailLabel, constraints);

        constraints.gridy = 4;
        panelCadastro.add(emailField, constraints);

        constraints.gridy = 5;
        panelCadastro.add(cpfLabel, constraints);

        constraints.gridy = 6;
        panelCadastro.add(cpfField, constraints);

        constraints.gridy = 7;
        panelCadastro.add(enderecoLabel, constraints);

        constraints.gridy = 8;
        panelCadastro.add(enderecoField, constraints);

        constraints.gridy = 9;
        panelCadastro.add(sexoLabel, constraints);

        constraints.gridy = 10;
        panelCadastro.add(sexoCombo, constraints);

        constraints.gridy = 11;
        panelCadastro.add(idadeLabel, constraints);

        constraints.gridy = 12;
        panelCadastro.add(idadeField, constraints);

        constraints.gridy = 13;
        panelCadastro.add(alturaLabel, constraints);

        constraints.gridy = 14;
        panelCadastro.add(alturaField, constraints);

        constraints.gridy = 15;
        panelCadastro.add(pesoLabel, constraints);

        constraints.gridy = 16;
        panelCadastro.add(pesoField, constraints);

        constraints.gridy = 17;
        panelCadastro.add(passwordLabel, constraints);

        constraints.gridy = 18;
        panelCadastro.add(passwordField, constraints);

        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridy = 19;
        panelCadastro.add(cadastroButton, constraints);

        //Quando clica no botão para cadastrar, pega os dados e chama a função de cadastrar o paciente
        cadastroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idade = 0;
                double altura = 0, peso = 0;
                String nome = usernameField.getText();
                String cpf = cpfField.getText();
                cpf = cpf.replaceAll("[.-]", "");
                String email = emailField.getText();
                String endereco = enderecoField.getText();
                String sexo = (String) sexoCombo.getSelectedItem();
                try {
                    idade = Integer.parseInt(idadeField.getText());
                    altura = Double.parseDouble(alturaField.getText());
                    peso = Double.parseDouble(pesoField.getText());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
                char[] senha = passwordField.getPassword();
                String password = new String(senha);

                if (UsuariosSQL.cadastroPaciente(nome, cpf, email, password, endereco, sexo, idade, altura, peso, panelCadastro)) {
                    JOptionPane.showMessageDialog(panelCadastro, "Cadastro do cliente realizado com sucesso!");
                    usernameField.setText("");
                    cpfField.setText("");
                    emailField.setText("");
                    enderecoField.setText("");
                    sexoCombo.setSelectedIndex(2);
                    idadeField.setText("");
                    alturaField.setText("");
                    pesoField.setText("");
                    passwordField.setText("");
                }
            }
        });

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(panelCadastro, BorderLayout.CENTER);

        return painelPrincipal;
    }

    public static JFormattedTextField inicializaCpf() {
        JFormattedTextField cpfTest;

        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            cpfTest = new JFormattedTextField(cpfMask);
            cpfTest.setColumns(20);
        } catch (ParseException e) {
            e.printStackTrace();
            cpfTest = new JFormattedTextField();
        }

        return cpfTest;
    }
}
