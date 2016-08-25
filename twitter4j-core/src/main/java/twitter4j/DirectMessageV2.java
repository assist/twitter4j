package twitter4j;

import java.util.Date;

/**
 * @since Twitter4J 4.1.0
 */
public interface DirectMessageV2 extends TwitterResponse, EntitySupport, java.io.Serializable {

    long getId();

    String getText();

    long getSenderId();

    long getRecipientId();

    Date getCreatedAt();

    QuickReply getQuickReply();

    String getQuickReplyResponseType();

    String getQuickReplyResponseMetadata();

}