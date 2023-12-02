package projeto.sims.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import projeto.sims.model.Noticia;
import projeto.sims.model.NoticiaDTO;

@Controller
public class NewsController {

	@PostMapping("/alterar-noticia")
	@ResponseBody
	public void alterarNoticia(@RequestBody NoticiaDTO noticia) {
		System.out.println(noticia);
	}

	@GetMapping("{cidade}")
	public String obterNoticias(@PathVariable String cidade, Model model) {
		try {
			String url = siteCidade.get(cidade);
			String seletor = seletorCidade.get(cidade);
			Document document = Jsoup.connect(url).get();
			Elements noticias = document.select(seletor);

			List<Noticia> noticiasList = new ArrayList<>();

			switch (cidade) {

			case "bertioga":

				pegarNoticiasBertioga(noticias, noticiasList);

				break;

			case "cubatao":

				pegarNoticiasCubatao(noticias, noticiasList);

				break;

			case "guaruja":

				pegarNoticiasGuaruja(noticias, noticiasList);

				break;

			case "itanhaem":

				pegarNoticiasItanhaem(noticias, noticiasList);

				break;

			case "mongagua":

				pegarNoticiasMongagua(noticias, noticiasList);

				break;

			case "peruibe":

				pegarNoticiasPeruibe(noticias, noticiasList);

				break;

			case "praiagrande":

				pegarNoticiasPraiaGrande(noticias, noticiasList);

				break;

			case "santos":

				pegarNoticiasSantos(noticias, noticiasList);

				break;

			case "saovicente":

				pegarNoticiasSaoVicente(noticias, noticiasList);

				break;

			default:

				model.addAttribute("cidade", cidade);
				model.addAttribute("noticiasList", "Cidade não reconhecida");

				return "noticias";

			}

			if (!noticiasList.isEmpty()) {
				model.addAttribute("noticiasList", noticiasList);
				return "noticias";
			} else {
				model.addAttribute("cidade", cidade);
				return "erro";
			}
		} catch (IOException e) {
			handleException(model, cidade, "Erro de E/S (IOException).", e);
		} catch (IllegalArgumentException e) {
			handleException(model, cidade, "Argumento inválido (IllegalArgumentException).", e);
		} catch (RuntimeException e) {
			handleException(model, cidade, "Erro de execução (RuntimeException).", e);
		} catch (Exception e) {
			handleException(model, cidade, "Erro ao buscar notícias (Exception).", e);
		}

		return "erro";
	}

	private void handleException(Model model, String cidade, String errorMessage, Throwable e) {

		e.printStackTrace();
		model.addAttribute("city", cidade);
		model.addAttribute("news", "Cidade não reconhecida");
	}

	// associando e armazenando urls específicas para cada cidade

	private static final Map<String, String> siteCidade = Map.of(
			"bertioga","https://www.bertioga.sp.gov.br/?s=sa%C3%BAde", 
			"cubatao", "https://www.cubatao.sp.gov.br/?s=sa%C3%BAde",
			"guaruja", "https://www.guaruja.sp.gov.br/categoria/saude/", 
			"itanhaem","https://www2.itanhaem.sp.gov.br/tag/saude/", 
			"mongagua", "https://www.mongagua.sp.gov.br/noticias/saude",
			"peruibe", "http://www.peruibe.sp.gov.br/secao/saude/", 
			"praiagrande", "https://www.praiagrande.sp.gov.br/pgnoticias/noticias/assunto_noticia.asp?idAssunto=14", 
			"santos", "https://www.santos.sp.gov.br/lista-de-noticias/151", 
			"saovicente", "https://www.saovicente.sp.gov.br/pesquisa?pesquisa=saude");

	private static final Map<String, String> seletorCidade = Map.of(
			"bertioga", ".news__wrapper", 
			"cubatao", ".pp-post-wrap", 
			"guaruja", ".archive.category.category-saude.category-215.real-accessability-body",
			"itanhaem", ".row", 
			"mongagua", ".list-item", 
			"peruibe", "article", 
			"praiagrande", "div#divAssuntoNoticia",
			"santos", ".grid-item.col-xs-12.col-sm-6.col-md-4.col-lg-4", 
			"saovicente", ".desc-noticia");

	// métodos específicos para as notícias de cada cidade

	private void pegarNoticiasBertioga(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		for (Element noticia : noticias) {
			Elements links = noticia.select("div a"); // Obter todos os links dentro do h4

			for (Element link : links) {
				if (link != null && contadorLinks < 10) {
					String urlNoticia = link.attr("href");

					try {
						// Fazer uma solicitação HTTP para a URL da notícia
						Document noticiaDocument = Jsoup.connect(urlNoticia).get();

						String imagem = noticia.select("img").attr("src");
						String title = noticia.select(".news__title").text();
						String data = "Publicado em " + noticia.select(".news__details").text();
						String content = obterFraseDesejada(noticiaDocument, "div.post-content__content");

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://bertioga.sp.gov.br/wp/wp-content/uploads/2022/10/brasao-de-bertioga.png";
						}

						Noticia topico = new Noticia(imagem, title, data, content, urlNoticia);
						noticiasList.add(topico); // Adicione cada notícia à lista

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
	}

	private void pegarNoticiasCubatao(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		for (Element noticia : noticias) {

			Elements links = noticia.select(".elementor-widget-theme-post-title h1 a");

			for (Element link : links) {
				if (link != null && contadorLinks < 10) {
					String urlNoticia = link.attr("href");

					try {
						Jsoup.connect(urlNoticia).get();

						String imagem = noticia.select("img").attr("src");
						String title = noticia.select(".elementor-widget-theme-post-title h1").text();
						String data = "Publicado em " + noticia.select(".elementor-icon-list-text").text();
						String content = noticia.select(".elementor-widget-container").text();

						content = content.replace(title, "").replace(noticia.select(".elementor-icon-list-text").text(),
								"");

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://upload.wikimedia.org/wikipedia/commons/9/9f/Brasao-Cubatao.png";
						}

						Noticia topico = new Noticia(imagem, title, data, content, urlNoticia);
						noticiasList.add(topico); // Adicione cada notícia à lista
						contadorLinks++;
					} catch (IOException e) {
						e.printStackTrace();
						System.err.println("Erro ao processar a URL da notícia: " + urlNoticia);
					}
				}
			}
		}
	}

	private void pegarNoticiasGuaruja(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		for (Element noticia : noticias) {
			// Seletor para o link dentro da notícia
			Elements links = noticia.select(
					".mvp-widget-feat2-left a, .mvp-widget-feat2-right a, .mvp-blog-story-wrap.left.relative.infinite-post a");

			// Iterar sobre os links dentro do h4
			for (Element link : links) {
				if (link != null && contadorLinks < 10) {
					String urlNoticia = link.attr("href");

					try {
						// Fazer uma solicitação HTTP para a URL da notícia
						Document noticiaDocument = Jsoup.connect(urlNoticia).get();

						String imagem = noticiaDocument.select("#mvp-post-feat-img img").attr("data-src");
						String title = noticiaDocument.select("h1").text();
						String data = "Publicado em " + noticiaDocument.select("time").text();
						String content = noticiaDocument.select("em").text();

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://upload.wikimedia.org/wikipedia/commons/d/db/Bras%C3%A3o_Guaruj%C3%A1.png";
						}

						Noticia topico = new Noticia(imagem, title, data, content, urlNoticia);
						noticiasList.add(topico); // Adicione cada notícia à lista

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
	}

	private void pegarNoticiasItanhaem(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		for (Element noticia : noticias) {
			// Seletor para o link dentro da notícia
			Elements links = noticia.select("h4 a"); // Obter todos os links dentro do h4

			// Iterar sobre os links dentro do h4
			for (Element link : links) {
				if (link != null && contadorLinks < 10) {
					String urlNoticia = link.attr("href");

					try {
						// Fazer uma solicitação HTTP para a URL da notícia
						Document noticiaDocument = Jsoup.connect(urlNoticia).get();

						Element primeiroParagrafo = noticiaDocument.select("article p").first();
						// Extrair informações da notícia
						String imagem = noticiaDocument.select("figure img").attr("src");
						String title = link.text(); // Texto dentro do h4
						String data = "Publicado em " + link.parent().nextElementSibling().text().substring(10);

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://www2.itanhaem.sp.gov.br/wp-content/uploads/2019/11/brasao-1-272x300.jpg";
						}

						Noticia topico = new Noticia(imagem, title, data, primeiroParagrafo.text(), urlNoticia);
						noticiasList.add(topico);

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
	}

	private void pegarNoticiasMongagua(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		for (Element noticia : noticias) {
			Elements links = noticia.select("h3 a"); // Obter todos os links dentro do h4

			// Iterar sobre os links dentro do h4
			for (Element link : links) {
				if (link != null && contadorLinks < 10) {
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

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://upload.wikimedia.org/wikipedia/commons/e/ee/Brasao-Mongagua.png";
						}

						Noticia topico = new Noticia(imagem, title, data, content, urlNoticia);
						noticiasList.add(topico); // Adicione cada notícia à lista

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
	}

	private void pegarNoticiasPeruibe(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		for (Element noticia : noticias) {
			Elements links = noticia.select("h2 a"); // Obter todos os links dentro do h4

			// Iterar sobre os links dentro do h4
			for (Element link : links) {
				if (link != null && contadorLinks < 10) {
					String urlNoticia = link.attr("href");

					try {
						// Fazer uma solicitação HTTP para a URL da notícia
						Document noticiaDocument = Jsoup.connect(urlNoticia).get();

						String imagem = noticiaDocument.select(".zak-content img").attr("src");
						String title = noticia.select("h2").text();
						String data = "Publicado em " + noticiaDocument.select(".entry-date").text();
						String content = noticia.select("p").text();

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Brasao_Peruibe_SaoPaulo_Brasil.svg/1471px-Brasao_Peruibe_SaoPaulo_Brasil.svg.png";
						}

						Noticia topico = new Noticia(imagem, title, data, content, urlNoticia);
						noticiasList.add(topico); // Adicione cada notícia à lista

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
	}

	private void pegarNoticiasPraiaGrande(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		// Iterar sobre as notícias
		for (Element noticia : noticias) {
			Elements links = noticia.select("td a"); // Obter todos os links dentro do h4

			// Iterar sobre os links dentro do h4
			for (Element link : links) {
				// Verifique se o link não é nulo e se já processou 9 links
				if (link != null && contadorLinks < 10) {
					String urlNoticia = "https://www.praiagrande.sp.gov.br/pgnoticias/noticias/" + link.attr("href");

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

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Bras%C3%A3o_de_Praia_Grande-SP.png/948px-Bras%C3%A3o_de_Praia_Grande-SP.png";
						}

						// Criar o objeto Noticia e adicionar à lista
						Noticia topico = new Noticia(imagem, title, data, content, urlNoticia);
						noticiasList.add(topico);

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
	}

	private void pegarNoticiasSantos(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		for (Element noticia : noticias) {
			Elements links = noticia.select(".grid-item.col-xs-12.col-sm-6.col-md-4.col-lg-4 a"); // Obter todos os
																									// links

			// Iterar sobre os links dentro do h4
			for (Element link : links) {
				if (link != null && contadorLinks < 10) {
					String urlNoticia = link.attr("href");

					try {
						// Fazer uma solicitação HTTP para a URL da notícia
						Document noticiaDocument = Jsoup.connect(urlNoticia).get();

						String imagem = noticia.select("img").attr("src");
						String title = noticia.select("h3").text();
						String data = "Publicado em " + noticia.select(".field-content").text();
						String content = obterFraseDesejada(noticiaDocument, ".field-item.even p");

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Brasao_Santos_SaoPaulo_Brasil.svg/1200px-Brasao_Santos_SaoPaulo_Brasil.svg.png";
						}

						Noticia topico = new Noticia(imagem, title, data, content, urlNoticia);
						noticiasList.add(topico); // Adicione cada notícia à lista
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
	}

	private void pegarNoticiasSaoVicente(Elements noticias, List<Noticia> noticiasList) {
		int contadorLinks = 0;

		for (Element noticia : noticias) {
			Elements links = noticia.select("hgroup a"); // Obter todos os links dentro do h4

			// Iterar sobre os links dentro do h4
			for (Element link : links) {
				if (link != null && contadorLinks < 10) {
					String urlNoticia = "https://www.saovicente.sp.gov.br/" + link.attr("href");

					try {
						// Fazer uma solicitação HTTP para a URL da notícia
						Document noticiaDocument = Jsoup.connect(urlNoticia).get();

						String imagem = noticiaDocument.select("figure.resultado-noticia").attr("style");

						String title = noticia.select("h3").text();
						String data = "Publicado em " + noticia.select(".data-pesquisa-materia span").text();
						String content = noticiaDocument.select(".desc-noticia h3").text();

						if (imagem == null || imagem.isEmpty()) {
							imagem = "https://upload.wikimedia.org/wikipedia/commons/d/dc/S%C3%A3o_Vicente.PNG";
						}

						Noticia topico = new Noticia(imagem, title, data, content, urlNoticia);
						noticiasList.add(topico); // Adicione cada notícia à lista
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
	}

	// método para ajudar a minimizar o conteúdo a ser exibido na tela

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

}