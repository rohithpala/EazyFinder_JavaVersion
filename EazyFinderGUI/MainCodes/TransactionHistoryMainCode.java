package EazyFinderGUI.MainCodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TransactionHistoryMainCode {
    public String transactionHistory(String username){
        File th = new File(System.getProperty("user.dir") + "\\EazyFinderGUI\\TransactionHistories\\" + username + ".txt");
        if(th.length() == 0)
            return "NO";
        String str;
        StringBuilder thString = new StringBuilder("<html>");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(th));
            while ((str = reader.readLine()) != null){
                thString.append(str).append("<br>");
            }
            thString.append("</html>");
            reader.close();
        } catch (Exception ignored) {}
        return String.valueOf(thString);
    }
}
