package Model;

public class Site {
	private String url;
	private String html;

	public Site() {
		// TODO Auto-generated constructor stub
	}
	
	public Site(String url, String html) {
		this.url = url;
		this.html = html;
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	

}
