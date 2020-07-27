import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

public class Main
{
    public static void main(String[] args) throws DbxException {
        String ACCESS_TOKEN = "M_m6L8LBABAAAAAAAAAAFlS7-wcOw669XHuVmT96IsRBwhvmsOCqOE96Li0R9sdg";

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());

        JavaSoundRecorder recorder = new JavaSoundRecorder(client, "C:\\Users\\User\\Desktop\\");
        for (int i = 0; i < 2; i++) {
            recorder.recordAudio(10000);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
