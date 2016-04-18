package frm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import model.Book;

public class SaveGraph {

	private static final String ROOT_ELEM_TAG = "root";
	private static Document doc;
	
	private static Logger log = Logger.getLogger(SaveGraph.class);

	private static Document newDoc() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document doc = null;
		try {
			doc = factory.newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	public static void save(File file, Collection<Book> elements) {

		doc = newDoc();
		Element root = doc.createElement(ROOT_ELEM_TAG);
		doc.appendChild(root);

		Element books = doc.createElement("Library");
		root.appendChild(books);

		for (Book book : elements) {
			Element newElement = doc.createElement("book");
			newElement.setAttribute(Book.AUTHOR_NAME, book.getAuthorName());
			newElement.setAttribute(Book.BOOK_NAME, book.getBookName());
			newElement.setAttribute(Book.CIRCULATION, Integer.toString(book.getCirculation()));
			newElement.setAttribute(Book.TOME_NUMBER, Integer.toString(book.getTomeNumber()));
			books.appendChild(newElement);
		}

		try {
			toXML(file, doc);
		} catch (TransformerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void toXML(File file, Document document) throws TransformerException, IOException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(doc), new StreamResult(file));
	}

	public static List<Book> loadLybrary(File fileName) {
		InputStream in = null;
		try {
			in = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLGraphLoader loader = new XMLGraphLoader(in);
		try {
			loader.parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Book> clipGraph = loader.getBooks();

		return clipGraph;
	}

	private static class XMLGraphLoader {
		private InputSource source;
		private SAXParser parser;
		private DefaultHandler documentHandler;

		private List<Book> booksList;

		public XMLGraphLoader(InputStream stream) {
			booksList = new LinkedList<>();
			try {
				Reader reader = new InputStreamReader(stream);
				source = new InputSource(reader);
				parser = SAXParserFactory.newInstance().newSAXParser();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			documentHandler = new XMLParser();
		}

		public void parse() throws Exception {
			parser.parse(source, documentHandler);
		}

		public List<Book> getBooks() {
			return booksList;
		}

		class XMLParser extends DefaultHandler {
			public void startElement(String uri, String localName, String qName, Attributes attr) {
				switch (qName) {
				case ("book"):
					parseBook(attr);
					break;
				}
			}

			private void parseBook(Attributes attr) {
				Book book = new Book(attr.getValue(Book.AUTHOR_NAME),
						attr.getValue(Book.BOOK_NAME),
						Integer.parseInt(attr.getValue(Book.CIRCULATION)),
						Integer.parseInt(attr.getValue(Book.TOME_NUMBER)));
				booksList.add(book);
			}
		}
	}
}
