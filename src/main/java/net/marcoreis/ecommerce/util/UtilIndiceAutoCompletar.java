package net.marcoreis.ecommerce.util;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class UtilIndiceAutoCompletar {
	private static Logger logger =
			LogManager.getLogger(UtilIndiceAutoCompletar.class);

	public void criaIndexInfixAutoCompletar() {
		try {
			logger.info("Iniciando indexado autocompletar");
			FSDirectory fSDirectory1 = FSDirectory.open(
					Paths.get(TipoIndice.PRODUTO.diretorio(),
							new String[0]));
			FSDirectory fSDirectory2 =
					FSDirectory.open(Paths.get(
							TipoIndice.PRODUTO_AUTOCOMPLETAR
									.diretorio(),
							new String[0]));
			DirectoryReader directoryReader = DirectoryReader
					.open((Directory) fSDirectory1);
			String campoNA = "produtoNomeNA";
			LuceneDictionary luceneDictionary =
					new LuceneDictionary(
							(IndexReader) directoryReader,
							campoNA);
			AnalyzingInfixSuggester analisadorSugestao =
					new AnalyzingInfixSuggester(
							(Directory) fSDirectory2,
							criaAnalyzer());
			analisadorSugestao
					.build((Dictionary) luceneDictionary);
			analisadorSugestao.close();
			directoryReader.close();
			logger.info("do autocompletar criado com sucesso");
		} catch (IOException e) {
			logger.error(e);
		}
	}

	private Analyzer criaAnalyzer() {
		return (Analyzer) new StandardAnalyzer();
	}
}
