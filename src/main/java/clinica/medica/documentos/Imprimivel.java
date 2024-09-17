package clinica.medica.documentos;

import java.sql.Date;

public interface Imprimivel {

    /**
     * Método para retornar o tipo do documento
     * @return Tipo
     */
    String imprimeTipo();

    /**
     * Método para retornar a informação do documento
     * @return Info documento
     */
    String imprimeInfo();

    /**
     * Método para retornar o nome do paciente do documento
     * @return Nome do paciente
     */
    String imprimeNomePaciente();

    /**
     * Método para retornar a idade do paciente do documento
     * @return Idade
     */
    String imprimeIdade();

    /**
     * Método para retornar o sexo do paciente
     * @return Sexo paciente
     */
    String imprimeSexo();

    /**
     * Método para retornar a data do documento
     * @return Data
     */
    String imprimeData();

    /**
     * Método para retornar os comentários do documento
     * @return Comentários do paciente
     */
    String imprimeComentarios();

    /**
     * Método para retornar no do médico
     * @return Nome do médico
     */
    String imprimeNomeMedico();

    /**
     * Método para imprimir o CRM do médico
     * @return CRM
     */
    String imprimeCrmAtuacaoMedico();
}
