package com.heitorbailke.usuario.business;

import com.heitorbailke.usuario.business.converter.UsuarioConverter;
import com.heitorbailke.usuario.business.dto.EnderecoDTO;
import com.heitorbailke.usuario.business.dto.TelefoneDTO;
import com.heitorbailke.usuario.business.dto.UsuarioDTO;
import com.heitorbailke.usuario.infrastructure.entity.Endereco;
import com.heitorbailke.usuario.infrastructure.entity.Telefone;
import com.heitorbailke.usuario.infrastructure.entity.Usuario;
import com.heitorbailke.usuario.infrastructure.exceptions.ConflictException;
import com.heitorbailke.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.heitorbailke.usuario.infrastructure.repository.EnderecoRepository;
import com.heitorbailke.usuario.infrastructure.repository.TelefoneRepository;
import com.heitorbailke.usuario.infrastructure.repository.UsuarioRepository;
import com.heitorbailke.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor


public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado" + email);
            }
        } catch (Exception e) {
            throw new ConflictException("Email já cadastrado", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscaUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                    () -> new ResourceNotFoundException("Email não encontrado " + email)
                            )
            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }

    public void deleteUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atulizaDadosUsuario(String token, UsuarioDTO dto) {
        // Aqui eu estou fazendo a busca do email do usuario através do token( tirar a obrigatoriedade do email)
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        // Aqui eu coloquei uma verificação para que se acaso o usuario alterar a senha gerar uma criptografia da nova senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null );

        //Nesse momento eu estou buscando os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));

        //Aqui eu mesclei os dados que recebemos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        //Salvou os dados do usuário convertido e depois pegou o retorno e conveteu para usuarioDTO

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {

        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + idEndereco));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto){


        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + idTelefone));

        //
        Telefone telefone = usuarioConverter.updateTelefone(dto, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));

    }
}
