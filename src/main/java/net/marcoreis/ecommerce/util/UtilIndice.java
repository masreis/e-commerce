package net.marcoreis.ecommerce.util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class UtilIndice {
	private static Logger logger =
			LogManager.getLogger(UtilIndice.class);

	private IndexWriter writer;

	private Integer quantidadeLimiteRegistros =
			Integer.valueOf(1000);

	private Analyzer analyzer;

	private SearcherManager sm;

	private TipoIndice tipo;

	private static final Map<TipoIndice, UtilIndice> instancias =
			new HashMap<>();

	public static synchronized UtilIndice getInstancia(
			TipoIndice tipo) throws IOException {
		UtilIndice utilIndice = instancias.get(tipo);
		if (utilIndice == null) {
			utilIndice = new UtilIndice(tipo);
			instancias.put(tipo, utilIndice);
		}
		return utilIndice;
	}

	private UtilIndice(TipoIndice tipo) throws IOException {
		this.tipo = tipo;
		abrirIndice(tipo.diretorio());
		inicializaSm();
	}

	private void abrirIndice(String diretorioIndice)
			throws IOException {
		FSDirectory fSDirectory = FSDirectory
				.open(Paths.get(diretorioIndice, new String[0]));
		this.analyzer = criaAnalyzer();
		IndexWriterConfig conf =
				new IndexWriterConfig(this.analyzer);
		this.writer =
				new IndexWriter((Directory) fSDirectory, conf);
		logger.info("IndexWriter aberto");
	}

	protected Analyzer criaAnalyzer() {
		return (Analyzer) new StandardAnalyzer();
	}

	private void inicializaSm() throws IOException {
		this.sm = new SearcherManager(this.writer, null);
		logger.info("SearcherManager aberto");
	}

	public void atualizarDoc(Term termo, Document doc)
			throws IOException {
		atualizarDoc(termo, doc, true);
	}

	public void atualizarDoc(Term termo, Document doc,
			boolean commit) throws IOException {
		verificarSeAberto();
		this.writer.updateDocument(termo, (Iterable) doc);
		if (commit)
			commit();
	}

	public void adicionarDoc(Document doc, boolean commit)
			throws IOException {
		verificarSeAberto();
		this.writer.addDocument((Iterable) doc);
		if (commit)
			commit();
	}

	public void adicionarDoc(Document doc) throws IOException {
		adicionarDoc(doc, true);
	}

	public void removerDoc(String campo, String id)
			throws IOException {
		removerDoc(campo, id, true);
	}

	public void removerDoc(String campo, String id,
			boolean commit) throws IOException {
		verificarSeAberto();
		Term termo = new Term(campo, id);
		this.writer.deleteDocuments(new Term[] { termo });
		if (commit)
			commit();
	}

	public void commit() throws IOException {
		this.writer.commit();
	}

	public Document doc(int docID) throws IOException {
		verificarSeAberto();
		this.sm.maybeRefresh();
		IndexSearcher searcher =
				(IndexSearcher) this.sm.acquire();
		try {
			Document doc = searcher.doc(docID);
			return doc;
		} finally {
			this.sm.release(searcher);
		}
	}

	private void verificarSeAberto() throws IOException {
		if (this.sm == null) {
			abrirIndice(this.tipo.diretorio());
			inicializaSm();
			logger.info("reaberto");
		}
	}

	public int getNumDocs() throws IOException {
		verificarSeAberto();
		IndexSearcher searcher =
				(IndexSearcher) this.sm.acquire();
		try {
			this.sm.maybeRefresh();
			return searcher.getIndexReader().numDocs();
		} finally {
			this.sm.release(searcher);
		}
	}

	public void fechar() throws IOException {
		synchronized (this) {
			if (this.sm != null) {
				this.sm.close();
				this.sm = null;
				logger.info("SearcherManager fechado");
			}
			if (this.writer != null) {
				this.writer.close();
				this.writer = null;
				logger.info("IndexWriter fechado");
			}
		}
	}

	public TopDocs buscar(String consulta)
			throws IOException, ParseException {
		verificarSeAberto();
		this.sm.maybeRefresh();
		IndexSearcher searcher =
				(IndexSearcher) this.sm.acquire();
		try {
			QueryParser queryParser =
					new QueryParser("", this.analyzer);
			queryParser.setDefaultOperator(
					QueryParser.Operator.AND);
			Query query = queryParser.parse(consulta);
			TopDocs hits = searcher.search(query,
					this.quantidadeLimiteRegistros.intValue());
			return hits;
		} finally {
			this.sm.release(searcher);
		}
	}

	public TopDocs buscar(Query consulta)
			throws IOException, ParseException {
		verificarSeAberto();
		this.sm.maybeRefresh();
		IndexSearcher searcher =
				(IndexSearcher) this.sm.acquire();
		try {
			TopDocs hits = searcher.search(consulta,
					this.quantidadeLimiteRegistros.intValue());
			return hits;
		} finally {
			this.sm.release(searcher);
		}
	}

	public Document highlight(Document doc, int docID,
			Query query) throws IOException {
		IndexSearcher searcher =
				(IndexSearcher) this.sm.acquire();
		try {
			FastVectorHighlighter fhl =
					new FastVectorHighlighter();
			FieldQuery fq = fhl.getFieldQuery(query);
			String fragHighlight = fhl.getBestFragment(fq,
					searcher.getIndexReader(), docID,
					"textoCompleto",
					doc.get("textoCompleto").length());
			if (fragHighlight == null)
				fragHighlight = doc.get("textoCompleto");
			doc.add((IndexableField) new StringField(
					"textoCompleto.hl", fragHighlight,
					Field.Store.NO));
			return doc;
		} finally {
			this.sm.release(searcher);
		}
	}

	public List<String> buscaSugestoesProduto(String nomeParcial)
			throws IOException {
		List<String> lista = new ArrayList<>();
		FSDirectory fSDirectory = FSDirectory.open(Paths.get(
				TipoIndice.PRODUTO_AUTOCOMPLETAR.diretorio(),
				new String[0]));
		AnalyzingInfixSuggester analisadorSugestao =
				new AnalyzingInfixSuggester(
						(Directory) fSDirectory, this.analyzer);
		int quantidadeSugestoes = 10;
		try {
			List<Lookup.LookupResult> lookupResultList =
					analisadorSugestao.lookup(nomeParcial, false,
							quantidadeSugestoes);
			for (Lookup.LookupResult lookupResult : lookupResultList)
				lista.add(lookupResult.key.toString());
		} finally {
			analisadorSugestao.close();
		}
		return lista;
	}

	public void atualizaSugestaoProduto() throws IOException {
		FSDirectory fSDirectory = FSDirectory.open(
				Paths.get(this.tipo.diretorio(), new String[0]));
		AnalyzingInfixSuggester analisadorSugestao =
				new AnalyzingInfixSuggester(
						(Directory) fSDirectory, this.analyzer);
		System.out.println("Tamanho anterior: "
				+ analisadorSugestao.getCount());
		System.out.println("Memanterior: "
				+ analisadorSugestao.ramBytesUsed());
		BytesRef novaSugestao =
				new BytesRef("Adaptador HDMI iPad "
						+ "Android Windows Linux MacOS"
								.getBytes());
		long weight = 1L;
		BytesRef payload = null;
		analisadorSugestao.add(novaSugestao, null, weight,
				payload);
		analisadorSugestao.refresh();
		analisadorSugestao.close();
	}
}
