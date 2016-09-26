package twitter4j;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author ccomandini 19 ago 2016
 */
public interface QuickReply extends Serializable {

    public enum KeyboardType {
        text, number
    }

    public final class Options implements Serializable {

        @Override
        public String toString() {
            return "Options [label=" + label + ", metadata=" + metadata + ", decription=" + decription + ", thumbImage=" + thumbImage + "]";
        }

        private static final long serialVersionUID = 2L;
        private final String label;
        private final String metadata;
        private final String decription;
        private final String thumbImage;

        public Options(String label, String metadata) {
            super();
            this.label = label;
            this.metadata = metadata;
            this.decription = null;
            this.thumbImage = null;
        }

        public Options(String label, String metadata, String decription) {
            super();
            this.label = label;
            this.metadata = metadata;
            this.decription = decription;
            this.thumbImage = null;
        }

        public Options(String label, String metadata, String decription, String thumbImage) {
            super();
            this.label = label;
            this.metadata = metadata;
            this.decription = decription;
            this.thumbImage = thumbImage;
        }

        public String getLabel() {
            return label;
        }

        public String getMetadata() {
            return metadata;
        }

        public String getDecription() {
            return decription;
        }

        public String getThumbImage() {
            return thumbImage;
        }

    } // Options

    public final class TextInput implements Serializable {
        @Override
        public String toString() {
            return "TextInput [label=" + label + ", metadata=" + metadata + ", keyboard=" + keyboard + "]";
        }

        private static final long serialVersionUID = 1L;
        private final String label;
        private final String metadata;
        private final String keyboard;

        public TextInput(String label, String metadata, String keyboard) {
            super();
            this.label = label;
            this.metadata = metadata;
            this.keyboard = keyboard;
        }

        public String getLabel() {
            return label;
        }

        public String getMetadata() {
            return metadata;
        }

        public String getKeyboard() {
            return keyboard;
        }

    } // TextInput

    public String getType();

    public List<Options> getOptions();

    public void setType(String type);

    public void addOption(String label, String metadata);

    public void addOption(String label, String metadata, String decription);

    public void addOption(String label, String metadata, String decription, String thumbImage);

    public void withTextInput(String label, String metadata, KeyboardType keyboard);

    public TextInput getTextInput();

    public boolean hasOptions();

}