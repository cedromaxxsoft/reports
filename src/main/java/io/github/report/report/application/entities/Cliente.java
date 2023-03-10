package io.github.report.report.application.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table
@Data
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String cpf;
    private String cidade;
    private String telefone;
    private String profissao;


    public Cliente(Long id, String nome, String cpf, String cidade, String telefone, String profissao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.cidade = cidade;
        this.telefone = telefone;
        this.profissao = profissao;
    }

    public Cliente() {
    }


}
