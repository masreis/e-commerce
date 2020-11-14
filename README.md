# E-commerce

Esta é a aplicação web usada no livro **Apache Lucene - Sistemas de busca com técnicas de Recuperação de Informação**, disponível na Casa do Código pelo link <https://www.casadocodigo.com.br/products/livro-apache-lucene>.

O Lucene é uma biblioteca que pode ser facilmente acoplada a um sistema existente. Há diferentes formas de se fazer isso e neste capítulo veremos algumas dessas estratégias. Para simular um sistema de informação, foi criado um sistema de *e-commerce* fictício que implementa as principais funcionalidades deste tipo de aplicação.

Vamos utilizar o Lucene para fazer consultas de produtos simulando a navegação de um cliente no site, que pode estar buscando um item muito específico, navegando à procura de um presente qualquer ou para verificar o histórico de pedidos.

Tecnologias utilizadas:

* Lucene: biblioteca para busca textual;
* Maven: gerenciador do *build* da aplicação;
* JPA / Hibernate: persistência;
* JSF / PrimeFaces: interfaces web;
* MySQL: Sistema Gerenciador de Banco de Dados Relacional;
* Tomcat: Servlet Container.

A aplicação de exemplo conta com páginas web escritas em `xhtml` e PrimeFaces, mas vale lembrar que não é nosso objetivo entrar em detalhes sobre frameworks web. O ponto central do livro continua sendo o sistema de busca.

## Carga inicial dos dados

Nosso projeto de e-commerce conta com uma base de dados que pode ser usada nos testes. Os registros estão gravados no arquivo `dump.sql`. Nesta seção explicaremos como fazer sua importação.

Comece criando a base de dados no MySQL com o comando `mysql -u root -p -e "create database ecommerce"`. Depois, execute o comando de importação `mysql -u root -p ecommerce < dump.sql`. Apenas se certifique de indicar o caminho correto do arquivo `dump.sql`, que deve estar no diretório `src/test/resources/`, dentro da pasta do projeto `e-commerce`. Com isso, as tabelas serão criadas e populadas com valores fictícios. O parâmetro `-p` do MySQL é opcional, usado para solicitar a senha para o usuário.

Foram criadas classes de teste para carregar algumas tabelas, como a de vendas e de clientes. As classes são `CriarVendasTest`, `CriarClientesTeste` e `AlterarPrecoProdutoTest`. Veja que os nomes dos clientes foram criados com caracteres aleatórios, bem como os preços dos produtos, que não representam o valor real de mercado. A classe `AlterarPrecoProdutoTest` muda os preços dos produtos e pode ser usada para simular um ambiente real, onde os valores são de fato alterados durante a operação.

Na sequência, temos de criar o índice no Lucene para esta base de dados inicial. A classe que indexa os dados do MySQL é `IndexadorProdutoTest`. Para executá-la no Eclipse, use a opção *Run as JUnit Test*.

## Acessando o sistema

Para acessar o sistema, podemos utilizar o usuário `email1@gmail.com`.
