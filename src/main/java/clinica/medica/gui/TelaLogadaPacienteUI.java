package clinica.medica.gui;

import clinica.medica.database.MedicosSQL;
import clinica.medica.database.PacientesSQL;
import clinica.medica.database.ReceitasSQL;
import clinica.medica.documentos.Exame;
import clinica.medica.documentos.Laudo;
import clinica.medica.documentos.Receita;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TelaLogadaPacienteUI {

    protected static JPanel telaVerificarConsulta() {
        JPanel painelConsulta = new JPanel();

        painelConsulta.setLayout(new GridBagLayout());
        painelConsulta.setSize(800, 600);

        JTextArea tempLabel = new JTextArea("Verificar a consulta");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        constraints.gridy = 1;
        painelConsulta.add(tempLabel);

        return painelConsulta;
    }

    protected static JPanel telaAgendarConsulta() {
        JPanel painelConsulta = new JPanel();

        painelConsulta.setLayout(new GridBagLayout());
        painelConsulta.setSize(800, 600);

        JTextArea tempLabel = new JTextArea("Agendar a consulta");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        constraints.gridy = 1;
        painelConsulta.add(tempLabel);

        return painelConsulta;
    }

    protected static JPanel telaVerificarLaudo(Paciente pacienteLogado, TelaLogadaUI telaLogada) {
        ArrayList<Laudo> laudos = PacientesSQL.verificarLaudos(pacienteLogado.getCpf());
        int i = 0;
        JPanel painelLaudo = new JPanel();

        painelLaudo.setLayout(new GridBagLayout());
        painelLaudo.setSize(800, 600);

        JTextArea tempLabel = new JTextArea("Laudos");

        JButton imprimirLaudoButton = new JButton("Visualizar laudo");
        JButton voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(telaLogada);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        String[] listaLaudo = new String[laudos.size()];

        for (Laudo ex : laudos) {
            listaLaudo[i] = "Laudo - " + ex.getPaciente().getNome() + " - " + ex.getExame().getTipo() + " - " + ex.getExame().getId();
            i++;
        }


        JList<String> list = new JList<>(listaLaudo);
        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(800, 600));

        constraints.gridy = 0;
        painelLaudo.add(tempLabel, constraints);
        constraints.gridy = 1;
        painelLaudo.add(scrollPanel, constraints);
        constraints.gridy = 2;
        painelLaudo.add(imprimirLaudoButton, constraints);
        constraints.gridy = 3;
        painelLaudo.add(voltarButton, constraints);

        imprimirLaudoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String laudoSelecionado = list.getSelectedValue();
                String[] elementos = laudoSelecionado.split("-");
                int id = Integer.parseInt(elementos[3].strip());
                Laudo laudo = new Laudo(id);
             //   TelaLogadaMedicoUI.imprimirLaudo(medicoLogado, laudo);
            }
        });

        return painelLaudo;
    }

    protected static JPanel telaVerificarExame(Paciente pacienteLoagado, TelaLogadaUI telaLogada) {
        int i = 0;
        ArrayList<Exame> exames = PacientesSQL.verificarExamesPaciente(pacienteLoagado.getCpf());
        JPanel painelExame = new JPanel();

        painelExame.setLayout(new GridBagLayout());
        painelExame.setSize(800, 600);

        JTextArea tempLabel = new JTextArea("Exames");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        constraints.gridy = 1;
        painelExame.add(tempLabel);

        JButton imprimirExameButton = new JButton("Visualizar exame");
        JButton voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(telaLogada);

        String[] listaExame = new String[exames.size()];

        for (Exame ex : exames) {
            listaExame[i] = "Exame - " + ex.getPaciente().getNome() + " - " + ex.getTipo() + " - " + ex.getId();
            i++;
        }

        JList<String> list = new JList<>(listaExame);
        JScrollPane scrollPanel = new JScrollPane(list);

        scrollPanel.setPreferredSize(new Dimension(800, 600));

        constraints.gridy = 0;
        painelExame.add(tempLabel);
        constraints.gridy = 1;
        painelExame.add(scrollPanel, constraints);
        constraints.gridy = 2;
        painelExame.add(imprimirExameButton, constraints);
        constraints.gridy = 3;
        painelExame.add(voltarButton, constraints);

        imprimirExameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String exameSelecionado = list.getSelectedValue();
                String[] elementos = exameSelecionado.split("-");
                int id = Integer.parseInt(elementos[3].strip());
                Exame exame = new Exame(id);
                //TelaLogadaMedicoUI.imprimirExame(exame);
            }
        });

        return painelExame;
    }

    protected static JPanel telaVerificarReceitas(Paciente pacienteLogado, TelaLogadaUI telaLogada) {
        ArrayList<Receita> receitas = ReceitasSQL.selectAllReceitas();
        int i = 0;
        JPanel painelReceita = new JPanel();

        painelReceita.setLayout(new GridBagLayout());
        painelReceita.setSize(800, 600);

        JTextArea tempLabel = new JTextArea("Receitas");
        JButton imprimirReceitaButton = new JButton("Visualizar receita");
        JButton voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(telaLogada);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        String[] listaReceita = new String[receitas.size()];

        for (Receita rc: receitas) {
            listaReceita[i] = "Receita - " + rc.getPaciente().getNome() + " - " + rc.getNomeDoRemedio() + " - " + rc.getMedico().getNome() + " - " + rc.getId();
            i++;
        }

        JList<String> list = new JList<>(listaReceita);
        JScrollPane scrollPanel = new JScrollPane(list);

        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 0;
        painelReceita.add(tempLabel, constraints);
        constraints.gridy = 1;
        painelReceita.add(scrollPanel, constraints);
        constraints.gridy = 2;
        painelReceita.add(imprimirReceitaButton, constraints);
        constraints.gridy = 3;
        painelReceita.add(voltarButton, constraints);

        imprimirReceitaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String receitaSelecionada = list.getSelectedValue();
                String[] elementos = receitaSelecionada.split("-");
                int id = Integer.parseInt(elementos[3].strip());

                Receita receita = new Receita(id);
                TelaLogadaMedicoUI.imprimirDocumento(receita);
            }
        });

        return painelReceita;
    }
}
