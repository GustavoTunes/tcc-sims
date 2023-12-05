package projeto.sims.model;

import jakarta.persistence.*;

@Table(name = "usuario_noticia")
@Entity(name = "UsuarioNoticia")
public class UsuarioNoticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "noticia_id", nullable = false)
    private Noticia noticia;

    private String noticiaUrl;
    private String usuarioEmail;

    public UsuarioNoticia() {
    }

    public UsuarioNoticia(Usuario usuario, Noticia noticia) {
        this.usuario = usuario;
        this.noticia = noticia;
    }

    public UsuarioNoticia(Usuario usuario, Noticia noticia, String noticiaUrl, String usuarioEmail) {
        this.usuario = usuario;
        this.noticia = noticia;
        this.noticiaUrl = noticiaUrl;
        this.usuarioEmail = usuarioEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Noticia getNoticia() {
        return noticia;
    }

    public void setNoticia(Noticia noticia) {
        this.noticia = noticia;
    }

    public String getNoticiaUrl() {
        return noticiaUrl;
    }

    public void setNoticiaUrl(String noticiaUrl) {
        this.noticiaUrl = noticiaUrl;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }
}
