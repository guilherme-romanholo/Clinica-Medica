package clinica.medica.gui.receita;

import clinica.medica.database.ReceitasSQL;
import clinica.medica.documentos.Receita;
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
 * Classe que possui os métodos usados na criação da interface da parte de receitas do paciente
 */
public class ReceitasPacienteUI {
    /**
     * Método usado para criar a tela de receitas do paciente
     * @param pacienteLogado Paciente que vai acessar suas receitas
     * @param telaLogada Tela principal onde esse novo painel será posicionado
     * @return Tela criada pelo método
     */
    public static JPanel telaVerificarReceitas(Paciente pacienteLogado, TelaLogadaUI telaLogada) {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setSize(800, 600);

        JPanel infoPanel = RecursosUI.criaInfoPanel("Receitas");

        JPanel painelReceita = new JPanel();
        painelReceita.setLayout(new GridBagLayout());
        painelReceita.setSize(800, 600);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;

        ArrayList<Receita> receitas = ReceitasSQL.selectAllReceitasFromPaciente(pacienteLogado.getCpf());
        String[] listaReceita = new String[receitas.size()];

        int i = 0;
        for (Receita rc: receitas) {
            listaReceita[i] = "Receita - " + rc.getPaciente().getNome() + " - " + rc.getNomeDoRemedio() + " - " + rc.getMedico().getNome() + " - " + rc.getId();
            i++;
        }

        JLabel pesquisaLabel = new JLabel("Pesquisar receita");
        constraints.gridy = 0;
        painelReceita.add(pesquisaLabel, constraints);

        JTextField pesquisaField = new JTextField(20);
        constraints.gridy = 1;
        painelReceita.add(pesquisaField, constraints);

        JList<String> list = new JList<>(listaReceita);
        JListFilter<String> listFilter = new JListFilter<>(list);
        listFilter.attachFilterField(pesquisaField);

        JScrollPane scrollPanel = new JScrollPane(list);
        scrollPanel.setPreferredSize(new Dimension(600, 400));
        constraints.gridy = 2;
        painelReceita.add(scrollPanel, constraints);

        JButton imprimirReceitaButton = new JButton("Visualizar receita");
        constraints.gridy = 3;
        painelReceita.add(imprimirReceitaButton, constraints);

        imprimirReceitaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String receitaSelecionada = list.getSelectedValue();
                String[] elementos = receitaSelecionada.split("-");
                int id = Integer.parseInt(elementos[4].strip());

                Receita receita = new Receita(id);
                ImpressaoUI.imprimirDocumento(receita);
            }
        });

        painelPrincipal.add(infoPanel,BorderLayout.NORTH);
        painelPrincipal.add(painelReceita,BorderLayout.CENTER);

        return painelPrincipal;
    }
}