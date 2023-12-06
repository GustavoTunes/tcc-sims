package projeto.sims.controllers;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import projeto.sims.model.Noticia;
import projeto.sims.model.NoticiaDTO;
import projeto.sims.model.Usuario;
import projeto.sims.model.UsuarioNoticia;
import projeto.sims.repository.NoticiaRepository;
import projeto.sims.repository.UsuarioNoticiaRepository;
import projeto.sims.service.NoticiaService;
import projeto.sims.service.UsuarioNoticiaService;
import projeto.sims.service.UsuarioService;

@Controller
public class NewsController {

	private final UsuarioService usuarioService;
	private final NoticiaService noticiaService;
	private final UsuarioNoticiaService usuarioNoticiaService;

	public NewsController(UsuarioService usuarioService, NoticiaService noticiaService, UsuarioNoticiaService usuarioNoticiaService) {
		this.usuarioService = usuarioService;
		this.noticiaService = noticiaService;
		this.usuarioNoticiaService = usuarioNoticiaService;
	}

	@PostMapping("/alterar-noticia")
	@ResponseBody
	public void alterarNoticia(@RequestBody NoticiaDTO noticiaDTO, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Usuario usuario = usuarioService.buscarPorEmail(email);
		Noticia noticia = new Noticia(noticiaDTO.getImagem(), noticiaDTO.getTitle(), noticiaDTO.getData(), noticiaDTO.getContent(), noticiaDTO.getUrlNoticia());

		if (noticiaDTO.getCheckBoxAtivo()) {
			usuarioNoticiaService.associarUsuarioNoticia(usuario, noticia);
		}else {
			usuarioNoticiaService.desassociarNoticiaUsuario(usuario, noticia);
		}
	}

	@GetMapping("/{cidade}")
	public ModelAndView obterNoticias(@PathVariable String cidade, Model model, HttpSession session) {
		// Verifique se a cidade é reconhecida
		if (!siteCidade.containsKey(cidade)) {
			return handleCidadeNaoReconhecida(cidade, model);
		}

		return puxarNoticias(cidade, model, session);
	}

	private ModelAndView handleCidadeNaoReconhecida(String cidade, Model model) {
		model.addAttribute("city", cidade);
		model.addAttribute("news", "Cidade não reconhecida: " + cidade);
		return new ModelAndView("erro");
	}

	private ModelAndView handleCidadeForadoAr(String cidade, Model model) {
		model.addAttribute("city", cidade);
		model.addAttribute("news", "Site de " + cidade + " fora do ar");
		return new ModelAndView("cidadeForaDoAr");
	}
	
	// associando e armazenando urls específicas para cada cidade

	private static final Map<String, String> siteCidade = new HashMap<>();
	private static final Map<String, String> seletorCidade = new HashMap<>();

	static {

		siteCidade.put("bertioga", "https://www.bertioga.sp.gov.br/?s=sa%C3%BAde");
		siteCidade.put("cubatao", "https://www.cubatao.sp.gov.br/?s=sa%C3%BAde");
		siteCidade.put("guaruja", "https://www.guaruja.sp.gov.br/categoria/saude/");
		siteCidade.put("itanhaem", "https://www2.itanhaem.sp.gov.br/tag/saude/");
		siteCidade.put("mongagua", "https://www.mongagua.sp.gov.br/noticias/saude");
		siteCidade.put("peruibe", "http://www.peruibe.sp.gov.br/secao/saude/");
		siteCidade.put("praiagrande",
				"https://www.praiagrande.sp.gov.br/pgnoticias/noticias/assunto_noticia.asp?idAssunto=14");
		siteCidade.put("santos", "https://www.santos.sp.gov.br/lista-de-noticias/151");
		siteCidade.put("saovicente", "https://www.saovicente.sp.gov.br/pesquisa?pesquisa=saude");

		// associando e armazenando seletores específicos para cada cidade

		seletorCidade.put("bertioga", ".news__wrapper");
		seletorCidade.put("cubatao", ".pp-post-wrap");
		seletorCidade.put("guaruja", ".archive.category.category-saude.category-215.real-accessability-body");
		seletorCidade.put("itanhaem", ".row");
		seletorCidade.put("mongagua", ".list-item");
		seletorCidade.put("peruibe", "article");
		seletorCidade.put("praiagrande", "div#divAssuntoNoticia");
		seletorCidade.put("santos", ".grid-item.col-xs-12.col-sm-6.col-md-4.col-lg-4");
		seletorCidade.put("saovicente", ".desc-noticia");
	}

	private String obterFraseDesejada(Element element, String seletor) {
		Elements divs = element.select(seletor);

		if (divs != null && !divs.isEmpty()) {
			Element firstElement = divs.first();
			if (firstElement != null) {
				String texto = firstElement.text();
				int primeiroPonto = texto.indexOf('.');
				return (primeiroPonto >= 0) ? texto.substring(0, primeiroPonto + 1) : texto;
			}
		}

		return "";
	}

	public ModelAndView puxarNoticias(String cidade, Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");
		String url = siteCidade.get(cidade);
		String seletor = seletorCidade.get(cidade);

		try {
			Document document = Jsoup.connect(url).get();
			Elements noticias = document.select(seletor);

			List<NoticiaDTO> noticiasList = new ArrayList<>();

			int contadorLinks;

			switch (cidade) {

				case "bertioga":

					contadorLinks = 0;

					for (Element noticia : noticias) {

						Elements links = noticia.select("div a"); // Obter todos os links dentro do h4

						for (Element link : links) {

							if (link != null && contadorLinks < 9) {

								String urlNoticia = link.attr("href");

								try {

									// Fazer uma solicitação HTTP para a URL da notícia
									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									String imagem = noticia.select("img").attr("src");
									String title = noticia.select(".news__title").text();
									String data = "Publicado em " + noticia.select(".news__details").text();
									String content = obterFraseDesejada(noticiaDocument, "div.post-content__content");

									if (imagem == null | imagem.isEmpty()) {
										imagem = "https://bertioga.sp.gov.br/wp/wp-content/uploads/2022/10/brasao-de-bertioga.png";
									}

									noticiasList.add(new NoticiaDTO(imagem, title, data, content, urlNoticia)); // Adicione cada notícia à lista

									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {
								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;

				case "cubatao":

					contadorLinks = 0;

					for (Element noticia : noticias) {

						// Seletor para o link dentro da notícia
						Elements links = noticia.select(".elementor-widget-theme-post-title h1 a");

						// Iterar sobre os links dentro do h4
						for (Element link : links) {

							if (link != null && contadorLinks < 9) {

								String urlNoticia = link.attr("href");

								try {

									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									String imagem = noticia.select("img").attr("src");
									String title = noticia.select(".elementor-widget-theme-post-title h1").text();
									String data = "Publicado em " + noticia.select(".elementor-icon-list-text").text();
									String content = noticia.select(".elementor-widget-container").text();

									content = content.replace(title, "")
											.replace(noticia.select(".elementor-icon-list-text").text(), "");

									if (imagem == null || imagem.isEmpty()) {

										imagem = "https://upload.wikimedia.org/wikipedia/commons/9/9f/Brasao-Cubatao.png";
									}

									noticiasList.add(new NoticiaDTO(imagem, title, data, content, urlNoticia));
									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {

								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;

				case "guaruja":

					contadorLinks = 0;

					for (Element noticia : noticias) {
						// Seletor para o link dentro da notícia
						Elements links = noticia.select(
								".mvp-widget-feat2-left a, .mvp-widget-feat2-right a, .mvp-blog-story-wrap.left.relative.infinite-post a");
						// Iterar sobre os links dentro do h4
						for (Element link : links) {

							if (link != null && contadorLinks < 9) {

								String urlNoticia = link.attr("href");

								try {

									// Fazer uma solicitação HTTP para a URL da notícia
									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									String imagem = noticiaDocument.select("#mvp-post-feat-img img").attr("data-src");
									String title = noticiaDocument.select("h1").text();
									String data = "Publicado em " + noticiaDocument.select("time").text();
									String content = noticiaDocument.select("em").text();

									if (imagem == null | imagem.isEmpty()) {
										imagem = "https://upload.wikimedia.org/wikipedia/commons/d/db/Bras%C3%A3o_Guaruj%C3%A1.png";
									}

									noticiasList.add(new NoticiaDTO(imagem, title, data, content, urlNoticia));

									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {
								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;

				case "itanhaem":

					contadorLinks = 0;

					for (Element noticia : noticias) {
						// Seletor para o link dentro da notícia
						Elements links = noticia.select("h4 a"); // Obter todos os links dentro do h4

						// Iterar sobre os links dentro do h4
						for (Element link : links) {

							if (link != null && contadorLinks < 9) {

								String urlNoticia = link.attr("href");

								try {

									// Fazer uma solicitação HTTP para a URL da notícia
									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									Element primeiroParagrafo = noticiaDocument.select("article p").first();
									// Extrair informações da notícia
									String imagem = noticiaDocument.select("figure img").attr("src");
									String title = link.text(); // Texto dentro do h4
									String data = "Publicado em " + link.parent().nextElementSibling().text().substring(10);

									if (imagem == null | imagem.isEmpty()) {
										imagem = "https://www2.itanhaem.sp.gov.br/wp-content/uploads/2019/11/brasao-1-272x300.jpg";
									}

									noticiasList.add(new NoticiaDTO(imagem, title, data, primeiroParagrafo.text(), urlNoticia));

									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {
								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;

				case "mongagua":

					contadorLinks = 0;

					for (Element noticia : noticias) {

						Elements links = noticia.select("h3 a"); // Obter todos os links dentro do h4

						// Iterar sobre os links dentro do h4
						for (Element link : links) {

							if (link != null && contadorLinks < 9) {

								String urlNoticia = "https://www.mongagua.sp.gov.br" + link.attr("href");

								try {
									// Fazer uma solicitação HTTP para a URL da notícia
									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									String imagem = noticiaDocument.select(".post-content img").attr("src");
									String title = noticia.select(".list-title").text();
									String data = "Publicado em "
											+ noticiaDocument.select(".page-content time").text().substring(0, 10);
									String content = obterFraseDesejada(noticiaDocument, ".post-image p");

									if (content == null || content.isEmpty()) {
										content = obterFraseDesejada(noticiaDocument,
												".x11i5rnm.xat24cr.x1mh8g0r.x1vvkbs.xtlvy1s.x126k92a div");

									}
									if (content == null || content.isEmpty()) {
										content = obterFraseDesejada(noticiaDocument, ".post-content span");
									}

									if (imagem == null | imagem.isEmpty()) {
										imagem = "https://upload.wikimedia.org/wikipedia/commons/e/ee/Brasao-Mongagua.png";
									}

									noticiasList.add(new NoticiaDTO(imagem, title, data, content, urlNoticia));

									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {
								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;

				case "peruibe":

					contadorLinks = 0;

					for (Element noticia : noticias) {

						Elements links = noticia.select("h2 a"); // Obter todos os links dentro do h4

						// Iterar sobre os links dentro do h4
						for (Element link : links) {

							if (link != null && contadorLinks < 9) {

								String urlNoticia = link.attr("href");

								try {

									// Fazer uma solicitação HTTP para a URL da notícia
									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									String imagem = noticiaDocument.select(".zak-content img").attr("src");
									String title = noticia.select("h2").text();
									String data = "Publicado em " + noticiaDocument.select(".entry-date").text();
									String content = noticia.select("p").text();

									if (imagem == null | imagem.isEmpty()) {
										imagem = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Brasao_Peruibe_SaoPaulo_Brasil.svg/1471px-Brasao_Peruibe_SaoPaulo_Brasil.svg.png";
									}

									noticiasList.add(new NoticiaDTO(imagem, title, data, content, urlNoticia));

									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {
								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;

				case "praiagrande":

					contadorLinks = 0;

					// Iterar sobre as notícias
					for (Element noticia : noticias) {
						Elements links = noticia.select("td a"); // Obter todos os links dentro do h4

						// Iterar sobre os links dentro do h4
						for (Element link : links) {
							// Verifique se o link não é nulo e se já processou 9 links
							if (link != null && contadorLinks < 9) {
								String urlNoticia = "https://www.praiagrande.sp.gov.br/pgnoticias/noticias/"
										+ link.attr("href");

								try {
									// Fazer uma solicitação HTTP para a URL da notícia
									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									// Extrair outras informações da notícia
									String imagem = "https://www.praiagrande.sp.gov.br/"
											+ noticiaDocument.select("div#divCadaNoticia img").attr("src").substring(5);
									String title = noticiaDocument.select(".olho_foto").text();
									String data = "Publicado em "
											+ noticiaDocument.select("td.txt_pg").first().text().substring(0, 9);
									String content = noticiaDocument.select(".olho_chamada").text();

									if (imagem == null | imagem.isEmpty()) {
										imagem = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Bras%C3%A3o_de_Praia_Grande-SP.png/948px-Bras%C3%A3o_de_Praia_Grande-SP.png";
									}

									// Criar o objeto Noticia e adicionar à lista
									noticiasList.add(new NoticiaDTO(imagem, title, data, content, urlNoticia));
									// Incrementar o contador de links processados
									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {
								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;
				// ...

				case "santos":

					contadorLinks = 0;

					for (Element noticia : noticias) {

						Elements links = noticia.select(".grid-item.col-xs-12.col-sm-6.col-md-4.col-lg-4 a"); // Obter todos

						// Iterar sobre os links dentro do h4
						for (Element link : links) {

							if (link != null && contadorLinks < 9) {

								String urlNoticia = link.attr("href");

								try {
									// Fazer uma solicitação HTTP para a URL da notícia
									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									String imagem = noticia.select("img").attr("src");
									String title = noticia.select("h3").text();
									String data = "Publicado em " + noticia.select(".field-content").text();
									String content = obterFraseDesejada(noticiaDocument, ".field-item.even p");

									if (imagem == null | imagem.isEmpty()) {
										imagem = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Brasao_Santos_SaoPaulo_Brasil.svg/1200px-Brasao_Santos_SaoPaulo_Brasil.svg.png";
									}

									noticiasList.add(new NoticiaDTO(imagem, title, data, content, urlNoticia));
									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {
								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;

				case "saovicente":

					contadorLinks = 0;

					for (Element noticia : noticias) {

						Elements links = noticia.select("hgroup a"); // Obter todos os links dentro do h4

						// Iterar sobre os links dentro do h4
						for (Element link : links) {

							if (link != null && contadorLinks < 9) {

								String urlNoticia = "https://www.saovicente.sp.gov.br/" + link.attr("href");

								try {

									// Fazer uma solicitação HTTP para a URL da notícia
									Document noticiaDocument = Jsoup.connect(urlNoticia).get();

									String imagem = noticiaDocument.select("figure.resultado-noticia").attr("style");

									String title = noticia.select("h3").text();
									String data = "Publicado em " + noticia.select(".data-pesquisa-materia span").text();
									String content = noticiaDocument.select(".desc-noticia h3").text();

									if (imagem == null | imagem.isEmpty()) {
										imagem = "https://upload.wikimedia.org/wikipedia/commons/d/dc/S%C3%A3o_Vicente.PNG";
									}

									noticiasList.add(new NoticiaDTO(imagem, title, data, content, urlNoticia));
									contadorLinks++;
								} catch (IOException e) {
									// Imprime informações sobre a exceção para depuração
									e.printStackTrace();
									System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
								}
							} else {
								// Se já processou 9 links, sai do loop
								break;
							}
						}
					}
					break;

				default:
					throw new IllegalArgumentException("Cidade inválida: " + cidade);
			}

			// Adicione a lista de notícias ao modelo
			List<UsuarioNoticia> listaUsuarioNoticias = usuarioNoticiaService.buscarPeloUsuarioEmail(email);
			noticiasList.forEach(favorita ->
					favorita.setCheckBoxAtivo(
							listaUsuarioNoticias.stream()
									.filter(site -> site.getNoticiaUrl().equals(favorita.getUrlNoticia()))
									.peek(site -> favorita.setCheckBoxAtivo(true))
									.findFirst()
									.isPresent()
					)
			);

			model.addAttribute("cidade", cidade);
			model.addAttribute("suaVariavel", true);
			model.addAttribute("noticiasList", noticiasList);

			// Retorna a página correspondente a cidade

			return new ModelAndView("noticias");


		} catch (SocketTimeoutException e) {
	        // Lidar com a exceção de timeout aqui
	        e.printStackTrace();
	        model.addAttribute("city", cidade);
	        model.addAttribute("news", "Site da prefeitura fora do ar");
	        return new ModelAndView("cidadeForaDoAr");
	        
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("city", cidade);
			model.addAttribute("news", "Erro de E/S (IOException).");
			return new ModelAndView("erro");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			model.addAttribute("city", cidade);
			model.addAttribute("news", "Argumento inválido (IllegalArgumentException).");
			return new ModelAndView("erro");
		} catch (RuntimeException e) {
			e.printStackTrace();
			model.addAttribute("city", cidade);
			model.addAttribute("news", "Erro de execução (RuntimeException).");
			return new ModelAndView("erro");
		
	}
}
}