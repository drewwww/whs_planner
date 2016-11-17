package WHS_planner.Schedule;

import WHS_planner.Core.IO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class Schedule
{
    @FXML
    private Pane rootLayout;

    private Pane memes;

    private Map<String, Object> labels;

    private ScheduleBlock[] blocks;

    public static Scene schedule;
    public static Scene day;


    private boolean special = false;

    public Schedule()
    {
        try
        {
            buildSchedule();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void buildSchedule() throws Exception
    {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/Schedule/wankTest.fxml"));

        rootLayout = loader.load();
        generateSchedule(loader);

        schedule = new Scene(rootLayout);

        FXMLLoader load2 = new FXMLLoader();

        load2.setLocation(getClass().getResource("/Schedule/day.fxml"));

        memes = load2.load();

        day = new Scene(memes);
    }


    private void generateSchedule(FXMLLoader loader)
    {
        labels = loader.getNamespace();

        blocks = getData();


        String[] start = {"7:30", "8:32", "9:39", "10:41", "12:16", "1:18", "0:00"};
        String[] end = {"8:27", "9:29", "10:36", "12:11", "1:13", "2:15", "0:00"};

        String[] wens = {"7:30", "8:15", "9:35", "10:20", "11:40", "12:25", "0:00"};
        String[] wene = {"8:10", "8:55", "10:15", "11:35", "12:20", "1:05", "0:00"};

        String[] bells = {"7:30", "8:26", "9:58", "10:55", "12:26", "1:23", "0:00"};
        String[] belle = {"8:21", "9:18", "10:50", "12:21", "1:18", "2:15", "0:00"};

        String currentClass;
        String currentTeacher;
        String currentPeriod;
        String currentRoom;

        int incr = 0;
        int incr2 = 1;

        for (int i = 0; i < 56; i++)
        {
            String s;

            currentClass = blocks[i].getClassName();
            currentTeacher = blocks[i].getTeacher();
            currentPeriod = blocks[i].getPeriodNumber();
            currentRoom = blocks[i].getRoomNumber();

            if(blocks[i].getClassName().trim().equals("free"))
            {
                s = "free";
            }
            else
            {
                s = currentClass+"\n"+currentTeacher+"\n"+currentRoom+"\n"+ "Block:" + currentPeriod;
            }

            //System.out.println(s);

            String letter;

            switch(incr)
            {
                case 0: letter = "A";
                    break;
                case 1: letter = "B";
                    break;
                case 2: letter = "C";
                    break;
                case 3: letter = "D";
                    break;
                case 4: letter = "E";
                    break;
                case 5: letter = "F";
                    break;
                case 6: letter = "G";
                    break;
                case 7: letter = "H";
                    break;
                default: letter = "Time";
                    break;
            }

            try
            {
                Label l = (Label) labels.get(letter+incr2);
                l.setText(s);
                l.setWrapText(true);
            }
            catch(Exception e)
            {
                //e.printStackTrace();
            }

            incr++;
            if(incr == 8)
            {
                incr = 0;
                incr2++;
            }
        }

        for (int i = 1; i <= 7; i++)
        {
            Label l = (Label) labels.get("Time"+i);


            String s;

            if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4)
            {
                s = "Period "+i+"\nStart: "+"\n"+wens[i-1]+"\nEnd:\n"+wene[i-1];
            }
            else if(special)
            {
                s = "Period "+i+"\nStart: "+"\n"+bells[i-1]+"\nEnd:\n"+belle[i-1];
            }
            else
            {
                s = "Period "+i+"\nStart: "+"\n"+start[i-1]+"\nEnd:\n"+end[i-1];
            }

            l.setText(s);
            l.setWrapText(true);
        }
    }

    public void parseSchedule()
    {
        File f = new File("output.html");

        File input = new File("Keys/ipass.key");

        String user = null;
        String pass = null;

        try
        {
            if(!input.exists())
            {
                input.createNewFile();
            }

            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);

            user = br.readLine();
            pass = br.readLine();

            br.close();
            fr.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        ScheduleParser parse = new ScheduleParser();

        if(!f.exists() && user != null && pass != null )
        {
            parse.grabwebpage(user, pass);
        }
        try
        {
            parse.getClasses();
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public ScheduleBlock[] getData()
    {
        parseSchedule();
        IO dotaIo = new IO("Schedule.json");
        ArrayList<ScheduleBlock> array = dotaIo.readScheduleArray();
        dotaIo.unload();

        ScheduleBlock[] blocks = new ScheduleBlock[array.size()];
        for(int i = 0; i < array.size(); i++)
        {
            blocks[i] = array.get(i);
        }
        return blocks;
    }

    public Node getPane()
    {
        Node n = schedule.getRoot();
        return n;
    }

    public Node getdaypane()
    {
        Node n = day.getRoot();
        return n;
    }

}
