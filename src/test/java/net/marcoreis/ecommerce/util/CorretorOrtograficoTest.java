package net.marcoreis.ecommerce.util;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.DirectSpellChecker;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.SuggestWord;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CorretorOrtograficoTest {

	private Directory diretorio;
	private IndexReader reader;
	private String campo = "produtoNome";
	private String termoComErro = "progeto";

	@Before
	public void abreRecursos() throws IOException {
		this.diretorio = FSDirectory
				.open(Paths.get(TipoIndice.PRODUTO.diretorio()));
		this.reader = DirectoryReader.open(diretorio);
	}

	@After
	public void fechaRecursos() throws IOException {
		this.diretorio.close();
		this.reader.close();
	}

	@Test
	public void test1IndexaDicionario() throws IOException {
		System.out.println("Criando o índice de sugestões");
		SpellChecker corretor = new SpellChecker(diretorio);
		Dictionary dicionario =
				new LuceneDictionary(reader, campo);
		IndexWriterConfig config = new IndexWriterConfig();
		boolean fullMerge = true;
		corretor.indexDictionary(dicionario, config, fullMerge);
		corretor.close();
	}

	@Test
	public void testSeExiste() throws IOException {
		SpellChecker verificador = new SpellChecker(diretorio);
		System.out.println(String.format(
				"O termo '%s' está no dicionário: %s",
				termoComErro, verificador.exist(termoComErro)));
		verificador.close();
	}

	@Test
	public void testSugereAlternativas() throws IOException {
		System.out.println("\nSugere alternativas");
		SpellChecker corretor = new SpellChecker(diretorio);
		int quantidadeSugestoes = 50;
		String[] sugestoes = corretor.suggestSimilar(
				termoComErro, quantidadeSugestoes);
		for (String sugestao : sugestoes) {
			System.out.println(sugestao);
		}
		corretor.close();
	}

	@Test
	public void testSugereComAcessoDireto() throws IOException {
		System.out
				.println("\nSugere alternativas acesso direto");
		DirectSpellChecker corretor = new DirectSpellChecker();
		Term termo = new Term(campo, termoComErro);
		int quantidadeSugestoes = 50;
		SuggestWord[] sugestoes = corretor.suggestSimilar(termo,
				quantidadeSugestoes, reader);
		for (SuggestWord sw : sugestoes) {
			System.out.println(sw.string);
		}
	}

}
