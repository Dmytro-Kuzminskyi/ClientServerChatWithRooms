import java.io.Serializable;

public class Message implements Serializable {
	private String type;
	private String text;
	private String addText;
	
	public Message(Type type) {
		setType(type.toString());
		setText(null);
		setAddText(null);
	}
	
	public Message(Type type, String text) {
		setType(type.toString());
		setText(text);
		setAddText(null);
	}
	
	public Message(Type type, String text, String addText) {
		setType(type.toString());
		setText(text);
		setAddText(addText);
	}
	
	public Message(Type type, Response response) {
		setType(type.toString());
		setText(response.toString());
		setAddText(null);
	}

	public Message(Type type, Response response, String addText) {
		setType(type.toString());
		setText(response.toString());
		setAddText(addText);
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

	public String getAddText() {
		return addText;
	}

	public void setAddText(String addText) {
		this.addText = addText;
	}
}
