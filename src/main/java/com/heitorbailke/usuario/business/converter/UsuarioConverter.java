package com.heitorbailke.usuario.business.converter;

import com.heitorbailke.usuario.business.dto.EnderecoDTO;
import com.heitorbailke.usuario.business.dto.TelefoneDTO;
import com.heitorbailke.usuario.business.dto.UsuarioDTO;
import com.heitorbailke.usuario.infrastructure.entity.Endereco;
import com.heitorbailke.usuario.infrastructure.entity.Telefone;
import com.heitorbailke.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEnderecoDTO(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefonesDTO(usuarioDTO.getTelefones()))
                .build();
    }

    public List<Endereco> paraListaEnderecoDTO(List<EnderecoDTO> enderecoDTOS) {
        List<Endereco> enderecos = new ArrayList<>();
        for(EnderecoDTO enderecoDTO : enderecoDTOS) {
            enderecos.add(paraEndereco(enderecoDTO));
        }
        return enderecos;
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {

        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<Telefone> paraListaTelefonesDTO(List<TelefoneDTO> telefonesDTOS) {
        return telefonesDTOS.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telfoneDTO) {
        return Telefone.builder()
                .numero(telfoneDTO.getNumero())
                .ddd(telfoneDTO.getDdd())

                .build();
    }


    public UsuarioDTO paraUsuarioDTO(Usuario usuarioDTO) {
        return UsuarioDTO.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefones(usuarioDTO.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> paraListaEndereco(List<Endereco> enderecoDTOS) {
        List<EnderecoDTO> enderecos = new ArrayList<>();
        for(Endereco enderecoDTO : enderecoDTOS) {
            enderecos.add(paraEnderecoDTO(enderecoDTO));
        }
        return enderecos;
    }

    public EnderecoDTO paraEnderecoDTO(Endereco enderecoDTO) {

        return EnderecoDTO.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .cidade(enderecoDTO.getCidade())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<TelefoneDTO> paraListaTelefones(List<Telefone> telefonesDTOS) {
        return telefonesDTOS.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telfoneDTO) {
        return TelefoneDTO.builder()
                .numero(telfoneDTO.getNumero())
                .ddd(telfoneDTO.getDdd())

                .build();
    }
}
