package clinica.medica.gui.telas;

import clinica.medica.database.HorariosSQL;
import clinica.medica.database.UsuariosSQL;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.usuarios.Medico;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

/**
 * Classe utilizada para a criação da interface gráfica do cadastro
 */
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
        LoginUI.frame.setVisible(false);

        JFrame frame = new JFrame("Cadastro médico");
        frame.setLocationRelativeTo(LoginUI.frame);
        frame.setLayout(new GridBagLayout());
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JLabel usernameLabel = new JLabel("Nome");
        constraints.gridy = 1;
        frame.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField(20);
        constraints.gridy = 2;
        frame.add(usernameField, constraints);

        JLabel emailLabel = new JLabel("E-mail");
        constraints.gridy = 3;
        frame.add(emailLabel, constraints);

        JTextField emailField = new JTextField(20);
        constraints.gridy = 4;
        frame.add(emailField, constraints);

        JLabel cpfLabel = new JLabel("CPF");
        constraints.gridy = 5;
        frame.add(cpfLabel, constraints);

        JFormattedTextField cpfField = inicializaCpf();
        constraints.gridy = 6;
        frame.add(cpfField, constraints);

        JLabel areaLabel = new JLabel("Área de atuação");
        constraints.gridy = 7;
        frame.add(areaLabel, constraints);

        JTextField areaField = new JTextField(20);
        constraints.gridy = 8;
        frame.add(areaField, constraints);

        JLabel crmLabel = new JLabel("CRM");
        constraints.gridy = 9;
        frame.add(crmLabel, constraints);

        JTextField crmField = new JTextField(20);
        constraints.gridy = 10;
        frame.add(crmField, constraints);

        JLabel horariosEntradaLabel = new JLabel("Escolha o horário de entrada");
        constraints.gridy = 11;
        frame.add(horariosEntradaLabel, constraints);

        String[] horariosEntrada = {"8","10","12","14","16"};
        JComboBox horarioEntrada = new JComboBox<>(horariosEntrada);
        horarioEntrada.setSelectedIndex(0);
        constraints.gridy = 12;
        frame.add(horarioEntrada, constraints);

        JLabel horariosSaidaLabel = new JLabel("Escolha o horário de saida");
        constraints.gridy = 13;
        frame.add(horariosSaidaLabel, constraints);

        String[] horariosSaida = {"10","12","14","16","18"};
        JComboBox horarioSaida = new JComboBox<>(horariosSaida);
        horarioSaida.setSelectedIndex(4);
        constraints.gridy = 14;
        frame.add(horarioSaida, constraints);

        JLabel passwordLabel = new JLabel("Senha");
        constraints.gridy = 15;
        frame.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridy = 16;
        frame.add(passwordField, constraints);


        JButton cadastroButton = new JButton("Cadastrar");
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridy = 17;
        frame.add(cadastroButton, constraints);

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
    protected static JPanel cadastroPaciente(Medico medicoLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Cadastro de paciente");

        JPanel panelCadastro = new JPanel();
        panelCadastro.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        JLabel usernameLabel = new JLabel("Nome");
        constraints.gridy = 1;
        panelCadastro.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField(20);
        constraints.gridy = 2;
        panelCadastro.add(usernameField, constraints);

        JLabel emailLabel = new JLabel("E-mail");
        constraints.gridy = 3;
        panelCadastro.add(emailLabel, constraints);

        JTextField emailField = new JTextField(20);
        constraints.gridy = 4;
        panelCadastro.add(emailField, constraints);

        JLabel cpfLabel = new JLabel("CPF");
        constraints.gridy = 5;
        panelCadastro.add(cpfLabel, constraints);

        JFormattedTextField cpfField = inicializaCpf();
        constraints.gridy = 6;
        panelCadastro.add(cpfField, constraints);

        JLabel enderecoLabel = new JLabel("Endereco");
        constraints.gridy = 7;
        panelCadastro.add(enderecoLabel, constraints);

        JTextField enderecoField = new JTextField(20);
        constraints.gridy = 8;
        panelCadastro.add(enderecoField, constraints);

        JLabel sexoLabel = new JLabel("Sexo");
        constraints.gridy = 9;
        panelCadastro.add(sexoLabel, constraints);

        String[] sexo = {"Masculino", "Feminino", "Outro"};
        JComboBox<String> sexoCombo = new JComboBox<>(sexo);
        sexoCombo.setSelectedIndex(2);
        constraints.gridy = 10;
        panelCadastro.add(sexoCombo, constraints);

        JLabel idadeLabel = new JLabel("Idade");
        constraints.gridy = 11;
        panelCadastro.add(idadeLabel, constraints);

        JTextField idadeField = new JTextField(20);
        constraints.gridy = 12;
        panelCadastro.add(idadeField, constraints);

        JLabel alturaLabel = new JLabel("Altura");
        constraints.gridy = 13;
        panelCadastro.add(alturaLabel, constraints);

        JTextField alturaField = new JTextField(20);
        constraints.gridy = 14;
        panelCadastro.add(alturaField, constraints);

        JLabel pesoLabel = new JLabel("Peso");
        constraints.gridy = 15;
        panelCadastro.add(pesoLabel, constraints);

        JTextField pesoField = new JTextField(20);
        constraints.gridy = 16;
        panelCadastro.add(pesoField, constraints);

        JLabel passwordLabel = new JLabel("Senha");
        constraints.gridy = 17;
        panelCadastro.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridy = 18;
        panelCadastro.add(passwordField, constraints);

        JButton cadastroButton = new JButton("Cadastrar paciente");
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridy = 19;
        panelCadastro.add(cadastroButton, constraints);

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
                    telaLogada.atualizaPainel(medicoLogado);
                }
            }
        });

        painelPrincipal.add(infoPanel, BorderLayout.NORTH);
        painelPrincipal.add(panelCadastro, BorderLayout.CENTER);

        return painelPrincipal;
    }

    /**
     * Método para a criação do campo formatado do CPF
     * @return Retorna o campo do CPF formatado
     */
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
