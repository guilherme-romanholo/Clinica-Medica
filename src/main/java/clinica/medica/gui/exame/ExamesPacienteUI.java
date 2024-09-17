package clinica.medica.gui.exame;

import clinica.medica.database.PacientesSQL;
import clinica.medica.documentos.Exame;
import clinica.medica.gui.recursos.ImpressaoUI;
import clinica.medica.gui.recursos.JListFilter;
import clinica.medica.gui.recursos.RecursosUI;
import clinica.medica.gui.telas.TelaLogadaUI;
import clinica.medica.usuarios.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
/**
 * Classe que possui os métodos usados na criação da interface da parte de exames do paciente
 */
public class ExamesPacienteUI {
    /**
     * Método que montará o painel com todos os exames do paciente
     * @param pacienteLoagado paciente logado no sistema
     * @param telaLogada tela principal que conterá o painel das consultas marcadas
     * @return retorna o painel das consultas marcadas para adicioná-lo na tela principal
     */
    public static JPanel showExamesDoPaciente(Paciente pacienteLoagado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Exames");

        JPanel painelExame = new JPanel();
        painelExame.setLayout(new GridBagLayout());
        painelExame.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        ArrayList<Exame> exames = PacientesSQL.verificarExamesPaciente(pacienteLoagado.getCpf());
        String[] listaExame = new String[exames.size()];

        int i = 0;
        for (Exame ex : exames) {
            listaExame[i] = "Exame - " + ex.getPaciente().getNome() + " - " + ex.getTipo() + " - " + ex.getId();
            i++;
        }

        JLabel pesquisaLabel = new JLabel("Pesquisar exame");
        constraints.gridy = 0;
        painelExame.add(pesquisaLabel, constraints);

        JTextField pesquisaField = new JTextField(20);
        constraints.gridy = 1;
        painelExame.add(pesquisaField, constraints);

        JList<String> list = new JList<>(listaExame);
        JListFilter<String> listFilter = new JListFilter<>(list);
        listFilter.attachFilterField(pesquisaField);

        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 2;
        painelExame.add(scrollPanel, constraints);

        JButton imprimirExameButton = new JButton("Visualizar exame");
        constraints.gridy = 3;
        painelExame.add(imprimirExameButton, constraints);

        imprimirExameButton.addMouseListener(new MouseAdapter() {
            /**
             * Método que pega os dados do exame selecionado e imprime este
             * @param e evento de click do mouse no botão Imprimir exame
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                String exameSelecionado = list.getSelectedValue();
                String[] elementos = exameSelecionado.split("-");
                int id = Integer.parseInt(elementos[3].strip());
                Exame exame = new Exame(id);
                ImpressaoUI.imprimirDocumento(exame);
            }
        });

        painelPrincipal.add(infoPanel,BorderLayout.NORTH);
        painelPrincipal.add(painelExame,BorderLayout.CENTER);

        return painelPrincipal;
    }
}
