
import org.apache.xerces.dom.DeferredElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by houlin.jiang on 17-2-10.
 */
public class Util {

    public static List<ElemeModel> parseXml(String xmlPath) {
        List<ElemeModel>  resultList = new ArrayList<ElemeModel>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(xmlPath);
            NodeList nodeList = document.getChildNodes();
            //第一层解析。根View
            if (nodeList.getLength() == 0) {
                Node eleme = nodeList.item(0);
                ElemeModel elemeModel = new ElemeModel();
                elemeModel.type = eleme.getNodeName();
                elemeModel.isRoot = true;
                String[] list = xmlPath.split("\\\\");
                elemeModel.name =list[list.length-1].replace(".xml","");
                resultList.add(elemeModel);
            }
            parseNodelist(nodeList.item(0).getChildNodes(),resultList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private static List<ElemeModel> parseNodelist(NodeList nodeList, List<ElemeModel> resultList) {
        if (nodeList.getLength() >0)
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node eleme = nodeList.item(i);
                if (hasID(eleme)) {
                    ElemeModel elemeModel = new ElemeModel();
                    elemeModel.name = ((DeferredElementImpl) eleme).getAttribute("android:id").replace("@+id/","");
                    elemeModel.type = eleme.getNodeName();
                    resultList.add(elemeModel);
                }
                parseNodelist(eleme.getChildNodes(),resultList);
            }
        return resultList;
    }

    private static boolean hasID(Node eleme) {
        return eleme.hasAttributes() && !"".equals(((DeferredElementImpl) eleme).getAttribute("android:id"));
    }


    public static void main(String[] args) {
        StringBuilder nameBuider = new StringBuilder();
        StringBuilder importBuider = new StringBuilder();
        List<ElemeModel>  resultList =  parseXml("D:\\work\\Android\\train\\m_adr_atom_train\\SRC\\app\\src\\main\\res\\layout\\atom_train_main_search_international_s2s.xml");
        for(ElemeModel elemeModel : resultList) {
            String name = Util.getPreName(elemeModel.type).toLowerCase() + "_" + elemeModel.name;
            nameBuider.append("private " + elemeModel.type + " " + name + ";\n");
            importBuider.append(name + " = (" + elemeModel.type + ") findViewById(" + elemeModel.name + ");\n");
        }
        nameBuider.append(importBuider);
        System.out.println(nameBuider);

    }

    public static String getPreName(String type) {
        if (type.contains(".")) { //           自定义View
            String[] sList = type.split("\\.");
            if (sList.length > 0) {
                return getUpperCase(sList[sList.length - 1]);
            } else {
                return "";
            }
        } else if (type.equals("View")) {
            return "view";
        } else if (type.equals("Button")) {
            return "bt";
        } else {
            return getUpperCase(type);
        }
    }

    public static String getUpperCase(String str) {
        String result = "";
        for(int i = 0;i<str.length();i++) {
            if (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z') {
                result+= str.charAt(i);
            }
        }
        return result;
    }
}