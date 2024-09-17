package clinica.medica.gui.recursos;

import clinica.medica.documentos.Imprimivel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImpressaoUI {
    public static <T extends Imprimivel> void imprimirDocumento(T documento) {
        JFrame documentFrame = new JFrame(documento.imprimeTipo());
        documentFrame.setLayout(new BorderLayout());
        documentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints constraints = new GridBagConstraints();

        // ============ Painéis ============
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new GridBagLayout());
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

        JPanel infosTopoPanel = new JPanel();
        infosTopoPanel.setLayout(new GridBagLayout());
        infosTopoPanel.setBackground(Color.WHITE);

        JPanel topoPanel = new JPanel();
        topoPanel.setLayout(new BoxLayout(topoPanel, BoxLayout.X_AXIS));
        topoPanel.setBackground(Color.WHITE);

        JPanel infosCentralPanel = new JPanel(new GridBagLayout());
        infosCentralPanel.setLayout(new GridBagLayout());
        infosCentralPanel.setBackground(Color.WHITE);
        infosCentralPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(Color.WHITE);
        centralPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));

        JPanel finalPanel = new JPanel();
        finalPanel.setLayout(new GridBagLayout());
        finalPanel.setBackground(Color.WHITE);

        // ---------------------------------- TOPO ------------------------------------------

        // ============ Logo ============
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;

        try {
            BufferedImage logo = ImageIO.read(ImpressaoUI.class.getResourceAsStream("/images/logoDocs.png"));
            ImageIcon logoIcon = new ImageIcon(logo);
            JLabel logoLabel = new JLabel(logoIcon);
            logoPanel.add(logoLabel, constraints);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ============ Tipo do Documento ============
        JLabel tipoDocumentoLabel = new JLabel(documento.imprimeTipo());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        infosTopoPanel.add(tipoDocumentoLabel, constraints);

        // ============ Endereço Clínica ============
        JLabel enderecoClinicaLabel = new JLabel("Rua Cristovão Colombo - (17) 3222-3174");

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        infosTopoPanel.add(enderecoClinicaLabel, constraints);

        topoPanel.add(logoPanel);
        topoPanel.add(infosTopoPanel);

        // ---------------------------------- MEIO ------------------------------------------
        constraints.insets = new Insets(0, 0, 0, 0);

        // ============ Info Documento ============
        JLabel docInfoLabel = new JLabel(documento.imprimeInfo());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        infosCentralPanel.add(docInfoLabel, constraints);

        // ============ Infos Paciente ============
        JLabel nomePacienteLabel = new JLabel(documento.imprimeNomePaciente());
        JLabel idadeLabel = new JLabel(documento.imprimeIdade());
        JLabel sexoLabel = new JLabel(documento.imprimeSexo());
        JLabel dataLabel = new JLabel(documento.imprimeData());

        constraints.gridx = 0;
        constraints.gridy = 1;
        infosCentralPanel.add(nomePacienteLabel, constraints);
        constraints.gridy = 2;
        infosCentralPanel.add(idadeLabel, constraints);
        constraints.gridy = 3;
        infosCentralPanel.add(sexoLabel, constraints);
        constraints.gridy = 4;
        infosCentralPanel.add(dataLabel, constraints);

        centralPanel.add(infosCentralPanel);

        // ============ Comentário do Médico ============
        JLabel comentariosLabel = new JLabel("<html>" + documento.imprimeComentarios() + "</html>");
        comentariosLabel.setBackground(Color.WHITE);
        comentariosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centralPanel.add(comentariosLabel);

        // ---------------------------------- Final ------------------------------------------

        // ============ Infos do Médico ============
        JLabel medicoLabel = new JLabel(documento.imprimeNomeMedico());
        JLabel infoMedicoLabel = new JLabel(documento.imprimeCrmAtuacaoMedico());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        finalPanel.add(medicoLabel, constraints);
        constraints.gridy = 1;
        finalPanel.add(infoMedicoLabel, constraints);

        // ============ Fontes ============
        Font fonteGrande = new Font(enderecoClinicaLabel.getFont().getFontName(), enderecoClinicaLabel.getFont().getStyle(), 30);
        Font fontePequena = new Font(enderecoClinicaLabel.getFont().getFontName(), enderecoClinicaLabel.getFont().getStyle(), 15);

        tipoDocumentoLabel.setFont(fonteGrande);
        enderecoClinicaLabel.setFont(fonteGrande);
        docInfoLabel.setFont(fontePequena);
        nomePacienteLabel.setFont(fontePequena);
        idadeLabel.setFont(fontePequena);
        sexoLabel.setFont(fontePequena);
        dataLabel.setFont(fontePequena);
        comentariosLabel.setFont(fontePequena);

        // ============ Ajuste Frame ============
        documentFrame.add(topoPanel, BorderLayout.NORTH);
        documentFrame.add(centralPanel, BorderLayout.CENTER);
        documentFrame.add(finalPanel, BorderLayout.SOUTH);
        documentFrame.setSize(1000, 800);
        documentFrame.setVisible(true);
    }
}
