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

                if (UsuariosSQL.cadastroMedico(nome, cpf, email, password, area, crm, frame)) {

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
        String[] sexo = {"Masculino", "Feminino", "Outro"};
        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());

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

        constraints.gridy = 1;
        panel.add(usernameLabel, constraints);

        constraints.gridy = 2;
        panel.add(usernameField, constraints);

        constraints.gridy = 3;
        panel.add(emailLabel, constraints);

        constraints.gridy = 4;
        panel.add(emailField, constraints);

        constraints.gridy = 5;
        panel.add(cpfLabel, constraints);

        constraints.gridy = 6;
        panel.add(cpfField, constraints);

        constraints.gridy = 7;
        panel.add(enderecoLabel, constraints);

        constraints.gridy = 8;
        panel.add(enderecoField, constraints);

        constraints.gridy = 9;
        panel.add(sexoLabel, constraints);

        constraints.gridy = 10;
        panel.add(sexoCombo, constraints);

        constraints.gridy = 11;
        panel.add(idadeLabel, constraints);

        constraints.gridy = 12;
        panel.add(idadeField, constraints);

        constraints.gridy = 13;
        panel.add(alturaLabel, constraints);

        constraints.gridy = 14;
        panel.add(alturaField, constraints);

        constraints.gridy = 15;
        panel.add(pesoLabel, constraints);

        constraints.gridy = 16;
        panel.add(pesoField, constraints);

        constraints.gridy = 17;
        panel.add(passwordLabel, constraints);

        constraints.gridy = 18;
        panel.add(passwordField, constraints);

        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridy = 19;
        panel.add(cadastroButton, constraints);

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

                if (UsuariosSQL.cadastroPaciente(nome, cpf, email, password, endereco, sexo, idade, altura, peso, panel)) {
                    JOptionPane.showMessageDialog(panel, "Cadastro do cliente realizado com sucesso!");
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

        return panel;
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
