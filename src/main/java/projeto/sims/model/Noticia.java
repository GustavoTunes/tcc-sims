package projeto.sims.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@Table(name = "noticias")
@Entity(name = "noticia")
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagem;
    private String title;
    private String data;
    @Column(length = 1000)
    private String content;
    private String urlNoticia;
    @OneToMany(mappedBy = "noticia", cascade = CascadeType.ALL)
    private Set<UsuarioNoticia> usuarioNoticias = new HashSet<>();

    public Noticia(String imagem, String title, String data, String content, String urlNoticia) {
        this.imagem = imagem;
        this.title = title;
        this.data = data;
        this.content = content;
        this.urlNoticia = urlNoticia;
    }

    public Noticia() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlNoticia() {
        return urlNoticia;
    }

    public void setUrlNoticia(String urlNoticia) {
        this.urlNoticia = urlNoticia;
    }

    public Set<UsuarioNoticia> getUsuarioNoticias() {
        return usuarioNoticias;
    }

    public void setUsuarioNoticias(Set<UsuarioNoticia> usuarioNoticias) {
        this.usuarioNoticias = usuarioNoticias;
    }
}
