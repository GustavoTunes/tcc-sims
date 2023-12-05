package projeto.sims.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@SuppressWarnings("serial")
@Table(name = "usuarios")
@Entity(name = "Usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "nome_usuario", nullable = false)
    private String nomeUsuario;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "data_nascimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<UsuarioNoticia> usuarioNoticias = new HashSet<>();

    public Usuario(String nomeCompleto, String nomeUsuario, String email, Date dataNascimento, String senha) {
        super();
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
    }

    public Usuario() {
    }

    public Usuario(String nomeCompleto, String nomeUsuario, String email, String senha, Date dataNascimento) {
        super();
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.dataNascimento = (date != null) ? sdf.parse(sdf.format(date)) : null;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato de data inválido para dataNascimento", e);
        }
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Long getId() {
        return id;
    }

    public Set<UsuarioNoticia> getUsuarioNoticias() {
        return usuarioNoticias;
    }

    public void setUsuarioNoticias(Set<UsuarioNoticia> usuarioNoticias) {
        this.usuarioNoticias = usuarioNoticias;
    }
}
