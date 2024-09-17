package clinica.medica.gui.recursos;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Classa usada para criar a JList com o filtro de pesquisa nas outras telas
 * @param <T> Tipo do elemento presente na JList
 */
public class JListFilter<T> {
    private final JList<T> list;
    private final List<T> originalItems;
    private final DefaultListModel<T> originalModel;

    /**
     * Método construtor da JList com filtro
     * @param list JList já existente
     */
    public JListFilter(JList<T> list) {
        this.list = list;
        this.originalItems = new ArrayList<>();
        this.originalModel = new DefaultListModel<>();

        for (int i = 0; i < list.getModel().getSize(); i++) {
            T item = list.getModel().getElementAt(i);
            originalItems.add(item);
            originalModel.addElement(item);
        }
    }

    /**
     * Método usado para adicionar um campo de texto como
     * campo para realizar as pesquisas da lista
     * @param filterText Campo de texto
     */
    public void attachFilterField(JTextField filterText) {
        filterText.getDocument().addDocumentListener(new DocumentListener() {
            /**
             * Método para realizar uma atualização quando um caractere é inserido
             * no campo de texto
             * @param e Evento de escrita
             */
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateList();
            }

            /**
             * Método para realizar uma atualização quando um caractere é removido
             * no campo de texto
             * @param e Evento de escrita
             */
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateList();
            }

            /**
             * Método para realizar uma atualização quando um caractere é alterado
             * no campo de texto
             * @param e Evento de escrita
             */
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateList();
            }

            /**
             * Método principal para realizar a atualização da lista
             */
            private void updateList() {
                String text = filterText.getText().toLowerCase();
                if (text.isEmpty()) {
                    list.setModel(originalModel);
                    return;
                }
                filterList(text);
            }
        });
    }

    /**
     * Método usado na filtragem da lista com base nos caracteres fornecidos
     * @param text Texto digitado na caixa de entrada
     */
    private void filterList(String text) {
        DefaultListModel<T> filteredModel = new DefaultListModel<>();
        text = text.toLowerCase();

        List<T> filteredItems = new ArrayList<>();

        for (T item : originalItems) {
            if (item.toString().toLowerCase().contains(text)) {
                filteredItems.add(item);
            }
        }

        for (T item : filteredItems) {
            filteredModel.addElement(item);
        }

        list.setModel(filteredModel);
    }
}
