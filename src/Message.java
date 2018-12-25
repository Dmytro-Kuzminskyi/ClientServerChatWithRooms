import java.io.Serializable;

public class Message implements Serializable {
	private String type;
	private String text;
	
	public Message(Type type, String text) {
		setType(type.toString());
		setText(text);
	}
	
	public Message(Type type, Response response) {
		setType(type.toString());
		setText(response.toString());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}
	

	public void setText(String text) {
		this.text = text;
	}
}
