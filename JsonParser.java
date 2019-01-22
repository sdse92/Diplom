import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonParser {

    private Map element;

    public JsonParser(){
        this.element = new LinkedHashMap();
    }

    public JsonParser(String s){
        this.element = new LinkedHashMap();
        parse(element, s);
    }

    private void parse(Map m, String s){
        if (s.startsWith("[")) {
            try {
                throw new inStringException("json must start with {");
            } catch (inStringException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(s);
            String string = s.substring(1, s.length()-1);
            String[] elementsArr = string.split(",");
            for (int i = 0; i < elementsArr.length; i++){
                int delimiter = elementsArr[i].indexOf(":");
                String key = elementsArr[i].substring(0, delimiter).replaceAll("\"", "");
                String value = elementsArr[i].substring(delimiter + 1).replaceAll("\"", "");
                m.put(key, value);
            }
        }
    }

    public String get(String key){
        return (String) element.get(key);
    }

    public void put(String key, String value){
        element.put(key, value);
    }

    @Override
    public String toString() {
        Iterator it = element.entrySet().iterator();
        StringBuffer sb = new StringBuffer("{");
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            sb.append("\"").append(entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"");
            if (it.hasNext()) sb.append(",");
            it.remove();
        }
        sb.append("}");
        return sb.toString();
    }

    class inStringException extends Exception{
        public inStringException(String text){
            super(text);
        }
    }
}
