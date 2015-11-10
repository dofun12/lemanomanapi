package org.lemanoman.download;

import java.util.ArrayList;
import java.util.List;

public class Teste {

    public static void main(String[] args) {
	
	String baseURL = "http://shogun.onepieceex.com.br/?numero=715&midia=1";
	String regex = ".*shogun.onepieceex.com.br.*";
	
	List<StringReplace> replaces = new ArrayList<StringReplace>();
	replaces.add(new StringReplace(".*http", "http"));
	replaces.add(new StringReplace("[ \\s\"]", ""));
	replaces.add(new StringReplace("..$", ""));
	
	Source source = new Source(baseURL,regex,replaces);
	source.searchContent();
	source.download("Teste.mp4");
	
    }

}
