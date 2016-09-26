package twitter4j;

import twitter4j.QuickReply.KeyboardType;

public class QuickReplyBuilder {

    private QuickReply quickReply;

    public QuickReplyBuilder() {
        quickReply = new QuickReplyJSONImpl();
    }

    public QuickReply build() {
        return quickReply;
    }

    public QuickReplyBuilder ofTypeOptions() {
        quickReply.setType("options");
        return this;
    }

    public QuickReplyBuilder ofTypeTextInput() {
        quickReply.setType("text_input");
        return this;
    }

    public QuickReplyBuilder addOption(String label, String metadata) {
        quickReply.addOption(label, metadata);
        return this;
    }

    public QuickReplyBuilder addOption(String label, String metadata, String decription) {
        quickReply.addOption(label, metadata, decription);
        return this;
    }

    public QuickReplyBuilder addOption(String label, String metadata, String decription, String thumbImage) {
        quickReply.addOption(label, metadata, decription, thumbImage);
        return this;
    }

    public QuickReplyBuilder withTextInput(String label, String metadata, KeyboardType keyboard) {
        quickReply.withTextInput(label, metadata, keyboard);
        return this;
    }
}
