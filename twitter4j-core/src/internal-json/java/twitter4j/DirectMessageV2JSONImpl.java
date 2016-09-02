package twitter4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import twitter4j.QuickReply.Options;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

public class DirectMessageV2JSONImpl extends TwitterResponseImpl implements DirectMessageV2, java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private String text;
    private long senderId;
    private long recipientId;
    private Date createdAt;
    private UserMentionEntity[] userMentionEntities;
    private URLEntity[] urlEntities;
    private HashtagEntity[] hashtagEntities;
    private MediaEntity[] mediaEntities;
    private ExtendedMediaEntity[] extendedMediaEntities;
    private SymbolEntity[] symbolEntities;
    private QuickReply quickReply;

    private String quickReplyResponseType;
    private String quickReplyResponseMetadata;

    DirectMessageV2JSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            TwitterObjectFactory.clearThreadLocalMap();
            TwitterObjectFactory.registerJSONObject(this, json);
        }
    }

    DirectMessageV2JSONImpl(JSONObject json) throws TwitterException {
        init(json);
    }

    private void init(JSONObject event) throws TwitterException {
        try {
            JSONObject json = event.getJSONObject("event");
            id = ParseUtil.getLong("id", json);
            if (json.has("created_timestamp")) {
                createdAt = new Date(ParseUtil.getLong(json.get("created_timestamp").toString()));
            }
            senderId = ParseUtil.getLong(json.getJSONObject("message_create").get("sender_id").toString());
            recipientId = ParseUtil.getLong(json.getJSONObject("message_create").getJSONObject("target").get("recipient_id").toString());

            if (!json.getJSONObject("message_create").getJSONObject("message_data").isNull("entities")) {
                JSONObject entities = json.getJSONObject("message_create").getJSONObject("message_data").getJSONObject("entities");
                int len;
                if (!entities.isNull("user_mentions")) {
                    JSONArray userMentionsArray = entities.getJSONArray("user_mentions");
                    len = userMentionsArray.length();
                    userMentionEntities = new UserMentionEntity[len];
                    for (int i = 0; i < len; i++) {
                        userMentionEntities[i] = new UserMentionEntityJSONImpl(userMentionsArray.getJSONObject(i));
                    }

                }
                if (!entities.isNull("urls")) {
                    JSONArray urlsArray = entities.getJSONArray("urls");
                    len = urlsArray.length();
                    urlEntities = new URLEntity[len];
                    for (int i = 0; i < len; i++) {
                        urlEntities[i] = new URLEntityJSONImpl(urlsArray.getJSONObject(i));
                    }
                }

                if (!entities.isNull("hashtags")) {
                    JSONArray hashtagsArray = entities.getJSONArray("hashtags");
                    len = hashtagsArray.length();
                    hashtagEntities = new HashtagEntity[len];
                    for (int i = 0; i < len; i++) {
                        hashtagEntities[i] = new HashtagEntityJSONImpl(hashtagsArray.getJSONObject(i));
                    }
                }

                if (!entities.isNull("symbols")) {
                    JSONArray symbolsArray = entities.getJSONArray("symbols");
                    len = symbolsArray.length();
                    symbolEntities = new SymbolEntity[len];
                    for (int i = 0; i < len; i++) {
                        // HashtagEntityJSONImpl also implements SymbolEntities
                        symbolEntities[i] = new HashtagEntityJSONImpl(symbolsArray.getJSONObject(i));
                    }
                }

            }
            MediaEntity media = null;
            if (!json.getJSONObject("message_create").getJSONObject("message_data").isNull("attachment")) {
                JSONObject attachment = json.getJSONObject("message_create").getJSONObject("message_data").getJSONObject("attachment");
                if (!attachment.isNull("media")) {
                    media = new MediaEntityJSONImpl(attachment.getJSONObject("media"));
                }
            }
            userMentionEntities = userMentionEntities == null ? new UserMentionEntity[0] : userMentionEntities;
            urlEntities = urlEntities == null ? new URLEntity[0] : urlEntities;
            hashtagEntities = hashtagEntities == null ? new HashtagEntity[0] : hashtagEntities;
            symbolEntities = symbolEntities == null ? new SymbolEntity[0] : symbolEntities;
            extendedMediaEntities = extendedMediaEntities == null ? new ExtendedMediaEntity[0] : extendedMediaEntities;
            if (json.getJSONObject("message_create").getJSONObject("message_data").has("text")) {
                String rawText = json.getJSONObject("message_create").getJSONObject("message_data").getString("text");
                int size = media != null ? 1 : 0;
                mediaEntities = new MediaEntity[size];
                if (size == 1) {
                    mediaEntities[0] = media;
                }
                text = HTMLEntity.unescapeAndSlideEntityIndices(rawText, userMentionEntities, urlEntities, hashtagEntities, mediaEntities);
            }
            if (json.getJSONObject("message_create").getJSONObject("message_data").has("quick_reply")) {
                quickReply = new QuickReplyJSONImpl(json.getJSONObject("message_create").getJSONObject("message_data").getJSONObject("quick_reply"));
            }

        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public long getSenderId() {
        return senderId;
    }

    @Override
    public long getRecipientId() {
        return recipientId;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public UserMentionEntity[] getUserMentionEntities() {
        return userMentionEntities;
    }

    @Override
    public URLEntity[] getURLEntities() {
        return urlEntities;
    }

    @Override
    public HashtagEntity[] getHashtagEntities() {
        return hashtagEntities;
    }

    @Override
    public ExtendedMediaEntity[] getExtendedMediaEntities() {
        return extendedMediaEntities;
    }

    @Override
    public SymbolEntity[] getSymbolEntities() {
        return symbolEntities;
    }

    /* package */
    static ResponseList<DirectMessageV2> createDirectMessageList(HttpResponse res, Configuration conf) throws TwitterException {
        try {
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.clearThreadLocalMap();
            }
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<DirectMessageV2> directMessages = new ResponseListImpl<DirectMessageV2>(size, res);
            for (int i = 0; i < size; i++) {
                JSONObject json = list.getJSONObject(i);
                DirectMessageV2 directMessage = new DirectMessageV2JSONImpl(json);
                directMessages.add(directMessage);
                if (conf.isJSONStoreEnabled()) {
                    TwitterObjectFactory.registerJSONObject(directMessage, json);
                }
            }
            if (conf.isJSONStoreEnabled()) {
                TwitterObjectFactory.registerJSONObject(directMessages, list);
            }
            return directMessages;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof DirectMessageV2 && ((DirectMessageV2) obj).getId() == this.id;
    }

    @Override
    public QuickReply getQuickReply() {
        return quickReply;
    }

    public static String createJsonString(long userId, String text, QuickReply quickReplies, Long mediaId) throws TwitterException {
        String json = null;
        try {
            JSONWriter jsonWriter = new JSONStringer().object().key("event").object().key("type").value("message_create").key("message_create").object().key("target").object()
                            .key("recipient_id").value(userId).endObject().key("message_data").object().key("text").value(text);
            if (quickReplies != null) {
                jsonWriter.key("quick_reply").object().key("type").value(quickReplies.getType());
                if (quickReplies.hasOptions()) {
                    jsonWriter.key("options").array();
                    for (Options opt : quickReplies.getOptions()) {
                        jsonWriter.object();
                        jsonWriter.key("label").value(opt.getLabel());
                        jsonWriter.key("metadata").value(opt.getMetadata());
                        jsonWriter.endObject();
                    }
                    jsonWriter.endArray();
                } else {
                    // text_input
                    jsonWriter.key("text_input").object();
                    jsonWriter.key("label").value(quickReplies.getTextInput().getLabel());
                    jsonWriter.key("metadata").value(quickReplies.getTextInput().getMetadata());
                    jsonWriter.key("keyboard").value(quickReplies.getTextInput().getKeyboard());
                    jsonWriter.endObject();
                }
                jsonWriter.endObject().endObject();
            }
            if (mediaId != null) {
                jsonWriter.key("attachment").object().key("media").object().key("id").value(mediaId).endObject().endObject();
            }
            jsonWriter.endObject().endObject().endObject();

            json = jsonWriter.toString();
        } catch (JSONException e) {
            throw new TwitterException("Error during json building", e);
        }
        return json;
    }

    @Override
    public MediaEntity[] getMediaEntities() {
        return mediaEntities;
    }

    public static List<DirectMessageV2> parseIncomingWebhookBody(String rawBody) throws TwitterException {
        List<DirectMessageV2> res = new ArrayList<DirectMessageV2>();
        try {
            JSONObject json = new JSONObject(rawBody);
            Configuration conf = ConfigurationContext.getInstance();
            res = createDirectMessageList(json, conf);
        } catch (JSONException e) {
            throw new TwitterException("Error during json parsing", e);
        }
        return res;
    }

    // {"direct_message_events":[{"type":"message_create","id":"771698995001516035","created_timestamp":"1472822352539","message_create":{"target":{"recipient_id":"xxx"},"sender_id":"yyy","message_data":{"text":"test"}}}]}
    private static List<DirectMessageV2> createDirectMessageList(JSONObject json, Configuration conf) throws TwitterException {
        List<DirectMessageV2> res = new ArrayList<DirectMessageV2>();
        if (json.has("direct_message_events")) {
            JSONArray dme;
            try {
                dme = json.getJSONArray("direct_message_events");
                for (int i = 0; i < dme.length(); i++) {
                    DirectMessageV2JSONImpl dm = new DirectMessageV2JSONImpl(dme.getJSONObject(i));
                    if (dme.getJSONObject(i).getJSONObject("message_create").getJSONObject("message_data").has("quick_reply_response")) {
                        JSONObject qrr = dme.getJSONObject(i).getJSONObject("message_create").getJSONObject("message_data").getJSONObject("quick_reply_response");
                        dm.setQuickReplyResponseMetadata(qrr.getString("metadata"));
                        dm.setQuickReplyResponseType(qrr.getString("type"));
                    }
                    res.add(dm);
                }
            } catch (JSONException e) {
                throw new TwitterException(e);
            }
        }
        return res;
    }

    @Override
    public String getQuickReplyResponseType() {
        return quickReplyResponseType;
    }

    @Override
    public String getQuickReplyResponseMetadata() {
        return quickReplyResponseMetadata;
    }

    private void setQuickReplyResponseType(String quickReplyResponseType) {
        this.quickReplyResponseType = quickReplyResponseType;
    }

    private void setQuickReplyResponseMetadata(String quickReplyResponseMetadata) {
        this.quickReplyResponseMetadata = quickReplyResponseMetadata;
    }

    @Override
    public String toString() {
        return "DirectMessageV2JSONImpl [id=" + id + ", text=" + text + ", senderId=" + senderId + ", recipientId=" + recipientId + ", createdAt=" + createdAt
                        + ", userMentionEntities=" + Arrays.toString(userMentionEntities) + ", urlEntities=" + Arrays.toString(urlEntities) + ", hashtagEntities="
                        + Arrays.toString(hashtagEntities) + ", mediaEntities=" + Arrays.toString(mediaEntities) + ", extendedMediaEntities="
                        + Arrays.toString(extendedMediaEntities) + ", symbolEntities=" + Arrays.toString(symbolEntities) + ", quickReply=" + quickReply
                        + ", quickReplyResponseType=" + quickReplyResponseType + ", quickReplyResponseMetadata=" + quickReplyResponseMetadata + "]";
    }

}
