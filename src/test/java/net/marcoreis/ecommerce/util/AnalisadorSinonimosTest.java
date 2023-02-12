package net.marcoreis.ecommerce.util;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class AnalisadorSinonimosTest {

	private static final Logger logger =
			LogManager.getLogger(AnalisadorSinonimos.class);

	@Test
	public void testBuscaPorSinonimoProdutoNome()
			throws ParseException, IOException {
		// Abre o índice de produtos
		FSDirectory fSDirectory = FSDirectory
				.open(Paths.get(TipoIndice.PRODUTO.diretorio()));
		Analyzer analyzer = criaAnalisador();

		/**
		 * Monta a consulta com sinônimos. A consulta é '8 gigabytes', mas deve
		 * encontrar produtos com o nome '8 GB', porque 'gigabytes' é sinônimo
		 * de 'GB' no arquivo sinonimos.txt
		 * 
		 */
		QueryParser parser = new QueryParser("", analyzer);
		String consulta = "produtoNome:(\"8 gigabytes\")";
		Query query = parser.parse(consulta);
		logger.info("Consulta analisada -> " + query);
		IndexReader reader = DirectoryReader.open(fSDirectory);
		IndexSearcher searcher = new IndexSearcher(reader);

		// Mostra os resultados
		TopDocs hits = searcher.search(query, 10);
		logger.info("Quantidade de itens encontrados: "
				+ hits.totalHits);
		for (ScoreDoc sd : hits.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			logger.info("Produto: " + doc.get("produtoNome"));
		}

		// Fecha os recursos
		reader.close();
		analyzer.close();

	}

	private Analyzer criaAnalisador() {
		return new AnalisadorSinonimos();
	}

}
