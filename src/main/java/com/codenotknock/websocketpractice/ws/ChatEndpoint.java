package com.codenotknock.websocketpractice.ws;

import com.alibaba.fastjson.JSON;
import com.codenotknock.websocketpractice.config.GetHttpSessionConfig;
import com.codenotknock.websocketpractice.pojo.Message;
import com.codenotknock.websocketpractice.util.MessageUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfig.class)    //多例的，Map用static 防止每一个都创建一个map

@Component
public class ChatEndpoint {
    private static final Map<String, Session> onLineUsers = new ConcurrentHashMap<>();

    private HttpSession httpSession;
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        /*
        首先通过EndpointConfig对象获取存储在其中的HttpSession对象。
        然后从HttpSession对象中读取属性"user"，得到用户的用户名。
        将用户的用户名和该WebSocket会话的Session对象存储在线程安全的ConcurrentHashMap集合onLineUsers中。
        最后广播消息给所有在线用户，显示用户列表。
        */

        this.httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        String user = (String)this.httpSession.getAttribute("user");
        //保存session
        onLineUsers.put(user, session);
        //向所有在线用户广播消息
        String msg = MessageUtils.getMessage(true, null, getFriends());
        broadcastAllUsers(msg);

    }
    private Set getFriends(){
        /*遍历ConcurrentHashMap集合onLineUsers，获取所有在线用户的用户名（key）。
        返回在线用户列表。
         */
        Set<String> strings = onLineUsers.keySet();
        return strings;
    }
    private void broadcastAllUsers(String msg){
//        向所有在线用户广播消息
        //遍历map集合
        Set<Map.Entry<String, Session>> entries = onLineUsers.entrySet();
        for (Map.Entry<String, Session> entry : entries){
            Session session = entry.getValue();
            // 发送消息
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
                // 记录日志...
            }
        }
    }
    @OnMessage
    public void onMessage(String msg){
        /*
        解析JSON字符串数据，得到消息的接收方用户名、消息内容、发送人的用户名等信息。
        从ConcurrentHashMap集合onLineUsers中获取接收方的Session对象。
        根据消息类型打包需要发送的响应消息，并使用接收方的Session对象发送响应信息。
         */
        Message message = JSON.parseObject(msg, Message.class);

        String toName = message.getToName();
        String mess = message.getMessage();
        String user = (String)this.httpSession.getAttribute("user");

        Session session = onLineUsers.get(toName);
        String resMessage = MessageUtils.getMessage(false, user, mess);
        try {
            session.getBasicRemote().sendText(resMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @OnClose
    public void onClose(Session session){
        /*
            从HttpSession对象中读取属性"user"，得到用户的用户名。
            从ConcurrentHashMap集合onLineUsers中移除该用户及其对应的Session对象。
            最后广播消息给所有在线用户，更新用户列表。
         */
        String user = (String)this.httpSession.getAttribute("user");
        onLineUsers.remove(user);

        String msg = MessageUtils.getMessage(true, null, getFriends());
        broadcastAllUsers(msg);
    }

}
