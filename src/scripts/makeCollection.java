package scripts;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.jsoup.Jsoup;
public class makeCollection {
	
	public void makeCollect(String collectionPos) {
		try
		{   // html받는 공간 생성
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			
			Element Docs = doc.createElement("docs");
			doc.appendChild(Docs);
			


			
			//문서 안의 파일 읽기
			File folder = new File(collectionPos);
			File files[] = folder.listFiles();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			for(int i = 0; i <files.length; i++) {
				org.jsoup.nodes.Document html = Jsoup.parse(files[i], "UTF-8");
				Element code = doc.createElement("doc");
				Docs.appendChild(code);
				int ID = i;
				String str = String.valueOf(ID);
				code.setAttribute("ID", str);
				String titleData = html.title();
				Element TIT = doc.createElement("title");
				TIT.appendChild(doc.createTextNode(titleData));
				code.appendChild(TIT);
				String bodyData = html.body().text();
				Element BOD = doc.createElement("body");
				code.appendChild(BOD);
				BOD.appendChild(doc.createTextNode("\n" + bodyData));
				
				
				
			}	
			File Folder = new File(collectionPos);
			Folder.mkdir();
			
			//저장소 생성, IRsample 폴더에 저장
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream("collection.xml"));			
			transformer.transform(source, result);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
