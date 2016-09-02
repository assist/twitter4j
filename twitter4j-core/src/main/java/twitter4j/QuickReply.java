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
            return "Options [label=" + label + ", metadata=" + metadata + "]";
        }

        private static final long serialVersionUID = 1L;
        private final String label;
        private final String metadata;

        public Options(String label, String metadata) {
            super();
            this.label = label;
            this.metadata = metadata;
        }

        public String getLabel() {
            return label;
        }

        public String getMetadata() {
            return metadata;
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

    public void withTextInput(String label, String metadata, KeyboardType keyboard);

    public TextInput getTextInput();

    public boolean hasOptions();

}