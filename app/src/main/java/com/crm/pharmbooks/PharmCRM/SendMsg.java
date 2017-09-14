package com.crm.pharmbooks.PharmCRM;
        import android.util.Log;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.URL;
        import java.net.URLConnection;
        import java.net.URLEncoder;


public class SendMsg  {
    String name,mobiles,amount;

    public SendMsg(String name,String mobiles,String amount)
    {
        this.name=name;
        this.mobiles=mobiles;
        this.amount=amount;

        SendSMS();
    }


    public   void SendSMS()
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    String authkey = "165963Ah3LDLrLx596ef669";
                    String phone = "91"+mobiles;
                    String senderId = "manyaG";
                    String message = "Thank you "+name+" for shopping from us!"+"\n+Your bill amount is: Rs."+amount;
                    String route="4";


                    URLConnection myURLConnection=null;
                    URL myURL=null;
                    BufferedReader reader=null;

                    String encoded_message= URLEncoder.encode(message);

                    String mainUrl="https://control.msg91.com/api/sendhttp.php?";


                    StringBuilder sbPostData= new StringBuilder(mainUrl);
                    sbPostData.append("authkey="+authkey);
                    sbPostData.append("&mobiles="+phone);
                    sbPostData.append("&message="+encoded_message);
                    sbPostData.append("&route="+route);
                    sbPostData.append("&sender="+senderId);

                    mainUrl = sbPostData.toString();
                    try
                    {
                        //prepare connection
                        myURL = new URL(mainUrl);
                        myURLConnection = myURL.openConnection();
                        myURLConnection.connect();
                        reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                        //reading response
                        String response;
                        while ((response = reader.readLine()) != null) {  //print response
                            System.out.println(response);
                            Log.d("hello", response);
                        }
                        //finally close connection
                        reader.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


    }


}



