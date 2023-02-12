package net.marcoreis.ecommerce.util;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnalisadorTermosIndiceTest {

	private Directory diretorio;
	private IndexReader reader;

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

	/**
	 * Mostra todos os termos (todas as palavras) que formam o índice
	 * 
	 * @throws IOException
	 */
	@Test
	public void testMostraTermosIndice() throws IOException {
		String campo = "produtoNome";
		Terms termos = MultiFields.getTerms(reader, campo);
		TermsEnum iteTermos = termos.iterator();
		BytesRef next;
		while ((next = iteTermos.next()) != null) {
			System.out.println(next.utf8ToString());
		}
	}

	/**
	 * Mostra a quantidade de ocorrências e a quantidade de documentos em que
	 * aparece a palavra 'xbox'
	 * 
	 * @throws IOException
	 */
	@Test
	public void testDadosTermo() throws IOException {
		Term termo = new Term("produtoDescricao", "xbox");
		String message = String.format(
				"%s:%s \nTotal de Ocorrências (TF): %3$d\n"
						+ "Total de Documentos (DF):%4$d",
				termo.field(), termo.text(),
				reader.totalTermFreq(termo),
				reader.docFreq(termo));
		System.out.println(message);
	}

	/**
	 * Mostra os termos e a quantidade de vezes em que eles aparecem.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testMostraTermosETotais() throws IOException {
		String campo = "produtoNome";
		Terms termos = MultiFields.getTerms(reader, campo);
		TermsEnum iteTermos = termos.iterator();
		BytesRef next;
		while ((next = iteTermos.next()) != null) {
			String palavra = next.utf8ToString();
			Term termo = new Term(campo, palavra);
			String message = String.format(
					"%s:%s - TF[%3$d] DF[%4$d]", termo.field(),
					termo.text(), reader.totalTermFreq(termo),
					reader.docFreq(termo));
			System.out.println(message);
		}
	}

}
