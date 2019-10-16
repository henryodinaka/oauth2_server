package market.henry.auth.enums;

public enum EncryptionHeader {

    AUTHORIZATION("Authorization"),
    PUBLIC_KEY("PublicKey"),
    CLIENT_ID("ClientId"),
    CHANNEL_CODE("ChannelCode");

    String name;

    EncryptionHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
