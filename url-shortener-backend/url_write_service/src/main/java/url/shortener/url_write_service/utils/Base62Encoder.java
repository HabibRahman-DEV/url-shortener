package url.shortener.url_write_service.utils;

public class Base62Encoder {

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;

    public static String encode(long id) {
        if (id == 0) {
            return String.valueOf(BASE62_CHARS.charAt(0));
        }

        StringBuilder builder = new StringBuilder();
        while (id > 0) {
            int reminder = (int) (id % BASE);
            id = id / BASE;
            builder.append(BASE62_CHARS.charAt(reminder));
        }
        return builder.reverse().toString();
    }
}
