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
import org.junit.Test;

public class AnalisadorTermosIndiceTest {

	Directory diretorio = FSDirectory
			.open(Paths.get(DIRETORIO_INDICE));
	IndexReader reader = DirectoryReader.open(diretorio);

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
