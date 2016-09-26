package twitter4j;

import java.util.ArrayList;
import java.util.List;

import twitter4j.conf.Configuration;

/**
 * 
 * @author ccomandini 24 ago 2016
 */
public class QuickReplyJSONImpl implements QuickReply {

    @Override
    public String toString() {
        return "QuickReplyJSONImpl [type=" + type + ", options=" + options + ", textInput=" + textInput + "]";
    }

    private static final long serialVersionUID = 1L;

    private String type;

    private List<Options> options;

    private TextInput textInput;

    QuickReplyJSONImpl() { // for builder
        type = null;
        options = null;
        textInput = null;
    }

    QuickReplyJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
            TwitterObjectFactory.registerJSONObject(this, json);
        }
    }

    public QuickReplyJSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject json) throws TwitterException {
        try {
            type = json.getString("type");
            if (json.has("options")) {
                setType("options");
                JSONArray options = json.getJSONArray("options");
                for (int i = 0; i < options.length(); i++) {
                    JSONObject opt = options.getJSONObject(i);
                    // TODO add image property when it will be available
                    addOption(opt.getString("label"), opt.getString("metadata"), opt.getString("description"));
                }
            }
            if (json.has("text_input")) {
                setType("text_input");
                JSONObject ti = json.getJSONObject("text_input");
                withTextInput(ti.getString("label"), ti.getString("metadata"), KeyboardType.valueOf(ti.getString("keyboard")));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    public static QuickReplyBuilder getBuilder() {
        QuickReplyBuilder builder = new QuickReplyBuilder();
        return builder;
    }

    public String getType() {
        return type;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setType(String type) {
        this.type = type;
        if ("options".equals(type)) {
            this.options = new ArrayList<QuickReply.Options>();
        }
    }

    public void addOption(String label, String metadata) {
        if (options != null) {
            options.add(new Options(label, metadata));
        }
    }

    public void addOption(String label, String metadata, String decription) {
        if (options != null) {
            options.add(new Options(label, metadata, decription));
        }
    }

    public void addOption(String label, String metadata, String decription, String thumbImage) {
        if (options != null) {
            options.add(new Options(label, metadata, decription, thumbImage));
        }
    }

    public void withTextInput(String label, String metadata, KeyboardType keyboard) {
        this.textInput = new TextInput(label, metadata, keyboard.name());
    }

    public TextInput getTextInput() {
        return textInput;
    }

    public boolean hasOptions() {
        return options != null && options.size() > 0;
    }

}
