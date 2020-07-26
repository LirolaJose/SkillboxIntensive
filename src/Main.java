import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

public class Main
{
    public static void main(String[] args)
    {
        String ACCESS_TOKEN = "M_m6L8LBABAAAAAAAAAAFHMjEdwM-ZZlx9WRqo5KnBjpghDszsc2QAUACU5CnPr3";

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        JavaSoundRecorder recorder = new JavaSoundRecorder(client, "");
        for(;;) {
            recorder.recordAudio(10000);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        try {
//            InputStream in = new FileInputStream("/Users/sortedmap/Desktop/Java.jpg");
//            client.files().uploadBuilder("/picture.jpg")
//                    .uploadAndFinish(in);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
}
