package com.example.rest;

/**
 * Created by Дамир on 10.11.2015.
 */
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Request;

import com.example.meeting.Meeting;
import com.example.participant.Participant;
import com.sun.jersey.api.view.Viewable;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.HeaderParam;

@Path("/meeting")
public class MeetingSvc {
    @Context
    ServletContext _context;
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    public static ArrayList<Meeting> meetings = new ArrayList<>();
    private String username = "user";
    private String password = "password";
    public static final String APP_MEETING_NAME="name";     // название встречи
    public static final String APP_BEGIN_DATE="begindate";  //дата начала
    public static final String APP_END_DATE="enddate";      //дата конца
    public static final String APP_PREFERENCES_NAME = "username"; // имя пользователя
    public static final String APP_PREFERENCES_PASSWORD = "password"; // пароль
    @GET
    @Path("/getMeeting")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public String getMeeting(@QueryParam("username") String username,
                             @QueryParam("password") String password) {
        if(this.username.equals(username)&& this.password.equals(password)){
            ArrayList<Meeting> currentMeetings = findMeetingsForDate();
            return currentMeetings.toString();
        }
        else
            return "[{\"response\":\"false\"}]";

    }

    @GET
    @Path("/setMeeting")
    public String setMeeting(@QueryParam("name") String name,
                               @QueryParam("description") String description,
                               @QueryParam("begindate") String begindate,
                               @QueryParam("enddate") String enddate,
                               @QueryParam("priority") String priority
    ) {
        addMeeting(name, description, begindate, enddate, priority);
        return meetings.toString();
    }

    @POST
    @Path("/mobileSetMeeting")
    public void setMeetings(@QueryParam("name") String name,
                            @QueryParam("description") String description,
                            @QueryParam("begindate") String begindate,
                            @QueryParam("enddate") String enddate,
                            @QueryParam("priority") String priority,
                            @QueryParam("username") String username,
                            @QueryParam("password") String password){
        addMeeting(name, description, begindate, enddate, priority);
    }

    @POST
    @Path("/getDescription")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public String getDescription(String data) {
        String res = null;
        try {
            String decodedValue1 = URLDecoder.decode(data, "UTF-8");
            String[] splitStr = decodedValue1.split("[=&]");
            String name = splitStr[1];
            String begindate = splitStr[3];
            String enddate = splitStr[5].substring(0, splitStr[5].length()-2);
            Meeting result = findMeeting(name, begindate, enddate);
            if (result != null)
                res = "\"description\":"+result.getDescription();
            else
                res = "[]";
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } finally {
            return res;
        }
    }

    @PUT
    @Path("/addParticipant")
    public void addParticipant(String data) {
        try {
            String decodedValue1 = URLDecoder.decode(data, "UTF-8");
            String[] splitStr = decodedValue1.split("[=&]");
            String name = splitStr[1];
            String begindate = splitStr[3];
            String enddate = splitStr[5];
            Participant participant = new Participant();
            participant.setLastName(splitStr[7]);
            participant.setFirstName(splitStr[9]);
            participant.setPatronymic(splitStr[11]);
            participant.setPost(splitStr[13].substring(0, splitStr[13].length() - 2));
            Meeting m = findMeeting(name, begindate, enddate);

            if (m != null) {
                ArrayList<Participant> participants = m.getParticipants();
                if (participants == null)
                    participants = new ArrayList<Participant>();
                participants.add(participant);
                m.setParticipants(participants);
                meetings.add(m);
            }


        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
    }


   // @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")

    @DELETE
    @Path("/deleteMeeting")
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public void deleteMeeting(@HeaderParam("data") String data){
//                              @PathParam("begindate") String begindate,
//                                    @PathParam("enddate") String enddate,
//                                    @PathParam("username") String username,
//                                    @PathParam("password") String password){
        String decoded ="";
        String name = request.getHeader("data");
        try {
            byte[] utf8 = data.getBytes("UTF-8");
            decoded = new String(utf8);
       // if(this.username.equals(username)&& this.password.equals(password)) {

                //String parse = URLDecoder.decode(data, "UTF-8");
                //String[] splitStr = parse.split("[=&]");
                String n = URLDecoder.decode(data, "UTF-8");
//                begindate = URLDecoder.decode(begindate, "UTF-8");
//                enddate = URLDecoder.decode(enddate, "UTF-8");
//                username = URLDecoder.decode(username, "UTF-8");
//                password = URLDecoder.decode(password, "UTF-8");
//                if(this.username.equals(username)&& this.password.equals(password)) {
//                    Meeting m = findMeeting(name, begindate, enddate);
//                    if (m != null) {
//                        meetings.remove(m);
//                        result = "[{\"response\":\"true\"}]";
//                    }
//                }
                throw new Exception(name +";"+ decoded+ ";"+data);
//                JSONObject obj = array.getJSONObject(0);
//                String name = obj.getString(APP_MEETING_NAME);
//                String begindate = obj.getString(APP_BEGIN_DATE);
//                String enddate = obj.getString(APP_END_DATE);
//                String username = obj.getString(APP_PREFERENCES_NAME);
//                String password = obj.getString(APP_PREFERENCES_PASSWORD);
//                if(this.username.equals(username)&& this.password.equals(password)) {
//                    String n = URLDecoder.decode(name, "UTF-8");
//                    String b = URLDecoder.decode(begindate, "UTF-8");
//                    String e = URLDecoder.decode(enddate, "UTF-8");
//                    Meeting m = findMeeting(n, b, e);
//                    if (m != null) {
//                        meetings.remove(m);
//                        result = "[{\"response\":\"true\"}]";
//                    }
//                }
            } catch (UnsupportedEncodingException uee) {
                uee.printStackTrace();
            } catch(Exception e){
                System.out.println(e.getMessage());
            } finally {
                //return result;
            }
    }


    private Meeting findMeeting(String name, String begindate, String enddate) {
        Meeting meeting = null;
        for (Meeting m : meetings) {
            if (m.getName().equals(name) && m.getBeginData().equals(begindate) && m.getEndData().equals(enddate)) {
                meeting = m;
            }
        }
        return meeting;
    }

    private ArrayList<Meeting> findMeetingsForDate(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = format.format(date);
        ArrayList<Meeting> currentMeetings = new ArrayList<Meeting>();
        for(Meeting m : meetings){
            if(m.getBeginData().equals(currentDate)){
                currentMeetings.add(m);
            }
        }
        return currentMeetings;
    }
    @POST
    @Path("/getMeetOnDes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public String findMeetingOnDescription(String description){
        Meeting meeting =null;
        description = description.substring(description.indexOf("=")+1);
        description = description.substring(0, description.length()-2);
        for(Meeting m : meetings){
            if(m.getDescription().equals(description))
                meeting=m;
        }
        if(meeting!=null)
            return meeting.toString();
        else
            return null;
    }



    private void addMeeting(String name,
                            String description,
                            String begindate,
                            String enddate,
                            String priority) {
        Meeting meeting = null;
        try {
            //String decodedValue1 = URLDecoder.decode(data, "UTF-8");
            //String[] splitStr = decodedValue1.split("[=&]");
            meeting = new Meeting();
            meeting.setName(URLDecoder.decode(name, "UTF-8"));
            meeting.setDescription(URLDecoder.decode(description, "UTF-8"));
            meeting.setBeginData(URLDecoder.decode(begindate, "UTF-8"));
            meeting.setEndData(URLDecoder.decode(enddate, "UTF-8"));
            String prior = URLDecoder.decode(priority, "UTF-8");
            if(prior.endsWith("\r\n"))
                prior = prior.substring(0, prior.length()-2);
            Meeting.Priority p = meeting.getPriority();
            switch (prior) {
                case "Срочная": {
                    p = Meeting.Priority.URGENT;
                }
                break;
                case "Плановая": {
                    p = Meeting.Priority.ROUTINE;
                }
                break;
                case "По возможности": {
                    p = Meeting.Priority.POSSIBLE;
                }
                break;
            }
            meeting.setPriority(p);
            meetings.add(meeting);
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
    }
}
