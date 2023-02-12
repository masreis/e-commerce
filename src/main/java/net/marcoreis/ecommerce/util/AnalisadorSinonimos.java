package net.marcoreis.ecommerce.util;

import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.br.BrazilianStemFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;

public class AnalisadorSinonimos extends StopwordAnalyzerBase {
	private static final Logger logger =
			LogManager.getLogger(AnalisadorSinonimos.class);

	protected Analyzer.TokenStreamComponents createComponents(
			String fieldName) {
		boolean ignoraMaiusculas = true;
		StandardTokenizer standardTokenizer =
				new StandardTokenizer();
		LowerCaseFilter lowerCaseFilter = new LowerCaseFilter(
				(TokenStream) standardTokenizer);
		StandardFilter standardFilter = new StandardFilter(
				(TokenStream) lowerCaseFilter);
		SynonymGraphFilter synonymGraphFilter =
				new SynonymGraphFilter(
						(TokenStream) standardFilter,
						criaMapaSinonimos(), ignoraMaiusculas);
		return new Analyzer.TokenStreamComponents(
				(Tokenizer) standardTokenizer, criaTokenStream(
						(TokenStream) synonymGraphFilter));
	}

	private SynonymMap criaMapaSinonimos() {
		boolean fazDeduplicacao = true;
		boolean expandeListaSinonimos = true;
		SolrSynonymParser parser = new SolrSynonymParser(
				fazDeduplicacao, expandeListaSinonimos,
				criaAnalizadorSinonimos());
		try {
			parser.parse(new FileReader(
					"src/test/resources/sinonimos.txt"));
			return parser.build();
		} catch (IOException | java.text.ParseException e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	private TokenStream criaTokenStream(TokenStream filtro) {
		return (TokenStream) new BrazilianStemFilter(filtro);
	}

	private Analyzer criaAnalizadorSinonimos() {
		return (Analyzer) new StandardAnalyzer();
	}
}
