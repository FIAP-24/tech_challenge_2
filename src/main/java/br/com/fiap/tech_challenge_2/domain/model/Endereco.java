package br.com.fiap.tech_challenge_2.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    private Long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    // Domain business logic methods
    public boolean isValid() {
        return logradouro != null && !logradouro.trim().isEmpty() &&
               numero != null && !numero.trim().isEmpty() &&
               bairro != null && !bairro.trim().isEmpty() &&
               cidade != null && !cidade.trim().isEmpty() &&
               estado != null && !estado.trim().isEmpty() &&
               cep != null && !cep.trim().isEmpty();
    }

    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(logradouro).append(", ").append(numero);
        
        if (complemento != null && !complemento.trim().isEmpty()) {
            address.append(" - ").append(complemento);
        }
        
        address.append(" - ").append(bairro)
               .append(", ").append(cidade)
               .append(" - ").append(estado)
               .append(", CEP: ").append(cep);
        
        return address.toString();
    }

    public void updateAddress(String logradouro, String numero, String complemento, 
                            String bairro, String cidade, String estado, String cep) {
        if (logradouro != null && !logradouro.trim().isEmpty()) {
            this.logradouro = logradouro.trim();
        }
        if (numero != null && !numero.trim().isEmpty()) {
            this.numero = numero.trim();
        }
        if (complemento != null) {
            this.complemento = complemento.trim();
        }
        if (bairro != null && !bairro.trim().isEmpty()) {
            this.bairro = bairro.trim();
        }
        if (cidade != null && !cidade.trim().isEmpty()) {
            this.cidade = cidade.trim();
        }
        if (estado != null && !estado.trim().isEmpty()) {
            this.estado = estado.trim();
        }
        if (cep != null && !cep.trim().isEmpty()) {
            this.cep = cep.trim();
        }
    }
}
